package project_02_TankWar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TankServer {
	public static final int TCP_PORT = 6666;
			
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(TCP_PORT);
			while(true) {
				Socket socket = serverSocket.accept();
System.out.println("A Client connected! Addr- " + socket.getInetAddress() + ":" + socket.getPort());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
