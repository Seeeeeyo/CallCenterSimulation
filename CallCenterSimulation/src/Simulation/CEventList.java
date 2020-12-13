package Simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;

/**
 * Event processing mechanism
 * Events are created here and it is ensured that they are processed in the proper order
 * The simulation clock is located here.
 */
public class CEventList implements CProcess {
    /** List of events that have to be executed */
    private final ArrayList<CEvent> events;
    /** The time in the simulation */
    private double currentTime;
    /** Stop flag */
    private boolean stopFlag;

    private int stopMachine = 0;

    private boolean checkShift2 = true;
    private boolean checkShift3 = true;

    /**
     * Standard constructor
     * Create an CEventList object
     */
    public CEventList() {
        currentTime = 0;
        stopFlag = false;
        events = new ArrayList<>();
    }

    /**
     * Method for the construction of a new event.
     *
     * @param target The object that will process the event
     * @param type   A type indicator of the event for objects that can process multiple types of events.
     * @param tme    The time at which the event will be executed
     */
    public void add(CProcess target, int type, double tme) {
        boolean success = false;
        // First create a new event using the parameters
        CEvent evnt;
        evnt = new CEvent(target, type, tme);
        // Now it is examined where the event has to be inserted in the list
        for (int i = 0; i < events.size(); i++) {
            // The events are sorted chronologically
            if (events.get(i).getExecutionTime() > evnt.getExecutionTime()) {
                // If an event is found in the list that has to be executed after the current event
                success = true;
                // Then the new event is inserted before that element
                events.add(i, evnt);
                break;
            }
        }
        if (!success) {
            // Else the new event is appended to the list
            events.add(evnt);
        }
    }

    /**
     * Method for starting the eventlist.
     * It will run until there are no longer events in the list
     */
    public void start() {
        // stop criterion
        while (events.size() > 0) {
            if (currentTime >= 28800 && checkShift2) {
                Simulation.getM_consumer().setEmployeeList(2);
                Simulation.getM_corporate().setEmployeeList(2);
                checkShift2 = false;
            }
            else if (currentTime >= 57600 && checkShift3) {
                Simulation.getM_consumer().setEmployeeList(3);
                Simulation.getM_corporate().setEmployeeList(3);
                checkShift3 = false;
            }

            System.out.println("----------[ Start event ]----------");
            System.out.println("Employees consumer = Idle: " + Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count());
            System.out.println("Employees corporate = Idle: " + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count());
            System.out.println(Simulation.getM_corporate().queue.requests.get(0).size() + " " + Simulation.getM_corporate().queue.requests.get(1).size());

            // Make the simulation time equal to the execution time of the first event in the list that has to be processed
            currentTime = events.get(0).getExecutionTime();

            System.out.println("Current time = " + currentTime);
            System.out.println("Shift = " + Machine.getShiftNum());

            System.out.println(">>> Execution");
            // Let the element be processed
            events.get(0).execute();
            System.out.println(">>> End");

            // Remove the event from the list
            events.remove(0);

            System.out.println("Employees consumer = Idle: " + Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count());
            System.out.println("Employees corporate = Idle: " + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count());
            System.out.println("-----------[ End event ]-----------");
            System.out.println();
        }

        System.out.println("Stop flag = " + stopFlag);
    }

    /**
     * Method for starting the eventlist.
     * It will run until there are no longer events in the list or that a maximum time has elapsed
     */

    /*
    public void start(double mx) {

        add(this, -1, mx);

        // stop criterion
        while ((events.size() > 0) && (!stopFlag) && !(Simulation.getM_consumer().isStopMachine() && Simulation.getM_corporate().isStopMachine())) {

            if (currentTime >= 28800 && checkShift2) {
                Simulation.getM_consumer().setEmployeeList(2);
                Simulation.getM_corporate().setEmployeeList(2);
                checkShift2 = false;
            }
            else if (currentTime >= 57600 && checkShift3) {
                Simulation.getM_consumer().setEmployeeList(3);
                Simulation.getM_corporate().setEmployeeList(3);
                checkShift3 = false;
            }

            System.out.println("----------[ Start event ]----------");
            System.out.println("Employees consumer = Idle: " + Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count());
            System.out.println("Employees corporate = Idle: " + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count());
            System.out.println(Simulation.getM_corporate().queue.requests.get(0).size() + " " + Simulation.getM_corporate().queue.requests.get(1).size());

            // Make the simulation time equal to the execution time of the first event in the list that has to be processed
            currentTime = events.get(0).getExecutionTime();

            System.out.println("Current time = " + currentTime);
            System.out.println("Shift = " + Machine.getShiftNum());

            System.out.println(">>> Execution");
            // Let the element be processed
            events.get(0).execute();
            System.out.println(">>> End");

            // Remove the event from the list
            events.remove(0);

            System.out.println("Employees consumer = Idle: " + Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count());
            System.out.println("Employees corporate = Idle: " + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count());
            System.out.println("Stop flag corporate = " + Simulation.getM_corporate().isStopMachine());
            System.out.println("Stop flag consumer = " + Simulation.getM_consumer().isStopMachine());
            System.out.println("-----------[ End event ]-----------");
            System.out.println();
        }

        System.out.println("Stop flag = " + stopFlag);
        System.out.println("Stop flag corporate = " + Simulation.getM_corporate().isStopMachine());
        System.out.println("Stop flag consumer = " + Simulation.getM_consumer().isStopMachine());
    }
 */

    public void stop() {
        stopFlag = true;
    }


    /**
     * Method for accessing the simulation time.
     * The variable with the time is private to ensure that no other object can alter it.
     * This method makes it possible to read the time.
     *
     * @return The current time in the simulation
     */
    public double getTime() {
        return currentTime;
    }

    /**
     * Method to have this object process an event
     *
     * @param type The type of the event that has to be executed
     * @param tme  The current time
     */
    @Override
    public void execute(int type, double tme) {
        if (type == -1)
            stop();
    }

    @Override
    public String toString() {
        return "CEventList{" +
                "events=" + events +
                ", currentTime=" + currentTime +
                ", stopFlag=" + stopFlag +
                '}';
    }
}