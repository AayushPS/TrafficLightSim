package ApplicationEVENTS;
	
import ApplicationEVENTS.Menu.menu;
import ApplicationEVENTS.Menu.impl.MainMenu;
	
	public class Main {
		public static final String EXIT_COMMAND = "exit";
	
		public static void main(String[] args) {
			menu mainMenu = new MainMenu();
			mainMenu.start();
		}
	
	}
