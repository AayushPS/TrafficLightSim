package ApplicationEVENTS.Menu.impl;

import ApplicationEVENTS.ApplicationContext;
import ApplicationEVENTS.Menu.menu;
import ApplicationEVENTS.setup.DisplaySimulator;
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

            int totalVehicles = perCrossing.get(current);
            double greenTime = TrafficSignalTimer.calculateGreenLightTime(
                    Map.of("car", totalVehicles)
            );

            // Clearer output with format
            System.out.println("\n---- Automatic Mode Cycle ----");
            System.out.println("Current Crossing: Crossing " + current);
            for (int i = 1; i <= crossingCount; i++) {
                if (i == current) {
                    System.out.printf("[GREEN] Crossing %d: %.1f seconds (vehicles=%d)%n", i, greenTime, totalVehicles);
                    // Show the signal on the display simulator
                    DisplaySimulator.displayTime(current, (int) greenTime);
                } else {
                    System.out.printf("[RED] Crossing %d: No green light.%n", i);
                }
            }
            System.out.println("-------------------------------");
            sleepSeconds((int) Math.round(greenTime));

            System.out.println("[YELLOW] Crossing " + current + " switching to Yellow.");
            // Show the signal on the display simulator
            DisplaySimulator.displayTime(current, 5);
            sleepSeconds(5);

            current = current % crossingCount + 1;
        }

        // Shutdown
        System.out.println("\nAutomatic Mode stopped.");
        pool.shutdownNow();
        context.getMainMenu().start();
    }

    private void handleClient(Socket sock) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()))) {
            String line = in.readLine();
            int[] data = parseCameraInput(line);
            int crossing = data[0], lane = data[1], count = data[2];
            if (crossing > 0 && lane > 0) {
                latestCounts.put(crossing + "-" + lane, count);
                System.out.printf("[Received] Crossing %d, Lane %d: Vehicle Count = %d%n",
                        crossing, lane, count);
            }
        } catch (IOException ignored) {
        } finally {
            try { sock.close(); } catch (IOException ignored) {}
        }
    }

    /**
     * Parses a camera message in format "crossing:1,lane:2,count:5" into {crossing, lane, count}.
     */
    private int[] parseCameraInput(String message) {
        int[] result = new int[3];
        if (message == null) return result;
        String[] pairs = message.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":");
            if (kv.length != 2) continue;
            String key = kv[0].trim().toLowerCase();
            int val;
            try { val = Integer.parseInt(kv[1].trim()); }
            catch (NumberFormatException e) { continue; }
            switch (key) {
                case "crossing": result[0] = val; break;
                case "lane":     result[1] = val; break;
                case "count":    result[2] = val; break;
                default: break;
            }
        }
        return result;
    }

    private void sleepSeconds(int seconds) {
        for (int i = 0; i < seconds && running.get(); i++) {
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    @Override
    public void printMenuHeader() {
        System.out.println("System Started in Auto Mode");
        System.out.println("Listening for camera updates on port " + PORT);
        System.out.println("Type 'stop' in Main Menu to exit Auto Mode.");
    }
}
