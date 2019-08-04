package project_02_TankWar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {
	int id;
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;

	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;

	private boolean L = false, U = false, R = false, D = false;

	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;

	private int x;
	private int y;

	TankClient tc;

	private boolean good = false;

	private boolean live = true;

	private static Random random = new Random();
	private int step = random.nextInt(12) + 3;

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public boolean isGood() {
		return good;
	}

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}

	public Tank(int x, int y, boolean good, TankClient tc) {
		this(x, y, good);
		this.tc = tc;
	}
	
	public Tank(int x, int y, boolean good, Direction dir,TankClient tc) {
		this(x, y, good,tc);
		this.dir = dir;
	}

	public void draw(Graphics g) {
		if (!live) {
			if (!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if (good) {
			g.setColor(Color.MAGENTA);
			g.drawString("ID : " + id, x, y - 10);
		} else {
			g.setColor(Color.gray);
		}
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);

		move();

		switch (ptDir) {
		case L:
			g.drawLine(x, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case LU:
			g.drawLine(x, y, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case U:
			g.drawLine(x + WIDTH / 2, y, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case RU:
			g.drawLine(x + WIDTH, y, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case R:
			g.drawLine(x + WIDTH, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case RD:
			g.drawLine(x + WIDTH, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case D:
			g.drawLine(x + WIDTH / 2, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case LD:
			g.drawLine(x, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case STOP:
			break;
		default:
			break;
		}

	}

	private void move() {
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}

		if (dir != Direction.STOP) {
			this.ptDir = this.dir;
		}

		if (x < 0) {
			x = 0;
		}
		if (y < 25) {
			y = 25;
		}
		if (x + Tank.WIDTH > TankClient.GAME_WIDTH) {
			x = TankClient.GAME_WIDTH - Tank.WIDTH;
		}
		if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT) {
			y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		}

	/*	if (!good) {
			Direction[] directions = Direction.values();
			if (step == 0) {
				step = random.nextInt(20) + 3;
				int rn = random.nextInt(directions.length);
				dir = directions[rn];
			}

			if (random.nextInt(50) > 48) {
				this.fire();
			}
			step--;
		}*/

	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			L = true;
			break;
		case KeyEvent.VK_UP:
			U = true;
			break;
		case KeyEvent.VK_RIGHT:
			R = true;
			break;
		case KeyEvent.VK_DOWN:
			D = true;
			break;
		}
		
		locateDirection();
	}

	private Missile fire() {
		if (!this.isLive())
			return null;

		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(id,x, y, good, ptDir, this.tc);
		tc.missiles.add(m);
		
		MissileNewMsg msg = new MissileNewMsg(m);
		tc.netClient.send(msg);
		
		return m;
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			L = false;
			break;
		case KeyEvent.VK_UP:
			U = false;
			break;
		case KeyEvent.VK_RIGHT:
			R = false;
			break;
		case KeyEvent.VK_DOWN:
			D = false;
			break;
		}

		locateDirection();
	}

	private void locateDirection() {
		Direction oldDir = this.dir;
		
		if (L & !U & !R & !D) {
			dir = Direction.L;
		} else if (L & U & !R & !D) {
			dir = Direction.LU;
		} else if (!L & U & !R & !D) {
			dir = Direction.U;
		} else if (!L & U & R & !D) {
			dir = Direction.RU;
		} else if (!L & !U & R & !D) {
			dir = Direction.R;
		} else if (!L & !U & R & D) {
			dir = Direction.RD;
		} else if (!L & !U & !R & D) {
			dir = Direction.D;
		} else if (L & !U & !R & D) {
			dir = Direction.LD;
		} else if (!L & !U & !R & !D) {
			dir = Direction.STOP;
		}
		
		if(dir != oldDir) {
			TankMoveMsg msg = new TankMoveMsg(id,dir);
			tc.netClient.send(msg);
		}		
	}

	public Rectangle getRectangle() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
}
