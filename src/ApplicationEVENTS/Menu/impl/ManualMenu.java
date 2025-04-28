package ApplicationEVENTS.Menu.impl;

import ApplicationEVENTS.ApplicationContext;
import ApplicationEVENTS.Menu.menu;
import ApplicationEVENTS.setup.DisplayManager;
import ApplicationEVENTS.setup.TrafficSignalTimer;

import java.util.Map;

public class ManualMenu implements menu {

    private ApplicationContext context;

    public ManualMenu() {
        context = ApplicationContext.getInstance();
    }

    @Override
    public void start() {
        printMenuHeader();

        int crossingCount = context.getCrossingint();
        int totalCycleTime = 60;  // Example: total cycle time for all crossings
        int currentCrossing = 1;

        while (true) {
            // For each crossing, calculate green time and countdown
            System.out.println("==== Manual Mode ====");
            for (int i = 1; i <= crossingCount; i++) {
                int timeRemaining;
                if (i == currentCrossing) {
                    // Active crossing: show green time
                    int totalVehicles = 0;  // Update this to use actual vehicle data
                    double greenTime = TrafficSignalTimer.calculateGreenLightTime(Map.of("car", totalVehicles));
                    timeRemaining = (int) greenTime;
                    System.out.printf("Crossing %d: GREEN for %.1f seconds%n", i, greenTime);
                    DisplayManager.showCountdownForCrossing(i, timeRemaining, totalCycleTime, (int) greenTime);
                } else {
                    // Non-active crossings: show time remaining until green
                    int remainingTime = totalCycleTime - (i - 1); // Adjust based on the crossing order
                    System.out.printf("Crossing %d: RED (remaining time for turn to green: %d seconds)%n", i, remainingTime);
                    DisplayManager.showCountdownForCrossing(i, remainingTime, totalCycleTime, 0);
                }
            }

            // Simulate user input to move to the next crossing (or break to stop)
            currentCrossing = (currentCrossing % crossingCount) + 1;
            try { Thread.sleep(5000); } catch (InterruptedException e) { break; }
        }

        context.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println("System Started in Manual Mode");
        System.out.println("Switching between crossings...");
    }
}
