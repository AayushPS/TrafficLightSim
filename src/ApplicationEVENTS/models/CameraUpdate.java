package ApplicationEVENTS.models;

public class CameraUpdate {
    private int crossing;
    private int lane;
    private int count;

    // Default constructor for JSON de-serialization
    public CameraUpdate() {}

    public CameraUpdate(int crossing, int lane, int count) {
        this.crossing = crossing;
        this.lane = lane;
        this.count = count;
    }

    public int getCrossing() {
        return crossing;
    }

    public void setCrossing(int crossing) {
        this.crossing = crossing;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CameraUpdate{" +
               "crossing=" + crossing +
               ", lane=" + lane +
               ", count=" + count +
               '}';
    }
}
