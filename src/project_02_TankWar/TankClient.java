package project_02_TankWar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	Image offSreenImage = null;

	Tank tank = new Tank(50, 50, true, this);

	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> enemyTanks = new ArrayList<Tank>();

	NetClient netClient = new NetClient(this);

	public static void main(String[] args) {
		TankClient t = new TankClient();
		t.launch();
	}

	private void launch() {
		for (int i = 0; i < 10; i++) {
			enemyTanks.add(new Tank(50 + 40 * (i + 9), 60, false, this));
		}

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

		netClient.connected("192.168.88.8", TankServer.TCP_PORT);

	}

	@Override
	public void paint(Graphics g) {
		g.drawString("Missiles's count: " + missiles.size(), 10, 50);
		g.drawString("explodes's count: " + explodes.size(), 10, 70);
		g.drawString("EnemyTanks's count: " + enemyTanks.size(), 10, 90);

		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
//			打敌人
			m.hitTanks(enemyTanks);
//			敌人打我
			m.hitTank(tank);
			m.draw(g);
		}

		for (int i = 0; i < explodes.size(); i++) {
			Explode explode = explodes.get(i);
			explode.draw(g);
		}

		for (int i = 0; i < enemyTanks.size(); i++) {
			Tank t = enemyTanks.get(i);
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
			tank.keyReleased(e);
		}

	}

}