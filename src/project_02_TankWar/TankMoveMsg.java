package project_02_TankWar;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankMoveMsg implements Msg {
	int id;
	Direction dir;
	TankClient tc;
	int MsgType =  Msg.TANK_MOVE_MSG;
	
	
	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}

	
	public TankMoveMsg(int id, Direction dir) {
		this.id = id;
		this.dir = dir;
	}
	
	
	@Override
	public void send(DatagramSocket ds, String IP, int serverUdpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(MsgType);
			dos.writeInt(id);
			dos.writeInt(dir.ordinal());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
				
		byte[] buf = baos.toByteArray();			
		DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(IP,serverUdpPort));
		try {
			ds.send(dp);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if(tc.tank.id == id) {
				return;
			}
			Direction dir = Direction.values()[dis.readInt()];
			
			boolean exist = false;
			for(int i = 0;i < tc.tanks.size();i++) {
				Tank t = tc.tanks.get(i);
				if(t.id == id) {
					t.setDir(dir);
					exist = true;
					break;
				}
			}
			
		} catch (SocketException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

}