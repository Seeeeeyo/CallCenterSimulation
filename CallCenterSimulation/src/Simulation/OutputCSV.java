package Simulation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Deals with the simulations' results and outputs them to a CSV file
 */
public class OutputCSV {

    private static double numCSACorporateShift1 = Simulation.numberCSAcorporateShift1;
    private static double numCSACorporateShift2 = Simulation.numberCSAcorporateShift2;
    private static double numCSACorporateShift3 = Simulation.numberCSAcorporateShift3;
    private static double numCSAConsumerShift1 = Simulation.numberCSAconsumerShift1;
    private static double numCSAConsumerShift2 = Simulation.numberCSAconsumerShift2;
    private static double numCSAConsumerShift3 = Simulation.numberCSAconsumerShift3;
    private static double costShift1 = Simulation.costShift1;
    private static double costShift2 = Simulation.costShift2;
    private static double costShift3 = Simulation.costShift3;
    private static int k = Simulation.k;

    private static double[][] storageMatrixConsumer = Simulation.getDataConsumer();
    private static double[][] storageMatrixCorporate = Simulation.getDataCorporate();

    private static double consumerAvgWaitingTime;
    private static double corporateAvgWaitingTime;
    private static double avgNumberCustomersInSystem;

    private static String fileName;

    public static void set(){

        double sumDelaysConsumers = 0;
        for (int i = 0; i < storageMatrixConsumer.length; i++) {
            for (int j = 0; j < 6; j++) {
                if(j == 4){
                    sumDelaysConsumers += storageMatrixConsumer[i][j];
                }
            }
        }
        consumerAvgWaitingTime = sumDelaysConsumers/(Simulation.numberCallsConsumer);

        double sumDelaysCorporate = 0;
        for (int i = 0; i < storageMatrixCorporate.length; i++) {
            for (int j = 0; j < 6; j++) {
                if(j == 4){
                    sumDelaysCorporate += storageMatrixCorporate[i][j];
                }
            }
        }
        corporateAvgWaitingTime = sumDelaysCorporate/(Simulation.numberCallsCorporate);
       avgNumberCustomersInSystem = Simulation.avgNumberClientInSystem;

       fileName = "output.txt";
    }

    private static String stringHeaderResume = "ConsumerAvgWaitingTime,CorporateAvgWaitingTime,avgNumberCustomersInSystem,#CSACorporateShift1,#CSACorporateShift2,#CSACorporateShift3,#CSAConsumerShift1,#CSAConsumerShift2,#CSAConsumerShift3,CostShift1,CostShift2,CostShift3,k";
    private static String stringDataResume;

    private static String stringHeaderDataConsumer = "ProductNumber,ArrivalTimeConsumer,StartTimeConsumer,EndTimeConsumer,DeltaTimeConsumer,ServiceTimeConsumer";
    private static String stringHeaderDataCorporate = "ProductNumber,ArrivalTimeCorporate,StartTimeCorporate,EndTimeCorporate,DeltaTimeCorporate,ServiceTimeCorporate";

    public static void writeResume() {

        stringDataResume = consumerAvgWaitingTime+","+corporateAvgWaitingTime+","+avgNumberCustomersInSystem+","+numCSACorporateShift1+","+numCSACorporateShift2+","+numCSACorporateShift3+","+numCSAConsumerShift1+","+numCSAConsumerShift2+","+numCSAConsumerShift3+","+costShift1+","+costShift2+","+costShift3+","+k;
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
            String filepath = fileName;
            FileWriter fileWriter = new FileWriter(filepath,false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            printWriter.println(stringHeaderResume);
            printWriter.println(stringDataResume);

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
            System.out.println();
            System.out.println("Data is save in: " + fileName);
        }
        catch (Exception e) {
            System.out.println("Recorded not save");
        }
    }
}
