package project_02_TankWar;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankNewMsg implements Msg {
	Tank tank;
	TankClient tc;
	int MsgType = Msg.TANK_NEW_MSG;
	
	public TankNewMsg(Tank tank) {
		this.tank = tank;
	}
	
	public TankNewMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(MsgType);
			dos.writeInt(tank.id);
			dos.writeInt(tank.getX());
			dos.writeInt(tank.getY());
			dos.writeInt(tank.getDir().ordinal());
			dos.writeBoolean(tank.isGood());
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, udpPort));
			ds.send(dp);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (tc.tank.id == id) {
				return;
			}			
			
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();
			
			boolean exist = false;
			for(int i = 0; i < tc.tanks.size(); i++) {
				Tank t = tc.tanks.get(i);
				if(t.id == id) {
					exist = true;
					break;
				}
			}
						
			if(!exist) {
//				把原有的Tank消息发送给新加进来的Tank。
				TankNewMsg tnMsg = new TankNewMsg(tc.tank);
				tc.netClient.send(tnMsg);
				
				Tank t = new Tank(x, y, good, dir,this.tc);
				t.id = id;
				tc.tanks.add(t);
			}
			
//System.out.println("id:" + id + " x:" + x + " y:" + y + " dir:" + dir +" good:" + good); 
		} catch (EOFException e) {
//			System.out.println("msg EOFException");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

}
