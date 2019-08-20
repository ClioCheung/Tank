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
	int x,y;
	Direction dir;
	Direction ptDir;
	TankClient tc;
	int MsgType =  Msg.TANK_MOVE_MSG;
	
	
	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}

	
	public TankMoveMsg(int id,int x,int y,Direction dir,Direction ptDir) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.ptDir = ptDir;
	}
	
	
	@Override
	public void send(DatagramSocket ds, String IP, int serverUdpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(MsgType);
			dos.writeInt(id);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());
			dos.writeInt(ptDir.ordinal());
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
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			Direction ptDir = Direction.values()[dis.readInt()];
			
			boolean exist = false;
			for(int i = 0;i < tc.tanks.size();i++) {
				Tank t = tc.tanks.get(i);
				if(t.id == id) {
					t.setX(x);
					t.setY(y);
					t.setDir(dir);
					t.setPtDir(ptDir);
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