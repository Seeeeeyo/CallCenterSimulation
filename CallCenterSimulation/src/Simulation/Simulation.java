/**
 * Class to set the parameters and run the simulation
 */

package Simulation;

import java.util.ArrayList;


public class Simulation {

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
    public static int numberCSAcorporateShift1 = 5;
    public static int numberCSAconsumerShift1 = 4;
    public static int numberCSAcorporateShift2 = 5;
    public static int numberCSAconsumerShift2 = 6;
    public static int numberCSAcorporateShift3 = 2;
    public static int numberCSAconsumerShift3 = 1;

    /** k number of helpers */
    public static int k = 3;

    /**
     * The cost for each shift (35€/hour for a consumer and 60€/hour for a corporate)
     */
    public static int costShift1 = 8 * (numberCSAconsumerShift1 * 35 + numberCSAcorporateShift1 * 60);
    public static int costShift2 = 8 * (numberCSAconsumerShift2 * 35 + numberCSAcorporateShift2 * 60);
    public static int costShift3 = 8 * (numberCSAconsumerShift3 * 35 + numberCSAcorporateShift3 * 60);
    /**
     * total cost (for one day)
     */
    public static int totalCostDay = costShift1 + costShift2 + costShift3;

    /**
     * the number of calls per shift
     */
    private static int numberCorporateCallsShift1 = 0;
    private static int numberCorporateCallsShift2 = 0;
    private static int numberCorporateCallsShift3 = 0;
    private static int numberConsumerCallsShift1 = 0;
    private static int numberConsumerCallsShift2 = 0;
    private static int numberConsumerCallsShift3 = 0;
    private static int numberCallsShift1 = 0;
    private static int numberCallsShift2 = 0;
    private static int numberCallsShift3 = 0;
    private static int totalNumberConsumerCalls = 0;
    private static int totalNumberCustomerCalls = 0;

    public static int avgNumberClientInSystem;

    private static double[][] dataConsumer;
    private static double[][] dataCorporate;

    public static int numberCallsConsumer;
    public static int numberCallsCorporate;

    public static ArrayList<Integer> numberClientInSystem = new ArrayList<>();


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
     * method to create a list of CSA corporate
     *
     * @param numberWorkers is the number of CSA corporate for a certain shift
     * @return an array list of CSA corporate
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {

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

        // Generate the calls for each type of customers (through one day)
        // Each Call has a service time, an interarrival time and a type
        ArrayList<Call> arrConsumer = Generator.consumersCallTimesInterArrival(0, dayInSecond);
        ArrayList<Call> arrCorporate = Generator.corporatesCallTimesInterArrival(0, dayInSecond);

        // count the number of calls during each shift
        ArrayList<Call> arrivalTimesConsumers =  Generator.toCallTimes(arrConsumer);
        ArrayList<Call> arrivalTimesCorporates = Generator.toCallTimes(arrCorporate);

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

        // Creates one eventlist
        CEventList eventList = new CEventList();

        // A queue for the machine
        Queue q = new Queue();
        q.setK(k);

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

        // print the infos of the simulation
        System.out.println("\nStart of the program. \n");
        System.out.println("Source Consumer \t\t\t\t--> " + s_consumer);
        System.out.println("Source Corporate \t\t\t\t--> " + s_corporate);
        System.out.println("Queue \t\t\t\t\t\t\t--> " + q);
        System.out.println("Event list \t\t\t\t\t\t--> " + eventList);
        System.out.println("Sink Consumer \t\t\t\t\t--> " + si_consumer);
        System.out.println("Sink Corporate \t\t\t\t\t--> " + si_corporate);
        System.out.println("CSA consumers at 06.00 \t\t\t--> Idle: " + CSAconsumerShift1.stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + CSAconsumerShift1.stream().filter(s -> s.getStatus().equals("b")).count());
        System.out.println("CSA corporates at 06.00 \t\t--> Idle: " + CSAconsumerShift2.stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + CSAconsumerShift2.stream().filter(s -> s.getStatus().equals("b")).count());
        System.out.println("CSA consumers at 14.00 \t\t\t--> Idle: " + CSAconsumerShift3.stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + CSAconsumerShift3.stream().filter(s -> s.getStatus().equals("b")).count());
        System.out.println("CSA corporates at 14.00 \t\t--> Idle: " + CSAconsumerShift1.stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + CSAconsumerShift1.stream().filter(s -> s.getStatus().equals("b")).count());
        System.out.println("CSA consumers at 22.00 \t\t\t--> Idle: " + CSAconsumerShift2.stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + CSAconsumerShift2.stream().filter(s -> s.getStatus().equals("b")).count());
        System.out.println("CSA corporates at 22.00 \t\t--> Idle: " + CSAconsumerShift3.stream().filter(s -> s.getStatus().equals("i")).count() + ", busy: " + CSAconsumerShift3.stream().filter(s -> s.getStatus().equals("b")).count());
        System.out.println("Number of consumers' calls \t\t--> " + stConsumer.length);
        System.out.println("Number of corporates' calls \t--> " + stCorporate.length);
        System.out.println();

        // start the process. The limit of time is one day
        eventList.start();

        System.out.println("Sink Consumer \t--> " + si_consumer);
        System.out.println("Sink Corporate \t--> " + si_corporate);

        System.out.println();

        int sumClientsInSystem = 0;
        for (int i = 0; i < numberClientInSystem.size(); i++) {
            sumClientsInSystem += numberClientInSystem.get(i);
        }
        avgNumberClientInSystem = sumClientsInSystem/(numberClientInSystem.size());

        System.out.println("Average number of clients in the system during the simulation = " + avgNumberClientInSystem);

        System.out.println("Queue size at the end = " + q.getRow().size());

        System.out.println();

        System.out.println("Counter Corporate calls in Shift 1 = " + m_corporate.getNumberCorporateExecutedShift1());
        System.out.println("Counter Corporate calls in Shift 2 = " + m_corporate.getNumberCorporateExecutedShift2());
        System.out.println("Counter Corporate calls in Shift 3 = " + m_corporate.getNumberCorporateExecutedShift3());
        System.out.println("Counter Consumer calls in Shift 1 = " + m_consumer.getNumberConsumerExecutedShift1());
        System.out.println("Counter Consumer calls in Shift 2 = " + m_consumer.getNumberConsumerExecutedShift2());
        System.out.println("Counter Consumer calls in Shift 3 = " + m_consumer.getNumberConsumerExecutedShift3());

        System.out.println();

        System.out.println("Number calls Shift 1 = " + numberCallsShift1);
        System.out.println("Number calls Shift 2 = " + numberCallsShift2);
        System.out.println("Number calls Shift 3 = " + numberCallsShift3);

        System.out.println();

        System.out.println("Total number of corporate  calls = " + si_corporate.getNumbers().length/3);
        System.out.println("Total number of consumer  calls = " + si_consumer.getNumbers().length/3);
        System.out.println("Total number of calls = " + ((si_corporate.getNumbers().length/3)+(si_consumer.getNumbers().length/3)));

        System.out.println();

        System.out.println("Number of consumer calls sent to CSA corporate = " + ((m_corporate.getNumberCorporateExecutedShift1()+m_corporate.getNumberCorporateExecutedShift2()+m_corporate.getNumberCorporateExecutedShift3())-numberCallsCorporate));

        System.out.println();

        // print the costs
        System.out.println("Cost Shift 1 = " + costShift1 + "€");
        System.out.println("Cost Shift 2 = " + costShift2 + "€");
        System.out.println("Cost Shift 3 = " + costShift3 + "€");
        System.out.println("Total cost (full day) = " + totalCostDay + "€");


        double[] timesConsumer = si_consumer.getTimes();
        double[] timesCorporate = si_corporate.getTimes();

        double[] productConsumerNumber = new double[timesConsumer.length/3];
        for (int i = 0; i < timesConsumer.length/3; i++) {
            productConsumerNumber[i] = i+1;
        }

        double[][] storageMatrixConsumer = new double[timesConsumer.length / 3][3];
        int count = 0;
        for (int i = 0; i < timesConsumer.length/3; i++) {
            for (int j = 0; j < 3; j++) {
                storageMatrixConsumer[i][j] = timesConsumer[count];
                count++;
            }
        }

        double[][] storageMatrixCorporate = new double[timesCorporate.length / 3][3];
        count = 0;
        for (int i = 0; i < timesCorporate.length/3; i++) {
            for (int j = 0; j < 3; j++) {
                storageMatrixCorporate[i][j] = timesCorporate[count];
                count++;
            }
        }

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

        for (int i = 0; i < timesConsumer.length/3; i++) {
            dataConsumer[i][4] = dataConsumer[i][2] - dataConsumer[i][1];
            dataConsumer[i][5] = dataConsumer[i][3] - dataConsumer[i][2];
        }

        double[] productCorporateNumber = new double[timesCorporate.length/3];
        for (int i = 0; i < timesCorporate.length/3; i++) {
            productCorporateNumber[i] = i+1;
        }

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

        for (int i = 0; i < timesCorporate.length/3; i++) {
            dataCorporate[i][4] = dataCorporate[i][2] - dataCorporate[i][1];
            dataCorporate[i][5] = dataCorporate[i][3] - dataCorporate[i][2];
        }
        OutputCSV.set();
        OutputCSV.writeResume();
    }

}