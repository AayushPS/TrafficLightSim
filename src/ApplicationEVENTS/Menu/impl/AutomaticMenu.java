package ApplicationEVENTS.Menu.impl;

import ApplicationEVENTS.ApplicationContext;
import ApplicationEVENTS.Menu.menu;
import ApplicationEVENTS.setup.DisplayManager;
import ApplicationEVENTS.setup.TrafficSignalTimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class AutomaticMenu implements menu {

    private ApplicationContext context;
    private static final int PORT = 7777;
    private final AtomicBoolean running = new AtomicBoolean(true);
    // key = "crossing-lane", value = count
    private final ConcurrentMap<String, Integer> latestCounts = new ConcurrentHashMap<>();

    public AutomaticMenu() {
        context = ApplicationContext.getInstance();
    }

    @Override
    public void start() {
        printMenuHeader();

        int crossingCount = context.getCrossingint();
        int totalCycleTime = 60;  // Example: total cycle time for all crossings
        ExecutorService pool = Executors.newCachedThreadPool();

        // Listener thread: accepts incoming connections
        Thread listenerThread = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(PORT)) {
                server.setSoTimeout(1000);
                while (running.get()) {
                    try {
                        Socket client = server.accept();
                        pool.submit(() -> handleClient(client));
                    } catch (IOException e) {
                        // Timeout or accept error: check running flag again
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to start server socket: " + e.getMessage());
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();

        // Thread to listen for 'stop' typed on the console
        Thread stopListener = new Thread(() -> {
            Scanner console = new Scanner(System.in);
            while (running.get()) {
                String line = console.nextLine().trim();
                if (line.equalsIgnoreCase("stop")) {
                    running.set(false);
                    System.out.println("\nAutomatic Mode interrupted by user.");
                }
            }
        });
        stopListener.setDaemon(true);
        stopListener.start();

        // Main control loop
        int current = 1;
        while (running.get()) {
            // Aggregate lane counts to per-crossing totals
            Map<Integer, Integer> perCrossing = latestCounts.entrySet().stream()
                    .collect(Collectors.groupingBy(
                            entry -> Integer.parseInt(entry.getKey().split("-")[0]),
                            Collectors.summingInt(Map.Entry::getValue)
                    ));

            // Ensure crossings with no data are present
            for (int i = 1; i <= crossingCount; i++) {
                perCrossing.putIfAbsent(i, 0);
            }

            // Correct casting and calculation
            Integer totalVehicles = perCrossing.get(current);  // Ensure casting here
            if (totalVehicles == null) {
                totalVehicles = 0;  // Handle null if no data for the crossing
            }

            double greenTime = TrafficSignalTimer.calculateGreenLightTime(
                    Map.of("car", totalVehicles)
            );

            System.out.println("==== Traffic Cycle ====");
            for (int i = 1; i <= crossingCount; i++) {
                if (i == current) {
                    // Active crossing: show countdown for green
                    System.out.printf("Crossing %d: GREEN for %.1f seconds (vehicles=%d)%n",
                            i, greenTime, totalVehicles);
                    DisplayManager.showCountdownForCrossing(i, (int) greenTime, totalCycleTime, (int) greenTime);
                } else {
                    // Non-active crossings: show countdown until green (time remaining)
                    int remainingTime = totalCycleTime - (int) greenTime * (i - 1) % totalCycleTime;
                    System.out.printf("Crossing %d: RED (remaining time for turn to green: %d seconds)%n", i, remainingTime);
                    DisplayManager.showCountdownForCrossing(i, 0, totalCycleTime, (int) greenTime);
                }
            }

            // Simulate the next crossing
            current = (current % crossingCount) + 1;
            try { Thread.sleep(5000); } catch (InterruptedException e) { break; }
        }

        context.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println("System Started in Automatic Mode");
        System.out.println("Press 'stop' to interrupt the automatic mode.");
    }

    private void handleClient(Socket client) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String message;
            while ((message = reader.readLine()) != null) {
                // Assuming the message contains crossing-lane data in "crossing-lane: vehicle_count" format
                String[] parts = message.split(":");
                if (parts.length == 2) {
                    String crossingLane = parts[0].trim();
                    int vehicleCount = Integer.parseInt(parts[1].trim());
                    latestCounts.put(crossingLane, vehicleCount);
                    System.out.println("Received update for " + crossingLane + ": " + vehicleCount + " vehicles");
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }
}
