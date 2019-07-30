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

public class TankClientMsg {
	Tank tank;

	public TankClientMsg(Tank tank) {
		this.tank = tank;
	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
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
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();
System.out.println("id:" + id + " x:" + x + " y:" + y + " dir:" + dir +" good:" + good); 
		} catch (EOFException e) {
//			System.out.println("msg EOFException");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

}
