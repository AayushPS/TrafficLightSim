package ApplicationEVENTS.setup;

import java.util.HashMap;
import java.util.Map;

public class TrafficSignalTimer {

    // Time additions per vehicle type in seconds
    static final Map<String, Double> timeAdditions = new HashMap<>();

    static {
        timeAdditions.put("truck", 4.0);
        timeAdditions.put("car", 2.5);
        timeAdditions.put("bike", 2.0);
        timeAdditions.put("auto", 2.5);
    }

    // Buffer times in seconds
    static final double RELAXATION_TIME = 5.0;
    static final double SWITCHING_BUFFER = 3.0;

    public static double calculateGreenLightTime(Map<String, Integer> vehicleCounts) {
        double totalTimeOfAType = 0.0;
        double totalTime=0.0;

        for (Map.Entry<String, Integer> entry : vehicleCounts.entrySet()) {
            String vehicleType = entry.getKey().toLowerCase();
            int count = entry.getValue();
            double timePerVehicle;

            if (timeAdditions.containsKey(vehicleType)) {
                timePerVehicle = timeAdditions.get(vehicleType);
            } else {
                // Log or handle unknown vehicle type here
                System.out.println("Unknown vehicle type detected: " + vehicleType + " → Treated as car.");
                timePerVehicle = timeAdditions.get("car");
            }

            totalTimeOfAType += timePerVehicle * count;
            totalTime=Math.max(totalTimeOfAType,totalTime);
            totalTimeOfAType=0.0;
    }

    // Add fixed buffer times
    totalTime += RELAXATION_TIME + SWITCHING_BUFFER;

    return totalTime;
}


    public static void main(String[] args) {
        Map<String, Integer> vehicleCounts = new HashMap<>();
        vehicleCounts.put("truck", 2);
        vehicleCounts.put("car", 4);
        vehicleCounts.put("bike", 5);
        vehicleCounts.put("scooter", 3); // unknown → treated as car

        double greenLightTime = calculateGreenLightTime(vehicleCounts);
        System.out.println("Calculated Green Light Time: " + greenLightTime + " seconds");
    }
}