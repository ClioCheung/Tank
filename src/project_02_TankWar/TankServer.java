package project_02_TankWar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TankServer {
	private static int ID = 100;
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
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			while(true) {
				socket = serverSocket.accept();
				
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				String IP = socket.getInetAddress().getHostAddress();
				int udpPort = dis.readInt();
				
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeInt(ID++);
				
				Client c = new Client(IP,udpPort);
				clients.add(c);
System.out.println("A Client connected! Addr- " + socket.getInetAddress() + ":" + socket.getPort() +  " udpPort ----" + udpPort);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
					socket = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
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
