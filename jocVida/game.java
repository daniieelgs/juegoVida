package jocVida;

import java.awt.Toolkit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import javax.swing.JOptionPane;

//©Daniel Garcia (daniieelgs@gmail.com)

public class game {

	private map mapa;
	
	private final int COLONIES_AUTO=4; //5
	
	private final int MIN_CELULES_MANUAL=5;
		
	private int count_celulesManual;
	
	private long vel;
	
	private boolean playing;
	
	private int generation;
			
	private int initialGrups;
	
	private boolean random;
	
	private boolean executing;
	
	public game(map mapa, int vel) { //rules
		
		this.mapa = mapa;
		
		for (cell[] cell : mapa.getMap())
			for (cell c : cell)
				c.setNeighbors(setNeighborsCell(c, mapa.getMap(), mapa.getSizeX(), mapa.getSizeY()));
		
		this.vel = vel;
		playing = false;
		generation = 0;
		initialGrups = 0;
		count_celulesManual=0;
		random = false;
	}	
	
	public game(map mapa, int vel, int grups) {
		
		this(mapa, vel);
		
		initialGrups = grups;
		
		random = true;
		
		random(grups, mapa.getMap());
	}
	
	
	public int getNumNeighbors(int x, int y, int sizeX, int sizeY) {
		int numNeighbors = 8;
		
		if(x==0 || x==sizeX-1) numNeighbors-=2;
		if(y==0 || y==sizeY-1) numNeighbors-=2;
		if(numNeighbors<8) numNeighbors--;
		
		return numNeighbors;
	}
	
	public int[][] getPosNeighbors(cell c, int sizeX, int sizeY, int numNeighbors) {
		
		int neighborsTop = c.getPosY()-1;
		int neighborsBottom = c.getPosY()+1;
		
		int neighborsLeft = c.getPosX()-1;
		int neighborsRight = c.getPosX()+1;
		
		int[][] posNeigbors = new int[numNeighbors][2];
		
		int cont=0;
		
		for(int i=-1; i<2; i++) {
			
			for(int j=0; j<2; j++) {
				
				int neigbhorY = j==0 ? neighborsTop : neighborsBottom;
				
				if(neigbhorY>=0 && neigbhorY<sizeY) {
					
					int neigbhorX = c.getPosX()-i;
					
					if(neigbhorX>=0 && neigbhorX<sizeX) { 
						posNeigbors[cont][0] = neigbhorX;
						posNeigbors[cont][1] = neigbhorY;
						cont++;
					}
					
				}
			}
			
		}
		
		for(int j=0; j<2; j++) {
			
			int neigbhorX = j==0 ? neighborsLeft : neighborsRight;
			int neigbhorY = c.getPosY();					
			
			if(neigbhorX>=0 && neigbhorX<sizeX && neigbhorY>=0 && neigbhorY<sizeY) {
				posNeigbors[cont][0] = neigbhorX;
				posNeigbors[cont][1] = neigbhorY; //cells[neigbhorY][neigbhorX]
				cont++;
			}	
		}
		
		
		return posNeigbors;
	}
	
	public cell[] setNeighborsCell(cell c, cell[][] cells, int sizeX, int sizeY) {
		
		int x = c.getPosX();
		int y = c.getPosY();
		
		int numNeigbors = getNumNeighbors(x, y, sizeX, sizeY);
		
		cell[] neighbors = new cell[numNeigbors];
		
		int[][] posNeigbors = getPosNeighbors(c, sizeX, sizeY, numNeigbors);
		
		for(int i=0; i<posNeigbors.length; i++)
			neighbors[i] = cells[posNeigbors[i][1]][posNeigbors[i][0]];
		
		return neighbors;
	}
	
	public void random(int grups, cell[][] mapa) {
		
		int sizeY=mapa.length;
		int sizeX=mapa[0].length;
		
		ExecutorService pool=Executors.newCachedThreadPool();
		
		for(int i=0; i<grups; i++) {
						
			int posX=0, posY=0;
			
			double tiempo_inicio=System.nanoTime();
			
			boolean timeOut = false;
			
			do {
				
				if((System.nanoTime()-tiempo_inicio)/1000000 > 10000) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "Tiempo de espera agotado", "Time Out", JOptionPane.ERROR_MESSAGE);
					System.out.println("[log] ERROR: Time out creatinc auto cell");
					timeOut=true;
					break;
				}
				
				posX=(int)(Math.random()*(sizeX-1));
				posY=(int)(Math.random()*(sizeY-1));
								
			}while(mapa[posY][posX].isLive() || ((posX==0 && posY==0) || (posX==sizeX-1 && posY==sizeY-1)));
			
			if(timeOut) break;
			
			cell cell = mapa[posY][posX];
			
			cell.live();
									
			final int x = posX;
			final int y = posY;
			
			pool.execute(new Runnable() {
				
				public void run() {
					
					while(cell.getNeighborsLive().length<COLONIES_AUTO) {
						
						int[][] posNeigbors = getPosNeighbors(cell, sizeX, sizeY, getNumNeighbors(x, y, sizeX, sizeY));
						
						int idPosNeigbor;
						
						do {
							
							idPosNeigbor = (int) (Math.random() * posNeigbors.length);
							
						}while(mapa[posNeigbors[idPosNeigbor][1]][posNeigbors[idPosNeigbor][0]].isLive());
			
						mapa[posNeigbors[idPosNeigbor][1]][posNeigbors[idPosNeigbor][0]].live();
						
					}
					
				}
				
			});
			
		}
		
		pool.shutdown();
						
		while(!pool.isTerminated());
		
	}
	
	public void setVelocity(long vel) {
		this.vel = vel;
	}
	
	public long getVelocity() {
		return vel;
	}

	
	public void play() {
		
		play(0);
		
	}
	
	public void play(int generacion) {
		
		/*for(cell[] cells : mapa.getMap())
			for(cell c : cells)
				if(c.isLive()) //TODO*/
		
		count_celulesManual = 0;
		
		execCell(c -> {
			if(c.isLive()) count_celulesManual++;
			return null;
		});
		
		if(count_celulesManual<MIN_CELULES_MANUAL) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "Debes indicar un mínimo de " + MIN_CELULES_MANUAL + " celulas vivas.\nCelulas vivas: " + count_celulesManual, "Atenció", JOptionPane.WARNING_MESSAGE);
			pause();
		}else {
			playing=true;
			
			new Thread(new Runnable() {
				
				public void run() {
					
					generation=generacion;
					
					while(playing) {
						
						executing = true;
												
						generation++;
						
						System.out.println("GENERACION: " + generation);
						
						ExecutorService pool = executeThreads();
						
						while(!pool.isTerminated());
						
						try {
							Thread.sleep(vel);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						execCell(c -> {
							c.exec();
							return null;
						});
					}
					
					executing = false;
										
				}
				
			}).start();
		}		
		
	}
	
	private void execCell(Function<cell, ?> funcion) {
		
		ExecutorService pool = Executors.newCachedThreadPool();
		
		for(cell[] cells : mapa.getMap())
			for(cell cell : cells)
				pool.execute(new Runnable() {
					
					public void run() {
						funcion.apply(cell);
						//cell.exec();
					}
				});
		
		pool.shutdown();
		
	}
	
	public void pause() {
		
		playing = false;
		
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public void reboot() {
		
		generation = 0;
		
		pause();
		
		while(executing) try{Thread.sleep(10);}catch(InterruptedException e){e.printStackTrace();}
		
		mapa.clear();
		
		if(random) random(initialGrups, mapa.getMap());
	}
	
	public ExecutorService executeThreads() {
		
		ExecutorService pool = Executors.newCachedThreadPool();
		
		for(cell[] cells : mapa.getMap())
			for(cell cell : cells)
				pool.execute(cell);
		
		pool.shutdown();
		
		return pool;
		
	}
	
}


