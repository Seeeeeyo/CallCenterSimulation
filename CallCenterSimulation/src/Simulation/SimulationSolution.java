

package Simulation;

import java.util.ArrayList;

/**
 * This class is a copy of the Simulation.java without the main method and the sout
 * Was only used to run the bunch of tests to get the data for the data analysis
 */
public class SimulationSolution {

    /**
     * The 2 machines used in the simulation, a machine for the CSA consumers and another one for the CSA Corporates
     */
    public static Machine m_consumer;
    public static Machine m_corporate;

    /**
     * The 2 sink
     */
    public static Sink si_consumer;
    public static Sink si_corporate;

    /** The number of workers per shift */
    public static int numberCSAcorporateShift1;
    public static int numberCSAconsumerShift1;
    public static int numberCSAcorporateShift2;
    public static int numberCSAconsumerShift2;
    public static int numberCSAcorporateShift3;
    public static int numberCSAconsumerShift3;

    /** k, the amount of idle CSA corporates required in order to be allowed to help the CSA consumers*/
    public static int k;

    /**
     * The cost for each shift (35€/hour for a consumer and 60€/hour for a corporate)
     */
    public static int costShift1;
    public static int costShift2;
    public static int costShift3;
    /**
     * total cost (for one day)
     */
    public static int totalCostDay;

    /**
     * the number of calls per shift
     */
    private static int numberCorporateCallsShift1;
    private static int numberCorporateCallsShift2;
    private static int numberCorporateCallsShift3;
    private static int numberConsumerCallsShift1;
    private static int numberConsumerCallsShift2;
    private static int numberConsumerCallsShift3;

    /**
     * the number of calls per shift
     */
    private static int numberCallsShift1;
    private static int numberCallsShift2;
    private static int numberCallsShift3;
    private static int totalNumberConsumerCalls;
    private static int totalNumberCustomerCalls;

    /**
     * the average number of clients in the system
     */
    public static int avgNumberClientInSystem;

    /**
     * Arraylist storing the number of clients in the system at different times of the simulation
     */
    public static ArrayList<Integer> numberClientInSystem;

    /**
     * matrices storing the times of arrival etc
     */
    private static double[][] dataConsumer;
    private static double[][] dataCorporate;

    /**
     * The  amount of consumers' calls for one day
     */
    public static int numberCallsConsumer;

    /**
     * The  amount of corporates' calls for one day
     */
    public static int numberCallsCorporate;

    /**
     * Boolean used for the display in the CSV file
     */
    private boolean check;

    /**
     * Constructor of the object simulation
     * @param numberCSAcorporateShift1
     * @param numberCSAcorporateShift2
     * @param numberCSAcorporateShift3
     * @param numberCSAconsumerShift1
     * @param numberCSAconsumerShift2
     * @param numberCSAconsumerShift3
     * @param k
     * @param check
     */
    public SimulationSolution(int numberCSAcorporateShift1, int numberCSAcorporateShift2, int numberCSAcorporateShift3, int numberCSAconsumerShift1, int numberCSAconsumerShift2, int numberCSAconsumerShift3, int k, boolean check) {
        Simulation.numberCSAcorporateShift1 = numberCSAcorporateShift1;
        Simulation.numberCSAcorporateShift2 = numberCSAcorporateShift2;
        Simulation.numberCSAcorporateShift3 = numberCSAcorporateShift3;
        Simulation.numberCSAconsumerShift1 = numberCSAconsumerShift1;
        Simulation.numberCSAconsumerShift2 = numberCSAconsumerShift2;
        Simulation.numberCSAconsumerShift3 = numberCSAconsumerShift3;
        Simulation.k = k;
        this.check = check;
        costShift1 = 8 * (numberCSAconsumerShift1 * 35 + numberCSAcorporateShift1 * 60);
        costShift2 = 8 * (numberCSAconsumerShift2 * 35 + numberCSAcorporateShift2 * 60);
        costShift3 = 8 * (numberCSAconsumerShift3 * 35 + numberCSAcorporateShift3 * 60);
        totalCostDay = costShift1 + costShift2 + costShift3;
        numberClientInSystem = new ArrayList<>();
    }


    /**
     * This method is used to give the number of CSA Corporate for a certain shift
     *
     * @param shift number: shift 1 = 6am – 2pm, shift = 2pm – 10pm, shift3 = 10pm – 6am
     * @return the number of CSA corporate during this shift
     */
    public static int getNumberCSACorporate(int shift){
        if (shift == 1){
            return numberCSAcorporateShift1;
        }
        else if (shift == 2){
            return numberCSAcorporateShift2;
        }
        else{
            return numberCSAcorporateShift3;
        }
    }

    /**
     * This method is used to give the number of CSA Corporate for a certain shift
     *
     * @param shift number: shift 1 = 6am – 2pm, shift = 2pm – 10pm, shift3 = 10pm – 6am
     * @return the number of CSA corporate during this shift
     */
    public static int getNumberCSAConsumer(int shift){
        if (shift == 1){
            return numberCSAconsumerShift1;
        }
        else if (shift == 2){
            return numberCSAconsumerShift2;
        }
        else{
            return numberCSAconsumerShift3;
        }
    }

    /**
     * method to create a list of CSAcorporate
     *
     * @param numberWorkers is the number of CSAcorporate for a certain shift
     * @return an arraylist of CSAcorporate
     */
    public static ArrayList<Employee> createCSAcorporate(int numberWorkers) {
        ArrayList<Employee> output = new ArrayList<>();

        for (int i = 1; i <= numberWorkers; i++) {
            CSAcorporate CP = new CSAcorporate();
            output.add(CP);
        }
        return output;
    }

    /**
     * method to create a list of CSAconsumer
     *
     * @param numberWorkers is the number of CSAconsumer for a certain shift
     * @return an arraylist of CSAconsumer
     */
    public static ArrayList<Employee> createCSAconsumer(int numberWorkers) {
        ArrayList<Employee> output = new ArrayList<>();

        for (int i = 1; i <= numberWorkers; i++) {
            CSAconsumer CM = new CSAconsumer();
            output.add(CM);
        }
        return output;
    }

    /**
     * Method to get the machine of CSA consumers
     *
     * @return the consumer machine
     */
    public static Machine getM_consumer() {
        return m_consumer;
    }

    /**
     * Method to get the machine of CSA corporate
     *
     * @return the corporate machine
     */
    public static Machine getM_corporate() {
        return m_corporate;
    }

    public static double[][] getDataConsumer() {
        return dataConsumer;
    }

    public static double[][] getDataCorporate() {
        return dataCorporate;
    }

    /**
     * method to execute the simulation
     */
    public void executeSimulation() {

        // create the list of CSA corporate for shift 1
        ArrayList<Employee> CSAcorporateShift1 = createCSAcorporate(numberCSAcorporateShift1);
        // create the list of CSA corporate for shift 2
        ArrayList<Employee> CSAcorporateShift2 = createCSAcorporate(numberCSAcorporateShift2);
        // create the list of CSA corporate for shift 3
        ArrayList<Employee> CSAcorporateShift3 = createCSAcorporate(numberCSAcorporateShift3);

        // create the list of CSA consumer for shift 1
        ArrayList<Employee> CSAconsumerShift1 = createCSAconsumer(numberCSAconsumerShift1);
        // create the list of CSA consumer for shift 2
        ArrayList<Employee> CSAconsumerShift2 = createCSAconsumer(numberCSAconsumerShift2);
        // create the list of CSA consumer for shift 3
        ArrayList<Employee> CSAconsumerShift3 = createCSAconsumer(numberCSAconsumerShift3);

        int dayInSecond = 86400; // number of seconds in 1 day. The day starts and ends at 6 AM.
        System.out.println("dayInSecond = " + dayInSecond);

        // Creates one eventlist
        CEventList eventList = new CEventList();

        // A queue for the machine
        Queue q = new Queue();
        // set k in the queue
        q.setK(k);

        // Generate the calls for each type of customers (through one day)
        // Each Call has a service time, an interarrival time and a type
        ArrayList<Call> arrConsumer = Generator.consumersCallTimesInterArrival(0, dayInSecond);
        ArrayList<Call> arrCorporate = Generator.corporatesCallTimesInterArrival(0, dayInSecond);
        // translates the interarrival times into arrival times
        ArrayList<Call> arrivalTimesConsumers =  Generator.toCallTimes(arrConsumer);
        ArrayList<Call> arrivalTimesCorporates = Generator.toCallTimes(arrCorporate);

        // count the number of calls during each shift
        // get the number of consumers' calls for each shift
        boolean foundNumberConsumersShift1 = false;
        boolean foundNumberConsumersShift2 = false;
        int iCon1 = 0;
        while (!foundNumberConsumersShift1){
            if(arrivalTimesConsumers.get(iCon1).getTime() >= 28800){
                numberConsumerCallsShift1 = iCon1;
                foundNumberConsumersShift1 = true;
            } else {
                iCon1++;
            }
        }
        int iCon2 = iCon1;
        while (!foundNumberConsumersShift2){
            if(arrivalTimesConsumers.get(iCon2).getTime() >= 57600){
                numberConsumerCallsShift2 = iCon2 - iCon1;
                foundNumberConsumersShift2 = true;
            } else {
                iCon2++;
            }
        }
        numberConsumerCallsShift3 = arrivalTimesConsumers.size() - numberConsumerCallsShift1 - numberConsumerCallsShift2;

        // get the number of corporates' calls for each shift
        boolean foundNumberCorporatesShift1 = false;
        boolean foundNumberCorporatesShift2 = false;
        int iCor1 = 0;
        while (!foundNumberCorporatesShift1){
            if(arrivalTimesCorporates.get(iCor1).getTime() >= 28800){
                numberCorporateCallsShift1 = iCor1;
                foundNumberCorporatesShift1 = true;
            } else {
                iCor1++;
            }
        }
        int iCor2 = iCor1;
        while (!foundNumberCorporatesShift2){
            if(arrivalTimesCorporates.get(iCor2).getTime() >= 57600){
                numberCorporateCallsShift2 = iCor2 - iCor1;
                foundNumberCorporatesShift2 = true;
            } else {
                iCor2++;
            }
        }
        numberCorporateCallsShift3 = arrivalTimesCorporates.size() - numberCorporateCallsShift1 - numberCorporateCallsShift2;

        numberCallsShift1 = numberCorporateCallsShift1 + numberConsumerCallsShift1;
        numberCallsShift2 = numberCorporateCallsShift2 + numberConsumerCallsShift2;
        numberCallsShift3 = numberCorporateCallsShift3 + numberConsumerCallsShift3;

        totalNumberConsumerCalls = numberConsumerCallsShift1 + numberConsumerCallsShift2 + numberConsumerCallsShift3;
        totalNumberCustomerCalls = numberCorporateCallsShift1 + numberCorporateCallsShift2 + numberCorporateCallsShift3;


        // to print the interarrival times for each Call (of consumers and corporates)
        /*
        for (int i = 0; i < arrConsumer.size(); i++) {
            System.out.println("Interarrival times of consumers' calls");
            System.out.println(arrConsumer.get(i).getTime());
        }
        for (int i = 0; i < arrCorporate.size(); i++) {
            System.out.println("Interarrival times of corporates' calls");
            System.out.println(arrCorporate.get(i).getTime());
        }
        */

        // Create the 2 sources, 1 for each type of customers (they are both linked to the same queue and the same event list)
        // type 1 denotes a corporate's Call and type 0 denotes a consumer's Call
        Source s_corporate = new Source(q, eventList, "Corporate Source", arrCorporate, 1);
        Source s_consumer = new Source(q, eventList, "Consumer Source", arrConsumer, 0);

        // A sink for the finished corporate Call
        si_corporate = new Sink("Sink Corporate");
        // A sink for the finished consumer Call
        si_consumer = new Sink("Sink Consumer");

        // number of calls from consumers customers
        numberCallsConsumer = arrConsumer.size();
        // number of calls from corporate customers
        numberCallsCorporate = arrCorporate.size();

        // instantiate the array of service times of consumers
        double[] stConsumer = new double[numberCallsConsumer];
        // instantiate the array of service times of corporates.
        double[] stCorporate = new double[numberCallsCorporate];

        // get the service time of each Call and add store it in the st arrays
        for (int i = 0; i < arrCorporate.size(); i++) {
            double st = arrCorporate.get(i).getServiceTime();
            stCorporate[i] = st;
        }
        for (int i = 0; i < arrConsumer.size(); i++) {
            double st = arrConsumer.get(i).getServiceTime();
            stConsumer[i] = st;
        }


        // A machine used for the CSA corporates
        m_corporate = new Machine(q, si_corporate, eventList,"Machine Corporate", stCorporate, CSAcorporateShift1,CSAcorporateShift2,CSAcorporateShift3);
        // A machine used for the CSA consumers
        m_consumer = new Machine(q, si_consumer, eventList, "Machine Consumer",stConsumer, CSAconsumerShift1,CSAconsumerShift2,CSAconsumerShift3);

        // start the eventlist without limited time
        eventList.start();

        // sum of the number of clients in the system at different time
        int sumClientsInSystem = 0;
        for (int i = 0; i < numberClientInSystem.size(); i++) {
            sumClientsInSystem += numberClientInSystem.get(i);
        }
        // compute the average
        avgNumberClientInSystem = sumClientsInSystem/(numberClientInSystem.size());

        // get the times of the different events of each objects (from the sinks)
        double[] timesConsumer = si_consumer.getTimes();
        double[] timesCorporate = si_corporate.getTimes();

        // create a vector of product number for the CSV output
        double[] productConsumerNumber = new double[timesConsumer.length/3];
        for (int i = 0; i < timesConsumer.length/3; i++) {
            productConsumerNumber[i] = i+1;
        }

        // get a nicer display for the CSV output
        double[][] storageMatrixConsumer = new double[timesConsumer.length / 3][3];
        int count = 0;
        for (int i = 0; i < timesConsumer.length/3; i++) {
            for (int j = 0; j < 3; j++) {
                storageMatrixConsumer[i][j] = timesConsumer[count];
                count++;
            }
        }
        // get a nicer display for the CSV output
        double[][] storageMatrixCorporate = new double[timesCorporate.length / 3][3];
        count = 0;
        for (int i = 0; i < timesCorporate.length/3; i++) {
            for (int j = 0; j < 3; j++) {
                storageMatrixCorporate[i][j] = timesCorporate[count];
                count++;
            }
        }
        // get a nicer display for the CSV output
        dataConsumer = new double[timesConsumer.length/3][6];
        for (int i = 0; i < timesConsumer.length/3; i++) {
            for (int j = 0; j < 4; j++) {
                if (j==0) {
                    dataConsumer[i][j] = productConsumerNumber[i];
                }
                else {
                    dataConsumer[i][j] = storageMatrixConsumer[i][j-1];
                }
            }
        }

        // compute the waiting time for each client
        for (int i = 0; i < timesConsumer.length/3; i++) {
            dataConsumer[i][4] = dataConsumer[i][2] - dataConsumer[i][1];
            dataConsumer[i][5] = dataConsumer[i][3] - dataConsumer[i][2];
        }
        // compute the waiting time for each client
        double[] productCorporateNumber = new double[timesCorporate.length/3];
        for (int i = 0; i < timesCorporate.length/3; i++) {
            productCorporateNumber[i] = i+1;
        }
        // get a nicer display for the CSV output
        dataCorporate = new double[timesCorporate.length/3][6];
        for (int i = 0; i < timesCorporate.length/3; i++) {
            for (int j = 0; j < 4; j++) {
                if (j==0) {
                    dataCorporate[i][j] = productCorporateNumber[i];
                }
                else {
                    dataCorporate[i][j] = storageMatrixCorporate[i][j-1];
                }
            }
        }
        // computes the service time for each client
        for (int i = 0; i < timesCorporate.length/3; i++) {
            dataCorporate[i][4] = dataCorporate[i][2] - dataCorporate[i][1];
            dataCorporate[i][5] = dataCorporate[i][3] - dataCorporate[i][2];
        }
        System.out.println("size of matrix = " + numberClientInSystem.size());
        System.out.println("avg number = " + avgNumberClientInSystem);
        OutputCSVsimulation.set();
        OutputCSVsimulation.writeResume(check);
    }

}