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

    // Simulate displaying a time on the seven-segment display
    public static void displayTime(int hours, int minutes) {
        // Display hours (can be in two digits)
        String hourTens = String.valueOf(hours / 10);  // Tens place of hours
        String hourOnes = String.valueOf(hours % 10);  // Ones place of hours

        // Display minutes (two digits)
        String minuteTens = String.valueOf(minutes / 10);  // Tens place of minutes
        String minuteOnes = String.valueOf(minutes % 10);  // Ones place of minutes

        // Display each digit as its pattern
        System.out.println("Displaying time " + hours + ":" + minutes);

        System.out.println("Hour Tens: " + getSignalPatternForNumber(Integer.parseInt(hourTens)));
        System.out.println("Hour Ones: " + getSignalPatternForNumber(Integer.parseInt(hourOnes)));
        System.out.println("Minute Tens: " + getSignalPatternForNumber(Integer.parseInt(minuteTens)));
        System.out.println("Minute Ones: " + getSignalPatternForNumber(Integer.parseInt(minuteOnes)));
    }
}
