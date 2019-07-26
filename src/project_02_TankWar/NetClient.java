package project_02_TankWar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClient {
	private static int UDP_PORT_START = 2223;
	private int udpPort;
	private TankWarClient tc;
	
	public NetClient(TankWarClient tc) {
		this.udpPort = UDP_PORT_START ++;
		this.tc = tc;
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
	
	}

}
