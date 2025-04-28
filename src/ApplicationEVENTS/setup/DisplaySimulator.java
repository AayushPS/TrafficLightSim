package ApplicationEVENTS.setup;

import java.util.HashMap;
import java.util.Map;

public class DisplaySimulator {

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

    // Simulate displaying countdown time remaining on the seven-segment display
    public static void displayCountdown(int secondsRemaining) {
        if (secondsRemaining < 0 || secondsRemaining > 99) {
            System.out.println("Invalid countdown value. Must be between 0 and 99 seconds.");
            return;
        }

        int tensPlace = secondsRemaining / 10;
        int onesPlace = secondsRemaining % 10;

        System.out.println("Displaying countdown: " + secondsRemaining + " seconds");

        System.out.println("Tens Digit:  " + getSignalPatternForNumber(tensPlace));
        System.out.println("Ones Digit:  " + getSignalPatternForNumber(onesPlace));
    }
}
