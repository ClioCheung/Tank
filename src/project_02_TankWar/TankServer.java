package project_02_TankWar;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TankServer {
	public static final int TCP_PORT = 6666;
	List<Client> clients = new ArrayList<Client>();
		
	
	public static void main(String[] args) {
		new TankServer().start();
	}
	
	
	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(TCP_PORT);
			while(true) {
				socket = serverSocket.accept();
System.out.println("A Client connected! Addr- " + socket.getInetAddress() + ":" + socket.getPort());
				
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				String IP = socket.getInetAddress().getHostAddress();
				int udpPort = dis.readInt();
				
				Client c = new Client(IP,udpPort);
				clients.add(c);
				socket.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	
	private class Client {
		String IP;
		int udpPort;
				
		public Client(String IP,int udpPort) {
			this.IP = IP;
			this.udpPort = udpPort;
		}
				
	}

}
