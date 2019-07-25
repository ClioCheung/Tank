package project_02_TankWar;

import java.awt.Color;
import java.awt.Graphics;

public class Explode {
	int x;
	int y;
	private TankWarClient tc;
	
	private boolean live = true;
	
	int[] diameter = {2,5,9,16,26,32,49,80,150,90,30,18,6};
	int step = 0;
	
	
	public Explode(int x, int y, TankWarClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	
	public void draw(Graphics g) {
		if(!live) {
			tc.explodes.remove(this);
			return;
		}
		
		if(step == diameter.length) {
			live = false;
			step = 0;
		}
		
		Color c = g.getColor();
		g.setColor(Color.orange);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step++;
	}
	
	
}
