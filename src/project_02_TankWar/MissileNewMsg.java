package project_02_TankWar;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MissileNewMsg implements Msg {
	int MsgType = Msg.MISSILE_NEW_MSG;
	private Missile m;
	TankClient tc;
	
	public MissileNewMsg(Missile m) {
		this.m = m;
	}
	public MissileNewMsg(TankClient tc) {
		this.tc = tc;
	}
		
	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(MsgType);
			dos.writeInt(m.getTankId());
			dos.writeInt(m.getX());
			dos.writeInt(m.getY());
			dos.writeInt(m.dir.ordinal());
			dos.writeBoolean(m.isGood());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] buf = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP,udpPort));
		try {
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void parse(DataInputStream dis) {
		try {
			int tankId = dis.readInt();
			if(tankId == tc.tank.id) {
				return;
			}
			
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();
			
			Missile m = new Missile(tankId,x,y,good,dir,this.tc);
			tc.missiles.add(m);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
