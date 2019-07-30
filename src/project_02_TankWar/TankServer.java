package project_02_TankWar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class TankServer {
	private static int ID = 100;
	public static final int TCP_PORT = 6666;
	public static final int UDP_PORT = 2222;

	List<Client> clients = new ArrayList<Client>();

	public static void main(String[] args) {
		new TankServer().start();
	}


	public void start() {
		new Thread(new UDPThread()).start();

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(TCP_PORT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

			while (true) {
				Socket socket = null;
				try {
					socket = serverSocket.accept();
	
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					String IP = socket.getInetAddress().getHostAddress();
					int udpPort = dis.readInt();
	
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					dos.writeInt(ID++);
	
					Client c = new Client(IP, udpPort);
					clients.add(c);
	
System.out.println("A Client connected! Addr- " + socket.getInetAddress() + ":" + socket.getPort()
							+ "  ----udpPort:" + udpPort);
		
		
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
	}

	private class Client {
		String IP;
		int udpPort;

		public Client(String IP, int udpPort) {
			this.IP = IP;
			this.udpPort = udpPort;
		}

	}

	private class UDPThread implements Runnable {
		byte[] buf = new byte[1024];
		DatagramSocket ds = null;
		DatagramPacket dp = null;

		@Override
		public void run() {
			try {
				ds = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
System.out.println("A thread started up at port: " + UDP_PORT);

			while (ds != null) {
				dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
System.out.println("A packet received!");
				for(int i = 0;i < clients.size();i++) {
					Client c = clients.get(i);
					dp.setSocketAddress(new InetSocketAddress(c.IP,c.udpPort));
					ds.send(dp);
				}

					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
