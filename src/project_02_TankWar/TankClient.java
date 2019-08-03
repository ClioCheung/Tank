package project_02_TankWar;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	public static final Frame TankClient = null;
	Image offSreenImage = null;

	Tank tank = new Tank(50, 50, true, this);

	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();

	NetClient netClient = new NetClient(this);
	ConnDialog dialog = new ConnDialog();

	public static void main(String[] args) {
		TankClient t = new TankClient();
		t.launch();
	}

	private void launch() {
	/*	for (int i = 0; i < 10; i++) {
			enemyTanks.add(new Tank(50 + 40 * (i + 9), 60, false, this));
		}*/

		this.setTitle("TankWar");
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setLocation(500, 50);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.addKeyListener(new KeyMonitor());
		this.setResizable(false);
		this.setVisible(true);

		new Thread(new PaintThread()).start();

//		netClient.connected("192.168.88.8", TankServer.TCP_PORT);

	}

	@Override
	public void paint(Graphics g) {
		g.drawString("Missiles's count: " + missiles.size(), 10, 50);
		g.drawString("explodes's count: " + explodes.size(), 10, 70);
		g.drawString("EnemyTanks's count: " + tanks.size(), 10, 90);

		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
//			打敌人
			m.hitTanks(tanks);
//			敌人打我
			m.hitTank(tank);
			m.draw(g);
		}

		for (int i = 0; i < explodes.size(); i++) {
			Explode explode = explodes.get(i);
			explode.draw(g);
		}

		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.draw(g);
		}

		tank.draw(g);
	}

	@Override
	public void update(Graphics g) {
		if (offSreenImage == null) {
			offSreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}

		Graphics gOffSreen = offSreenImage.getGraphics();
		Color c = gOffSreen.getColor();
		gOffSreen.setColor(getBackground());
		gOffSreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffSreen.setColor(c);
		paint(gOffSreen);

		g.drawImage(offSreenImage, 0, 0, null);

	}

	private class PaintThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}

	}

	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			tank.keyPressed(e);

		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_C) {
				dialog.setVisible(true);
			} else {
				tank.keyReleased(e);
			}
			
		}

	}
	
	
	class ConnDialog extends Dialog {
		TextField tfIP = new TextField("192.168.88.8",12);
		TextField tfPort = new TextField("" + TankServer.TCP_PORT,4);
		TextField myUdpPort = new TextField("2223",4);
		Button bConfirm = new Button("Confirm"); 
		
		public ConnDialog() {
			super(TankClient.this, true);
			this.setLayout(new FlowLayout());
			this.add(new Label("IP: "));
			this.add(tfIP);
			this.add(new Label("Port: "));
			this.add(tfPort);
			this.add(new Label("MyUdpPort: "));
			this.add(myUdpPort);
			this.add(bConfirm);
					
			this.setLocation(300, 300);
			this.pack();
			
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
				
			});
			
			bConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String IP = tfIP.getText().trim();
					int serverTcpPort = Integer.parseInt(tfPort.getText().trim());
					int udpPort = Integer.parseInt(myUdpPort.getText().trim());
					
					netClient.setUdpPort(udpPort);
					netClient.connected(IP, serverTcpPort);
					setVisible(false);
				}
				
			});
	
		}
	
	}
	
}