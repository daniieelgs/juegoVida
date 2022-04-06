package jocVida;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

//©Daniel Garcia (daniieelgs@gmail.com)

public class cell extends JPanel implements Runnable{
	
	private static final long serialVersionUID = 1L;
	private int x, y;
	private int ruleLiveMin, ruleLiveMax, ruleDie;
	private boolean live;
	
	private final Color COLOR_LIVE = Color.WHITE;
	private final Color COLOR_DIE = Color.BLACK;
	
	private cell[] neighbors;
	
	public cell(int x, int y, int ruleLive, int ruleDie) {
		super(null);
		this.x=x;
		this.y=y;
		this.ruleDie=ruleDie;
		
		if(ruleLive<10) {
			ruleLiveMin = 0;
			ruleLiveMax = ruleLive;
		}else {
			
			String s = String.valueOf(ruleLive);
			
			ruleLiveMin = Integer.parseInt(s.substring(0, 1));
			ruleLiveMax = Integer.parseInt(s.substring(1, 2));
						
		}
		
		die();
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		clickListener();
	}

	public void setNeighbors(cell[] neighbors) {
		this.neighbors = neighbors;
	}
	
	public cell[] getNeighbors() {
		return neighbors;
	}
	
	public void live() {
		live=true;
		setBackground(COLOR_LIVE);
	}
	
	public void die() {
		live=false;
		setBackground(COLOR_DIE);
	}
	
	public boolean isLive() {
		return live;
	}
	
	public int getPosX() {
		return x;
	}
	
	public int getPosY() {
		return y;
	}

	public cell[] getNeighborsLive() {
		
		cell[] neighbors = getNeighbors();
		
		ArrayList<cell> neighborsLive = new ArrayList<>();
		
		neighborsLive.ensureCapacity(neighbors.length);
		
		for(cell cell : neighbors) if(cell.isLive()) neighborsLive.add(cell);
		
		neighborsLive.trimToSize();
		
		return neighborsLive.toArray(new cell[neighborsLive.size()]);
		
	}
	
	public void clickListener() {
		
		this.addMouseListener(new MouseAdapter() {
		
			public void mouseClicked(MouseEvent e) {
				
				active();
			}
			
			public void mouseEntered(MouseEvent e) {
				
				if(e.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK | e.getModifiersEx()==MouseEvent.BUTTON2_DOWN_MASK)
					active();
				
			}
		
		});
		
	}
	
	private void active() {
		
		if(live) die();	
		else live();
		
	}
	
	private boolean futureDie=false;
	private boolean futureLive=false;;
	
	public void run() {
		
		int numLiveNeigbors = getNeighborsLive().length;
	
//		futureDie=(isLive() && !(numLiveNeigbors==2 || numLiveNeigbors==3));
		futureDie=(isLive() && !(numLiveNeigbors>=ruleLiveMin && numLiveNeigbors<=ruleLiveMax));
		futureLive=(!isLive() && numLiveNeigbors==ruleDie);
		
		//if(isLive() && !(numLiveNeigbors==2 || numLiveNeigbors==3)) die();
		//if(!isLive() && numLiveNeigbors==3) live();
		
	}
	
	public void exec() {
		
		if(futureDie) die();
		if(futureLive) live();
		
	}
	
}

