package project_02_TankWar;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetClient {
	private int udpPort;
	private TankClient tc;
	DatagramSocket ds = null;
	
	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public NetClient(TankClient tc) {
		this.tc = tc;
	}
	
	public void connected(String IP,int tcpPort) {
		Socket socket = null;
		
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		} 
				
		try {
			socket = new Socket(IP,tcpPort);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeInt(udpPort);

			DataInputStream dis = new DataInputStream(socket.getInputStream());
			int id = dis.readInt();
			tc.tank.id = id;
System.out.println("connected to server! and Server give me a ID: " + id);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null) {
				try {
					socket.close();
					socket = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		TankNewMsg msg = new TankNewMsg(tc.tank);
		send(msg);	
		
		new Thread(new UDPReceiveThread()).start();
	}


	public void send(Msg msg) {
		msg.send(ds,"192.168.88.8",TankServer.UDP_PORT);
	}
	
	private class UDPReceiveThread implements Runnable {
		byte[] buf = new byte[1024];

		@Override
		public void run() {
			while(ds != null) {
				DatagramPacket dp = new DatagramPacket(buf,0,buf.length);
				try {
					ds.receive(dp);
System.out.println("A packet received form server!");
					parse(dp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf,0,dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			int msgType = 0;
			try {
				msgType = dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Msg msg = null; 
			switch(msgType){
			case Msg.TANK_NEW_MSG:
				msg = new TankNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_MOVE_MSG:
				msg = new TankMoveMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_NEW_MSG:
				msg = new MissileNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			}
//			TankClientMsg msg = new TankClientMsg(tc);
//			在一个内部访问封装类的成员变量：封装类名.this.成员变量名——NetClient.this.tc
//			TankNewMsg msg = new TankNewMsg(NetClient.this.tc);
		}
		
	}
	
}












