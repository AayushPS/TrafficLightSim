package ApplicationEVENTS.Menu.impl;

import ApplicationEVENTS.ApplicationContext;
import ApplicationEVENTS.Menu.menu;
import ApplicationEVENTS.setup.DisplaySimulator;
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
        Scanner scanner = new Scanner(System.in);

        // Example manual control: user controls the signal for each crossing
        while (true) {
            System.out.print("\nEnter crossing number to control (1 to " + context.getCrossingint() + "): ");
            int crossing = scanner.nextInt();
            if (crossing < 1 || crossing > context.getCrossingint()) {
                System.out.println("Invalid crossing number.");
                continue;
            }

            System.out.print("Enter green light time for crossing " + crossing + " in seconds: ");
            int greenTime = scanner.nextInt();
            System.out.print("Enter yellow light time for crossing " + crossing + " in seconds: ");
            int yellowTime = scanner.nextInt();

            // Simulate sending signal to the display for the current crossing
            System.out.println("\n--- Manual Control ---");
            System.out.printf("Manually controlling Crossing %d%n", crossing);
            System.out.printf("[GREEN] Crossing %d: %.1f seconds%n", crossing, greenTime);
            // Show the signal on the display simulator
            DisplaySimulator.displayTime(crossing, greenTime);
            sleepSeconds(greenTime);

            System.out.println("[YELLOW] Crossing " + crossing + " switching to Yellow.");
            DisplaySimulator.displayTime(crossing, yellowTime);
            sleepSeconds(yellowTime);
            System.out.println("-----------------------");
        }
    }

    private void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void printMenuHeader() {
        System.out.println("\nSystem Started in Manual Mode");
        System.out.println("Type 'stop' in Main Menu to exit Manual Mode.");
    }
}
