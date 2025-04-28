package ApplicationEVENTS.setup;

import java.util.HashMap;
import java.util.Map;

public class DisplayManager {

    // Map of numbers to their corresponding 7-segment bit patterns
    private static final Map<Integer, String> sevenSegmentPatterns = new HashMap<>();
    
    static {
        // Initialize the segment patterns for 0-9
        sevenSegmentPatterns.put(0, "1111110");
        sevenSegmentPatterns.put(1, "0110000");
        sevenSegmentPatterns.put(2, "1101101");
        sevenSegmentPatterns.put(3, "1111001");
        sevenSegmentPatterns.put(4, "0110011");
        sevenSegmentPatterns.put(5, "1011011");
        sevenSegmentPatterns.put(6, "1011111");
        sevenSegmentPatterns.put(7, "1110000");
        sevenSegmentPatterns.put(8, "1111111");
        sevenSegmentPatterns.put(9, "1111011");
    }

    // Get the signal pattern for a given number
    private static String getSignalPatternForNumber(int number) {
        return sevenSegmentPatterns.getOrDefault(number, "Invalid number");
    }

    // Simulate displaying countdown for each crossing
    public static void showCountdownForCrossing(int crossingNumber, int countdownTime, int totalCycleTime, int greenTime) {
        int timeRemaining = (countdownTime == 0) ? totalCycleTime - greenTime : countdownTime;

        // Display the countdown for the current crossing
        System.out.println("Displaying countdown for Crossing " + crossingNumber + ": " + timeRemaining + " seconds");

        // Display the countdown on seven-segment display
        int tens = timeRemaining / 10;
        int ones = timeRemaining % 10;

        System.out.println("Crossing " + crossingNumber + " - Tens: " + getSignalPatternForNumber(tens));
        System.out.println("Crossing " + crossingNumber + " - Ones: " + getSignalPatternForNumber(ones));
    }
}
