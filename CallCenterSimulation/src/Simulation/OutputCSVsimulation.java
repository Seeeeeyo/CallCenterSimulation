package Simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Deals with the simulations' results and outputs them to a CSV file
 */
public class OutputCSVsimulation {

    /* number of CSA per shift */
    private static double numCSACorporateShift1;
    private static double numCSACorporateShift2;
    private static double numCSACorporateShift3;
    private static double numCSAConsumerShift1;
    private static double numCSAConsumerShift2;
    private static double numCSAConsumerShift3;
    /** cost of each shift */
    private static double costShift1;
    private static double costShift2;
    private static double costShift3;
    /** total cost (1 day) */
    public static int totalCost;
    /** condition for CSA customers to help CSA consumers */
    private static int k;
    /** sum of the delay of clients, used to compute the average delay */
    private static double sumDelaysConsumers;
    private static double sumDelaysCorporate;
    /** average waiting time */
    private static double consumerAvgWaitingTime;
    private static double corporateAvgWaitingTime;
    /* matrix with all the infos about the event */
    private static double[][] storageMatrixConsumer;
    private static double[][] storageMatrixCorporate;
    /** average number of clients in the system */
    private static double avgNumberCustomersInSystem;
    /** title of columns in csv */
    private static String stringHeaderDataConsumer = "ProductNumber,ArrivalTimeConsumer,StartTimeConsumer,EndTimeConsumer,DeltaTimeConsumer,ServiceTimeConsumer";
    private static String stringHeaderDataCorporate = "ProductNumber,ArrivalTimeCorporate,StartTimeCorporate,EndTimeCorporate,DeltaTimeCorporate,ServiceTimeCorporate";
    /** percentages of clients in a specific performance bound */
    public static double percentage5min;
    public static double percentage10min;
    public static double percentage3min;
    public static double percentage7min;
    /** number of clients which waited less than a certain amount od time */
    private static int consumerCounter5min;
    private static int consumerCounter10min;
    private static int corporateCounter3min;
    private static int corporateCounter7min;


    /**
     * Method used to compute and set the variables before sending them to the CSV file
     */
    public static void set(){

        consumerCounter5min = 0;
        consumerCounter10min = 0;
        corporateCounter3min = 0;
        corporateCounter7min = 0;

        k = Simulation.k;
        costShift1 = Simulation.costShift1;
        costShift2 = Simulation.costShift2;
        costShift3 = Simulation.costShift3;
        totalCost = Simulation.totalCostDay;

        storageMatrixConsumer = Simulation.getDataConsumer();
        storageMatrixCorporate = Simulation.getDataCorporate();

        // count the number of clients which waited less than a certain amount of time
        for (int i = 0; i < storageMatrixConsumer.length; i++) {
            if (storageMatrixConsumer[i][4] <= 300) {
                consumerCounter5min++;
            }
            if (storageMatrixConsumer[i][4] <= 600) {
                consumerCounter10min++;
            }
        }
        // translate if to percentages
        percentage5min = (((double)consumerCounter5min/(double)storageMatrixConsumer.length)*100);
        percentage10min = (((double)consumerCounter10min/(double)storageMatrixConsumer.length)*100);
        // count the number of clients which waited less than a certain amount of time
        for (int i = 0; i < storageMatrixCorporate.length; i++) {
            if (storageMatrixCorporate[i][4] <= 180) {
                corporateCounter3min++;
            }
            if (storageMatrixCorporate[i][4] <= 420) {
                corporateCounter7min++;
            }
        }
        // translate if to percentages
        percentage3min = (((double)corporateCounter3min/(double)storageMatrixCorporate.length)*100);
        percentage7min = (((double)corporateCounter7min/(double)storageMatrixCorporate.length)*100);

        numCSAConsumerShift1 =  Simulation.numberCSAconsumerShift1;
        numCSAConsumerShift2 = Simulation.numberCSAconsumerShift2;
        numCSAConsumerShift3 = Simulation.numberCSAconsumerShift3;
        numCSACorporateShift1 = Simulation.numberCSAcorporateShift1;
        numCSACorporateShift2 = Simulation.numberCSAcorporateShift2;
        numCSACorporateShift3 = Simulation.numberCSAcorporateShift3;

        // compute the sum of the delays of each client
        sumDelaysConsumers = 0;
        for (int i = 0; i < storageMatrixConsumer.length; i++) {
            for (int j = 0; j < 6; j++) {
                if(j == 4){
                    sumDelaysConsumers += storageMatrixConsumer[i][j];
                }
            }
        }
        consumerAvgWaitingTime = sumDelaysConsumers/(Simulation.numberCallsConsumer);
        // compute the sum of the delays of each client
        sumDelaysCorporate = 0;
        for (int i = 0; i < storageMatrixCorporate.length; i++) {
            for (int j = 0; j < 6; j++) {
                if(j == 4){
                    sumDelaysCorporate += storageMatrixCorporate[i][j];
                }
            }
        }
        corporateAvgWaitingTime = sumDelaysCorporate/(Simulation.numberCallsCorporate);
        avgNumberCustomersInSystem = Simulation.avgNumberClientInSystem;
    }
    /** title of columns in csv */
    private static String stringHeaderResume = "ConsumerAvgWaitingTime,CorporateAvgWaitingTime,avgNumberCustomersInSystem,#CSACorporateShift1,#CSACorporateShift2,#CSACorporateShift3,#CSAConsumerShift1,#CSAConsumerShift2,#CSAConsumerShift3,CostShift1,CostShift2,CostShift3,TotalCost,k";
    private static String stringDataResume;
    /** title of columns in csv */
    private static String percentageHeader = "Consumer%5min,Consumer%10min,Corporate%3min,Corporate%7min";

    /**
     * Method used to export the data to a CSV file
     * @param firstRunFirstHeader is a boolean, true if we want the columns' titles otherwise false.
     */
    public static void writeResume(boolean firstRunFirstHeader) {

        stringDataResume = consumerAvgWaitingTime+", "+corporateAvgWaitingTime+", "+avgNumberCustomersInSystem+",   "+numCSACorporateShift1+", "+numCSACorporateShift2+", "+numCSACorporateShift3+",   "+numCSAConsumerShift1+", "+numCSAConsumerShift2+", "+numCSAConsumerShift3+",   "+costShift1+", "+costShift2+", "+costShift3+", " +totalCost+",   "+k;
        String[][] dataConsumer = new String[storageMatrixConsumer.length][storageMatrixConsumer[0].length];
        for (int i = 0; i < storageMatrixConsumer.length; i++) {
            for (int j = 0; j < storageMatrixConsumer[0].length; j++) {
                dataConsumer[i][j] = Double.toString(storageMatrixConsumer[i][j]);
            }
        }

        String[][] dataCorporate = new String[storageMatrixCorporate.length][storageMatrixCorporate[0].length];
        for (int i = 0; i < storageMatrixCorporate.length; i++) {
            for (int j = 0; j < storageMatrixCorporate[0].length; j++) {
                dataCorporate[i][j] = Double.toString(storageMatrixCorporate[i][j]);
            }
        }

        try {
            String filepath = "outputRun1Conf6.txt";
            FileWriter fileWriter = new FileWriter(filepath,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            if (firstRunFirstHeader) {
                printWriter.println(stringHeaderResume);
            }

            printWriter.println(stringDataResume);


            printWriter.println();

            printWriter.println(percentageHeader);
            printWriter.println(percentage5min+", "+percentage10min+", "+percentage3min+", "+percentage7min);

            printWriter.println();

            printWriter.println(stringHeaderDataConsumer);
            for (int i = 0; i < dataConsumer.length; i++) {
                printWriter.println(dataConsumer[i][0]+","+dataConsumer[i][1]+","+dataConsumer[i][2]+","+dataConsumer[i][3]+","+dataConsumer[i][4]+","+dataConsumer[i][5]);
            }


            printWriter.println();

            printWriter.println(stringHeaderDataCorporate);
            for (int i = 0; i < dataCorporate.length; i++) {
                printWriter.println(dataCorporate[i][0]+","+dataCorporate[i][1]+","+dataCorporate[i][2]+","+dataCorporate[i][3]+","+dataCorporate[i][4]+","+dataCorporate[i][5]);
            }

            printWriter.flush();
            printWriter.close();
        }
        catch (Exception e) {
            System.out.println("Recorded not save");
        }
    }
}
