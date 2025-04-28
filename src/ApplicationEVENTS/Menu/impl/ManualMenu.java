package ApplicationEVENTS.Menu.impl;

import ApplicationEVENTS.ApplicationContext;
import ApplicationEVENTS.Menu.menu;
import ApplicationEVENTS.setup.DisplayManager;
import ApplicationEVENTS.setup.TrafficSignalTimer;

import java.util.Scanner;

public class ManualMenu implements menu {
    private ApplicationContext context;

    public ManualMenu() {
        context = ApplicationContext.getInstance();
    }

    @Override
    public void start() {
        printMenuHeader();

        int crossingCount = context.getCrossingint();
        Scanner console = new Scanner(System.in);

        while (true) {
            System.out.println("Enter crossing number (1 to " + crossingCount + " or 0 to quit):");
            int crossing = console.nextInt();
            if (crossing == 0) break;

            if (crossing < 1 || crossing > crossingCount) {
                System.out.println("Invalid crossing number.");
                continue;
            }

            System.out.println("Enter green light duration (seconds):");
            int greenTime = console.nextInt();
            System.out.println("Enter yellow light duration (seconds):");
            int yellowTime = console.nextInt();

            // Simulate manual light control
            System.out.printf("Crossing %d: GREEN for %d seconds%n", crossing, greenTime);
            DisplayManager.showCountdown(greenTime);  // Show countdown
            sleepSeconds(greenTime);

            System.out.printf("Crossing %d: YELLOW for %d seconds%n", crossing, yellowTime);
            DisplayManager.showCountdown(yellowTime);  // Show countdown
            sleepSeconds(yellowTime);
        }

        System.out.println("Manual Mode Stopped.");
        context.getMainMenu().start();
    }

    private void sleepSeconds(int seconds) {
        for (int i = 0; i < seconds; i++) {
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    @Override
    public void printMenuHeader() {
        System.out.println("System Started in Manual Mode");
        System.out.println("Type '0' to exit Manual Mode.");
    }
}
