package Simulation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Queue that stores products until they can be handled by a machine
 */
public class Queue implements ProductAcceptor {
    /** List in which the products are kept */
    private final ArrayList<Product> row;
    /** Requests from machine that will be handling the products */
    public final ArrayList<ArrayList<Machine>> requests;
    /** The num */
    private static int k;


    /**
     * Initializes the queue and introduces a dummy machine
     * the machine has to be specified later
     */
    public Queue() {
        row = new ArrayList<>();
        requests = new ArrayList<>();
        requests.add(new ArrayList<>());
        requests.add(new ArrayList<>());
    }

    /**
     * Asks a queue to give a product to a machine
     * True is returned if a product could be delivered; false if the request is queued
     */
    public boolean askProduct(Machine machine) {
        List<Product> machineRow;
        if(machine.getName().equals("Machine Consumer")) {
            machineRow = row.stream().filter(p -> p.getEvents().get(0).equals("Creation type 0")).collect(Collectors.toList());
        } else {
            machineRow = row.stream().filter(p -> p.getEvents().get(0).equals("Creation type 1")).collect(Collectors.toList());
        }

        // This is only possible with a non-empty queue
        if (machineRow.size() > 0) {
            //System.out.println("Queue --> Machine");
            //System.out.println("\tQueue (0: " + row.stream().filter(p -> p.getEvents().get(0).equals("Creation type 0")).count() + ", 1: " + row.stream().filter(p -> p.getEvents().get(0).equals("Creation type 1")).count() + ") --> Machine");
            //System.out.println("\tMachine queue = " + machineRow.size());

            // If the machine accepts the product
            if (machine.giveProduct(machineRow.get(0))) {
                row.remove(machineRow.get(0));
                machineRow.remove(0);// Remove it from the queue
                //System.out.println("Queue (0: " + row.stream().filter(p -> p.getEvents().get(0).equals("Creation type 0")).count() + ", 1: " + row.stream().filter(p -> p.getEvents().get(0).equals("Creation type 1")).count() + ")");
                //System.out.println("\tMachine queue = " + machineRow.size());
                return true;
            } else
                return false; // Machine rejected; don't queue request
        } else {
            if(machine.getName().equals("Machine Consumer"))
                requests.get(0).add(machine);
            else
                requests.get(1).add(machine);
            return false; // queue request
        }
    }

    /**
     * Offer a product to the queue
     * It is investigated whether a machine wants the product, otherwise it is stored
     */
    public boolean giveProduct(Product p) {
        if(p.getEvents().get(0).equals("Creation type 0")){
            if (requests.get(0).size() < 1) {
                if (requests.get(1).size() < getK()) {
                    row.add(p); // Otherwise store it
                    //System.out.println("\tAdd to the Queue --> Queue size = " + row.size() + " (0: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 0")).count() + ", 1: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 1")).count() + ")");
                } else {
                    //System.out.println("\tSend to the machine (mix)");
                    boolean delivered = false;

                    while (!delivered & (requests.get(1).size() > getK())) {
                        delivered = requests.get(1).get(0).giveProduct(p);
                        requests.get(1).remove(0);
                    }

                    if (!delivered) {
                        row.add(p);
                        //System.out.println("\t--> didn't deliver --> add to the Queue --> Queue size = " + row.size() + " (0: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 0")).count() + ", 1: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 1")).count() + ")");
                    }
                }
            } else {
                //System.out.println("\tSend to the machine");
                boolean delivered = false;

                while (!delivered & (requests.get(0).size() > 0)) {
                    delivered = requests.get(0).get(0).giveProduct(p);
                    requests.get(0).remove(0);
                }

                if (!delivered) {
                    row.add(p);
                    //System.out.println("\t--> didn't deliver --> add to the Queue --> Queue size = " + row.size() + " (0: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 0")).count() + ", 1: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 1")).count() + ")");
                }
            }
            return true;
        } else if(p.getEvents().get(0).equals("Creation type 1")){
            if (requests.get(1).size() < 1) {
                row.add(p); // Otherwise store it
                //System.out.println("\tAdd to the Queue --> Queue size = " + row.size() + " (0: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 0")).count() + ", 1: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 1")).count() + ")");
            } else {
                //System.out.println("\tSend to the machine");
                boolean delivered = false;

                while (!delivered & (requests.get(1).size() > 0)) {
                    delivered = requests.get(1).get(0).giveProduct(p);
                    requests.get(1).remove(0);
                }

                if (!delivered) {
                    row.add(p);
                    //System.out.println("\t--> didn't deliver --> add to the Queue --> Queue size = " + row.size() + " (0: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 0")).count() + ", 1: " + row.stream().filter(pr -> pr.getEvents().get(0).equals("Creation type 1")).count() + ")");
                }
            }
            return true;
        }
        return true;
    }

    public void setK(int k) {
        Queue.k = k;
    }

    public int getK() {
        return k;
    }

    public ArrayList<Product> getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "Queue{" +
                "row size=" + row.size() +
                ", requests size=" + requests.size() +
                '}';
    }
}