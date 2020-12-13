package Simulation;

import java.util.ArrayList;

/**
 * Product that is send trough the system
 */
class Product {
    /** Stamps for the products */
    private final ArrayList<Double> times;
    private final ArrayList<String> events;
    private final ArrayList<String> stations;

    /**
     * Constructor for the product
     * Mark the time at which it is created
     */
    public Product() {
        times = new ArrayList<>();
        events = new ArrayList<>();
        stations = new ArrayList<>();
    }

    public void stamp(double time, String event, String station) {
        times.add(time);
        events.add(event);
        stations.add(station);
    }

    public ArrayList<Double> getTimes() {
        return times;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public ArrayList<String> getStations() {
        return stations;
    }

    public double[] getTimesAsArray() {
        times.trimToSize();
        double[] tmp = new double[times.size()];
        for (int i = 0; i < times.size(); i++)
            tmp[i] = (times.get(i)).doubleValue();

        return tmp;
    }

    public String[] getEventsAsArray() {
        String[] tmp = new String[events.size()];
        tmp = events.toArray(tmp);
        return tmp;
    }

    public String[] getStationsAsArray() {
        String[] tmp = new String[stations.size()];
        tmp = stations.toArray(tmp);
        return tmp;
    }
}
