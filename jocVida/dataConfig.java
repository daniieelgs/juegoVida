package jocVida;

import java.io.Serializable;

public class dataConfig implements Serializable{

	private static final long serialVersionUID = 1L;

	private int ruleLive, ruleDie, mode, initialGroup, vel, sizeX, sizeY;
	
	public static final int MODE_MANUAL = 0;
	public static final int MODE_AUTOMATIC = 1;
	
	public static final String[] MODES = {"Manual", "Automático"};
	
	public dataConfig() {
		
		this(23, 3, MODE_MANUAL, 5, 100, 10, 10);
		
	}
	
	public dataConfig(int ruleLive, int ruleDie, int mode, int initialGroup, int vel, int sizeX, int sizeY) {
		
		this.ruleLive = ruleLive;
		this.ruleDie = ruleDie;
		this.mode = mode;
		this.initialGroup = initialGroup;
		this.vel = vel;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
	}

	public int getRuleLive() {
		return ruleLive;
	}

	public void setRuleLive(int ruleLive) {
		this.ruleLive = ruleLive;
	}

	public int getRuleDie() {
		return ruleDie;
	}

	public void setRuleDie(int ruleDie) {
		this.ruleDie = ruleDie;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getInitialGroup() {
		return initialGroup;
	}

	public void setInitialGroup(int initalGroup) {
		this.initialGroup = initalGroup;
	}

	public int getVel() {
		return vel;
	}

	public void setVel(int vel) {
		this.vel = vel;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	
	
}
