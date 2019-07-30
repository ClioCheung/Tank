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
	private static int UDP_PORT_START = 2223;
	private int udpPort;
	private TankClient tc;
	DatagramSocket ds = null;
	
	
	public NetClient(TankClient tc) {
		this.udpPort = UDP_PORT_START ++;
		this.tc = tc;
		
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void connected(String IP,int port) {
		Socket socket = null;
		try {
			socket = new Socket(IP,port);
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
		
		TankClientMsg msg = new TankClientMsg(tc.tank);
		send(msg);	
		new Thread(new UDPReceiveThread()).start();
		
	}


	private void send(TankClientMsg msg) {
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
System.out.println("A packet receive form server!");
					parse(dp);
				} catch (IOException e) {
				
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf,0,dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			
			TankClientMsg msg = new TankClientMsg(tc.tank);
			msg.parse(dis);
		}
		
	}
	
}












