package ApplicationEVENTS;

import ApplicationEVENTS.Menu.menu;

public class ApplicationContext {
	private static ApplicationContext instance;
	private menu mainMenu; //current menu or instance working on
	private Mode mode = Mode.AUTO; //current mode of application , auto or manual
	private int  crossingint; //no of crossings like a 4 way or 3 way crossing
	private int lanes; //sometimes one lane has multiple cameras due to wide roads
	
	public enum Mode{
		AUTO,MANUAL;
	}
	
	private ApplicationContext() {
	}
	public static ApplicationContext getInstance() {
		if(instance==null) {
			instance = new ApplicationContext();
		}
		return instance;
	}
	
	public void setMainMenu(menu menu) {
		this.mainMenu = menu;
	}
	
	public menu getMainMenu() {
		return this.mainMenu;
	}
	
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode m) {
		this.mode = m;
	}
	public int getCrossingint() {
		return crossingint;
	}
	public void setCrossingint(int crossingint) {
		this.crossingint = crossingint;
	}
	public int getLanes() {
		return lanes;
	}
	public void setLanes(int lanes) {
		this.lanes = lanes;
	}
}
