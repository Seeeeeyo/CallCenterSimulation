package Simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Machine in a factory
 */
public class Machine implements CProcess, ProductAcceptor {
    /** Eventlist that will manage events */
    private final CEventList eventlist;
    /** Machine name */
    private final String name;
    /** Product that is being handled */
    private ArrayList<Product> product = new ArrayList<>();
    /** Queue from which the machine has to take products */
    final Queue queue;
    /** List that contains Employees for shift 1 (CSA corporate or consumers) */
    private ArrayList<Employee> employeeListShift1;
    /** List that contains Employees for shift 2 (CSA corporate or consumers) */
    private ArrayList<Employee> employeeListShift2;
    /** List that contains Employees for shift 3 (CSA corporate or consumers) */
    private ArrayList<Employee> employeeListShift3;

    /** List of the employees of the current shift. It is changed at every shift using the internal clock and the schedules */
    private ArrayList<Employee>  employeeList;

    /** Sink to dump products */
    private final ProductAcceptor sink;
    /** Status of the machine (b=busy, i=idle) */
    private String machineStatus;
    /** Mean processing time */
    private final double meanProcTime;
    /** Processing times (in case pre-specified) */
    public double[] processingTimes;
    /** Processing time iterator */
    public int procCnt;
    /** int used to keep track of the shift number the simulation is currently in */
    private static int shiftNum;
    /** counters to keep track of how many products have been processed (in total) */
    private static int counterStarted = 0;
    private static int counterFinished = 0;
    private int employeeLeft;

    private double globalProcessingTimes;

    /** counters to keep track of how many products have been processed in each shift */
    private int numberConsumerExecutedShift1;
    private int numberCorporateExecutedShift1;
    private int numberConsumerExecutedShift2;
    private int numberCorporateExecutedShift2;
    private int numberConsumerExecutedShift3;
    private int numberCorporateExecutedShift3;


    /**
     * Constructor
     * Service times are pre-specified
     *
     * @param q  Queue from which the machine has to take products
     * @param s  Where to send the completed products
     * @param e  Eventlist that will manage events
     * @param n  The name of the machine
     * @param st service times
     * @param listShift1 list of employees for shift 1. See the shift rules in pdf.
     * @param listShift2 list of employees for shift 2. See the shift rules in pdf.
     * @param listShift3 list of employees for shift 3. See the shift rules in pdf.
     */
    public Machine(Queue q, ProductAcceptor s, CEventList e, String n, double[] st, ArrayList<Employee> listShift1, ArrayList<Employee> listShift2, ArrayList<Employee> listShift3) {
        machineStatus = "i";
        queue = q;
        sink = s;
        eventlist = e;
        name = n;
        shiftNum = 1;
        meanProcTime = -1;
        processingTimes = st;
        globalProcessingTimes += st.length;
        employeeListShift1 = listShift1;
        employeeListShift2 = listShift2;
        employeeListShift3 = listShift3;
        employeeList = employeeListShift1;
        procCnt = 0;
        //queue.askProduct(this);
        for(int i = 0; i < employeeList.size(); i++)
            queue.askProduct(this);
    }

    /**
     * Method to check whether all the employees are busy or not.
     *
     * @return i if one or more employee are free. Returns b if all the employees are busy at the current time.
     */
    public String getMachineStatus() {
        int count = 0;
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getStatus().equals("b"))
                count++;
        }

        if (count == employeeList.size())
            machineStatus = "b";
        else
            machineStatus = "i";

        return machineStatus;
    }

    /**
     * Method to have this object execute an event
     *
     * @param type The type of the event that has to be executed
     * @param tme  The current time
     */
    public void execute(int type, double tme) {
        if (product.get(0).getEvents().get(0).equals("Creation type 0"))
            System.out.println("\tProduct = \u001b[36;1mConsumer\u001b[0m (0)");
        else
            System.out.println("\tProduct = \u001b[33;1mCorporate\u001b[0m (1)");

        // show arrival
        System.out.println("\tEvent type = \u001b[31;1mFinished\u001b[0m");
        System.out.println("\tEvent time = " + tme);
        // Remove product from system
        product.get(0).stamp(tme, "Production complete", name);
        counterFinished++;
        System.out.println("\tNumber of product finished = " + counterFinished);

        // send the product to the correct sink
        if (product.get(0).getEvents().get(0).equals("Creation type 0")) {
            Simulation.si_consumer.giveProduct(product.get(0));
        } else if (product.get(0).getEvents().get(0).equals("Creation type 1")){
            Simulation.si_corporate.giveProduct(product.get(0));
        }

        product.remove(0);
        boolean check = true;
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getStatus().equals("b") && check) {
                employeeList.get(i).setStatus("i");
                if(employeeLeft > 0) {
                    employeeList.remove(i);
                }
                check = false;
            }
        }
        int numclient = (int) (Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count() + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count() + queue.getRow().size());
        Simulation.numberClientInSystem.add(numclient);
        System.out.println("\tNumer of clients in the system = " + numclient);
        // Ask the queue for products
        if(employeeLeft > 0) {
            employeeLeft--;
            System.out.println("\tRemove employee, employee left to remove = " + employeeLeft);
        } else {
            queue.askProduct(this);
        }
    }

    /**
     * Let the machine accept a product and let it start handling it
     *
     * @param p The product that is offered
     * @return true if the product is accepted and started, false in all other cases
     */
    @Override
    public boolean giveProduct(Product p) {
        // Only accept something if the machine is idle
        System.out.println("\t" + this.name + " status = " + getMachineStatus());

        if (getMachineStatus().equals("i")) {
            p.stamp(eventlist.getTime(), "Production started", name);
            // accept the product
            product.add(p);
            // mark starting time

            System.out.println("\tProduction started");
            System.out.println("\tStart time = " + eventlist.getTime());

            System.out.println("\tNumber of product started = " + counterStarted);
            counterStarted++;

            if ((shiftNum==1) && (this.name.equals("Machine Consumer"))) {
                numberConsumerExecutedShift1++;
            }
            if ((shiftNum==1) && (this.name.equals("Machine Corporate"))) {
                numberCorporateExecutedShift1++;
            }
            if ((shiftNum==2) && (this.name.equals("Machine Consumer"))) {
                numberConsumerExecutedShift2++;
            }
            if ((shiftNum==2) && (this.name.equals("Machine Corporate"))) {
                numberCorporateExecutedShift2++;
            }
            if ((shiftNum==3) && (this.name.equals("Machine Consumer"))) {
                numberConsumerExecutedShift3++;
            }
            if ((shiftNum==3) && (this.name.equals("Machine Corporate"))) {
                numberCorporateExecutedShift3++;
            }

            // start production
            startProduction();

            int numclient = (int) (Simulation.getM_consumer().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count() + Simulation.getM_corporate().getEmployeeList().stream().filter(s -> s.getStatus().equals("b")).count() + queue.getRow().size());
            Simulation.numberClientInSystem.add(numclient);
            System.out.println("\tNumer of clients in the system = " + numclient);

            // Flag that the product has arrived
            return true;
        }
        // Flag that the product has been rejected
        return false;
    }

    /**
     * Method to get the first available employee to accept a product. It first checks if the machine is busy or not
     *
     * @return the index of the first employee who is available. If none of them is available, returns -1
     */
    private int getFirstFreeEmp() {
        if (getMachineStatus().equals("i")) {
            for (int i = 0; i < employeeList.size(); i++) {
                if (employeeList.get(i).getStatus().equals("i"))
                    return i;
            }
        }
        return -1;
    }

    /**
     * Starting routine for the production
     * Start the handling of the current product with an exponentionally distributed processingtime with average 30
     * This time is placed in the eventlist
     */
    private int startProduction() {
        int index = -1;

            if (processingTimes.length > procCnt) {
                if(product.get(product.size()-1).getEvents().get(0).equals("Creation type 1")) {
                    eventlist.add(this, 0, eventlist.getTime() + processingTimes[procCnt]); //target,type,time
                    Simulation.m_corporate.procCnt++;
                } else {
                    if(Simulation.m_consumer.processingTimes.length > Simulation.m_consumer.procCnt) {
                        eventlist.add(this, 0, eventlist.getTime() + Simulation.m_consumer.processingTimes[Simulation.m_consumer.procCnt]); //target,type,time
                        Simulation.m_consumer.procCnt++;
                    }
                }
                // set specific employee to busy
                index = getFirstFreeEmp();
                employeeList.get(getFirstFreeEmp()).setStatus("b");
            }
        return index;
    }

    /**
     * method to change the current list of employees based on the current shift. See the shift in the pdf or simulation class.
     * @param shiftNumber is the shiftnumber we're in based on the internal clock. shiftNumber =  2 or 3 (could be one but not logic in our context, still have the method for it tho)
     */
    public void setEmployeeList(int shiftNumber) {
        int x = 0;
        if (this.name.equals("Machine Corporate")) {
            x = 1;
        }

        System.out.println("/!\\ Shift changes for " + this.name + " /!\\");

        System.out.println("Before Queue request size = " + queue.requests.get(x).size());
        System.out.println("Before Employee = idle: " + employeeList.stream().filter(e -> e.getStatus().equals("i")).count() + ", busy: " + employeeList.stream().filter(e -> e.getStatus().equals("b")).count());

        int queueSize = queue.requests.get(x).size();
        for (int i = 0; i < queueSize; i++)
            queue.requests.get(x).remove(0);

        employeeList.removeAll(employeeList.stream()
                .filter(e -> e.getStatus().equals("i"))
                .collect(Collectors.toList())
        );

        employeeLeft = employeeList.size();

        System.out.println("Employee left = " + employeeLeft);

        if (shiftNumber == 1){
            employeeList.addAll(employeeListShift1);

            for(int i = 0; i < employeeListShift1.size(); i++) {
                queue.askProduct(this);
            }
            shiftNum = 1;
        }
        else if (shiftNumber == 2){
            employeeList.addAll(employeeListShift2);

            for(int i = 0; i < employeeListShift2.size(); i++) {
                queue.askProduct(this);
            }
            shiftNum = 2;
        }
        else if (shiftNumber == 3){
            employeeList.addAll(employeeListShift3);

            for(int i = 0; i < employeeListShift3.size(); i++) {
                queue.askProduct(this);
            }
            shiftNum = 3;
        }

        System.out.println("After Queue request size = " + queue.requests.get(x).size());
        System.out.println("After Employee = idle: " + employeeList.stream().filter(e -> e.getStatus().equals("i")).count() + ", busy: " + employeeList.stream().filter(e -> e.getStatus().equals("b")).count());

        System.out.println();
    }

    public ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }

    public int getNumberOfBusyEmployee(){
        int counter = 0;
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getStatus().equals("b"))
                counter ++;
        }
        return counter;
    }

    public String getName() {
        return name;
    }

    public static int getShiftNum() {
        return shiftNum;
    }

    public int getNumberConsumerExecutedShift1() {
        return numberConsumerExecutedShift1;
    }

    public int getNumberCorporateExecutedShift1() {
        return numberCorporateExecutedShift1;
    }

    public int getNumberConsumerExecutedShift2() {
        return numberConsumerExecutedShift2;
    }

    public int getNumberCorporateExecutedShift2() {
        return numberCorporateExecutedShift2;
    }

    public int getNumberConsumerExecutedShift3() {
        return numberConsumerExecutedShift3;
    }

    public int getNumberCorporateExecutedShift3() {
        return numberCorporateExecutedShift3;
    }

    @Override
    public String toString() {
        return "Machine{" +
                ", name='" + name + '\'' +
                ", machineStatus='" + machineStatus + '\'' +
                ", meanProcTime=" + meanProcTime +
                ", processingTimes=" + Arrays.toString(processingTimes) +
                ", procCnt=" + procCnt +
                '}';
    }
}