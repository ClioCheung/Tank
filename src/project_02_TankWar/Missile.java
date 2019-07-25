package project_02_TankWar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {
	public static final int XSPEED = 10; 
	public static final int YSPEED = 10; 
	
	public static final int WIDTH = 10; 
	public static final int HEIGHT = 10; 
	
	private int x;
	private int y;
	
	boolean L=false,U=false,R=false,D=false;
	Direction dir;
	
	TankWarClient tc;
	private boolean live = true;
	private boolean good;


	public boolean isLive() {
		return live;
	}


	public Missile(int x, int y,Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	
	public Missile(int x, int y,boolean good,Direction dir,TankWarClient tc) {
		this(x,y,dir);
		this.good = good;
		this.tc = tc;
	}

	
	public void draw(Graphics g) {
		if(!live) {	
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		
		if(good) g.setColor(Color.red);
		else g.setColor(Color.gray);
		
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}
	
	
	private void move() {
		switch(dir) {
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
		default:
			break;
		}
		
		if(x < 0 || y < 0 || x > TankWarClient.GAME_WIDTH || y > TankWarClient.GAME_HEIGHT) {
			live = false;
		}
		
	}
	
	
	public Rectangle getRectangle() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	
	public boolean hitTank(Tank t) {
		if(this.isLive() && this.getRectangle().intersects(t.getRectangle()) && t.isLive() && this.good != t.isGood()) {
			t.setLive(false);
			this.live = false;
			Explode e = new Explode(x,y,this.tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i =0;i < tanks.size();i++) {
			Tank t = tanks.get(i);
			if(hitTank(t)) {
				t.setLive(false);
				return true;
			}
//			tc.enemyTanks.remove(t);
		}
		return false;		
	}
	

}
