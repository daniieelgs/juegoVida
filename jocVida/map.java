package jocVida;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

//©Daniel Garcia (daniieelgs@gmail.com)

public class map extends JPanel{

	private static final long serialVersionUID = 1L;
	private cell[][] cells;
	private int sizeX, sizeY;
	
	public map(int sizeX, int sizeY, int ruleLive, int ruleDie, Dimension size) {
				
		super(new GridBagLayout());
		
		this.sizeX=sizeX;
		this.sizeY=sizeY;
		
		cells = new cell[sizeY][sizeX];
				
		int sizeBox = getSizeBoxing(sizeX, sizeY, size);
		
		for(int y=0; y<sizeY; y++) 
			
			for(int x=0; x<sizeX; x++) 
				
				addCell(createCell(x, y, ruleLive, ruleDie), sizeBox);
				
		this.updateUI();
		
		resizeListener();
		
	}
	
	
	public map(Dimension d) {
				
		this(30, 30, 23, 3, d); 
		
	}
	
	public cell createCell(int x, int y, int ruleLive, int ruleDie) {
		
		cell c=new cell(x, y, ruleLive, ruleDie);
		
		c.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		
		return c;
		
	}
	
		
	public void addCell(cell cell, int dim) {
		
		GridBagConstraints constraint=new GridBagConstraints();

		
		constraint.fill=GridBagConstraints.BOTH;
		
		constraint.gridx=cell.getPosX();
		constraint.gridy=cell.getPosY();
		
		constraint.ipadx=dim;
		constraint.ipady=dim;		
				
		
		add(cell, constraint);
						
		cells[cell.getPosY()][cell.getPosX()]=cell;
	}
	
	private int getSizeBoxing(int sizeX, int sizeY, Dimension size) {
		
		int dimension=(sizeX > sizeY ? sizeX : sizeY);
		
		return ((size.width<size.height ? size.width : size.height)-50)/dimension;
	}
	
	public cell[][] getMap(){
		return cells;
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}
		
	public void clear() {
		
		for (cell[] cell : cells)
			for (cell c : cell)
				c.die();
		
		
	}
	
	private void resizeListener() {
		
		this.addComponentListener(new ComponentAdapter() {
			
			public void componentResized(ComponentEvent e) {
				
				resize();
				
			}
		});
		
	}
	
	public void resize() {
				
		int sizeBox = getSizeBoxing(sizeX, sizeY, getSize());
		
		removeAll();
		
		for (cell[] cell : cells)
			for (cell c : cell)
				addCell(c, sizeBox);
			
		
	}	
}
