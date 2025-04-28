package ApplicationEVENTS.setup;

public class DisplayManager {

    // This method will be called from Manual or Auto mode
    public static void showCountdown(int secondsRemaining) {
        DisplaySimulator.displayCountdown(secondsRemaining);
    }
}
