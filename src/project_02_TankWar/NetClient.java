package project_02_TankWar;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClient {
	
	public void connected(String IP,int port) {
		try {
			Socket socket = new Socket(IP,port);
System.out.println("connected to server!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

}
