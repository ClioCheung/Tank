package project_02_TankWar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClient {
	private static int UDP_PORT_START = 2223;
	private int udpPort;
	
	public NetClient() {
		this.udpPort = UDP_PORT_START ++;
	}
	
	
	public void connected(String IP,int port) {
		Socket socket = null;
		try {
			socket = new Socket(IP,port);
System.out.println("connected to server!");

			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeInt(port);

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
