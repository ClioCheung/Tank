package project_02_TankWar;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MissileDeathMsg implements Msg {
	int msgType = Msg.MISSILE_DEATH_MSG;
	TankClient tc;
	int tankId;
	int id;
	
	public MissileDeathMsg(int tankId,int id) {
		this.tankId = tankId;
		this.id = id;
	}
	
	public MissileDeathMsg(TankClient tc) {
		this.tc = tc;
	}
	
	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(tankId);
			dos.writeInt(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] buf = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(IP,udpPort));
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
			int id = dis.readInt();
			
			for(int i = 0;i < tc.missiles.size(); i++) {
				Missile m = tc.missiles.get(i);
				if(tankId == m.getTankId() && id == m.getId()) {
					m.setLive(false);
					tc.explodes.add(new Explode(m.getX(),m.getY(),tc));
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
