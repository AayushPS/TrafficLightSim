package ApplicationEVENTS.Menu.impl;

import java.util.Scanner;

import ApplicationEVENTS.ApplicationContext;
import ApplicationEVENTS.ApplicationContext.Mode;
import ApplicationEVENTS.Main;
import ApplicationEVENTS.Menu.menu;

public class MainMenu implements menu{
	private ApplicationContext context;
	
	{
		context=ApplicationContext.getInstance();
	}
	
	@Override
	public void start() {
		if (context.getMainMenu() == null) {
			context.setMainMenu(this);
		}
		
		menu menuToNavigate = null;
		while (true) {
			printMenuHeader();
			
			Scanner sc = new Scanner(System.in);

			System.out.println("Time Control Switch:\nEnter 0 for Auto Mode\nEnter 1 for Manual Mode\nEnter exit to Exit from application\n");
			String userInput = sc.next();
			
			
			
			if (userInput.equalsIgnoreCase(Main.EXIT_COMMAND)) {
				System.exit(0);
			} else {
				System.out.print("Enter no. of crossings in this context: ");
				int crossingint = sc.nextInt();
				System.out.print("Enter no. of lanes in each side in this context: ");
				int lanes = sc.nextInt();
				int commandNumber = Integer.parseInt(userInput);
				context.setCrossingint(crossingint);
				context.setLanes(lanes);
				if(commandNumber==0) {
					context.setMode(Mode.AUTO);
					menuToNavigate = new AutomaticMenu();
					break;
				}else if(commandNumber==1){
					context.setMode(Mode.MANUAL);
					menuToNavigate = new ManualMenu();
					break;
				}else {
					System.out.println("Enter Valid value");
				}
			}
			sc.close();
		}
		menuToNavigate.start();		
	}

	@Override
	public void printMenuHeader() {
		System.out.println("main.menu.header");
		if (context.getMode() == Mode.MANUAL) {
			System.out.println("menu.for.manual.mode");
		} else {
			System.out.println("menu.for.auto.mode");
		}
	}

}
