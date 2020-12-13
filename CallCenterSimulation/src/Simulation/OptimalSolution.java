package Simulation;

/**
 * class used to find an optimal solution. Many experiments have been done from here.
 */

public class OptimalSolution {
    /*
    public static int numberCSAcorporateShift1 = 1;
    public static int numberCSAcorporateShift2 = 1;
    public static int numberCSAcorporateShift3 = 1;
    public static int numberCSAconsumerShift1 = 1;
    public static int numberCSAconsumerShift2 = 1;
    public static int numberCSAconsumerShift3 = 1;
     */

    /**
     * predefined ranges of number of CSA consumers per shift (based on simulations' results)
     */
    private static int maxK = 3;
    private static int minShift1Consumer = 4;
    private static int maxShift1Consumer = 8;
    private static int minShift2Consumer = 5;
    private static int maxShift2Consumer = 8;
    private static int minShift3Consumer = 1;
    private static int maxShift3Consumer = 4;
    /**
     * predefined ranges of number of CSA corporates per shift (based on simulations' results)
     */
    private static int minShift1Corporate = 5;
    private static int maxShift1Corporate = 6;
    private static int minShift2Corporate = 5;
    private static int maxShift2Corporate = 6;
    private static int minShift3Corporate = 2;
    private static int maxShift3Corporate = 3;
    /*
    private static int[] shift1Corporate = {4,5,4,4,5,4,4,4,4,5,4,4,4,4,4,5,5,4,4,5,4,4,5,5,5,5,5,4,4,4,4,4,4,4,4,4,4,5,5,5,5,5,4,4,4,5,4,5,5,4};
    private static int[] shift2Corporate = {4,4,4,4,4,4,4,4,4,3,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4};
    private static int[] shift3Corporate = {1,1,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,1,2,2,1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,2,2,2,2,2,3,3,3,1,2,1,1,2};
    private static int[] shift1Consumer = {5,4,4,4,4,5,5,6,5,6,6,4,6,5,4,5,5,4,5,5,5,4,4,4,3,4,3,4,6,5,6,5,4,5,4,5,4,4,4,3,4,4,4,4,5,5,6,5,4,5};
    private static int[] shift2Consumer = {4,5,5,5,5,6,6,5,5,5,5,6,4,4,6,5,5,6,5,5,5,6,5,6,6,5,7,5,6,6,5,6,6,5,6,5,7,5,5,5,5,4,5,5,4,6,5,6,6,5};
    private static int[] shift3Consumer = {3,2,2,2,2,2,2,2,3,2,2,3,3,4,3,2,2,2,2,2,2,2,3,2,3,3,2,3,2,3,3,3,4,4,4,4,3,2,2,3,2,3,2,2,2,2,2,2,3,3};

    private static int[] kSimulation = {2,1,2,1,2,1,3,2,1,2,1,2,1,1,3,1,3,1,3,2,1,3,2,3,1,1,1,1,1,2,3,1,1,2,2,3,3,1,2,1,3,1,3,2,1,1,1,3,1,1};
     */

    /**
     * array storing the configurations' values after having pruned the configurations
     */
    private static int[] shift1Corporate = {5,4,4,5,4,4,4,4,5,4,4,4,4};
    private static int[] shift2Corporate = {4,4,4,4,4,4,4,4,3,4,4,4,4};
    private static int[] shift3Corporate = {1,2,2,1,1,1,1,1,1,1,1,1,1};
    private static int[] shift1Consumer = {4,4,4,4,5,5,6,5,6,6,4,6,4};
    private static int[] shift2Consumer = {5,5,5,5,6,6,5,5,5,5,6,4,4};
    private static int[] shift3Consumer = {2,2,2,2,2,2,2,3,2,2,3,3,3};
    private static int[] kSimulation = {1,2,1,2,1,3,2,1,2,1,2,1,3};

    /**
     * percentages to compare to the required performance guarantees. e.g 90% of the consumers should be assisted within 5 minutes
     */
    private static double percentage5min;
    private static double percentage10min;
    private static double percentage3min;
    private static double percentage7min;

    /**
     * average performance percentages for a certain simulation through many runs
     */
    private static double averagePercentage5min;
    private static double averagePercentage10min;
    private static double averagePercentage3min;
    private static double averagePercentage7min;

    /**
     * Cost of a certain configuration (roster)
     */
    private static int totalCost;
    private static int totalCostCount;

    public static void main(String[]args) {


        // initial method to find some possible rooster
        /*
        for (int i = minShift1Corporate; i <= maxShift1Corporate; i++) {
            for (int j = minShift2Corporate; j <= maxShift2Corporate; j++) {
                for (int l = minShift3Corporate; l <= maxShift3Corporate; l++) {
                    for (int m = minShift1Consumer; m <= maxShift1Consumer; m++) {
                        for (int n = minShift2Consumer; n <= maxShift2Consumer; n++) {
                            for (int o = minShift3Consumer; o <= maxShift3Consumer; o++) {
                                for (int k = 1; k < maxK; k++) {

                                    totalCostCount = 0;
                                    double count5min = 0;
                                    double count10min = 0;
                                    double count3min = 0;
                                    double count7min = 0;
                                    int iterations = 20;
                                    for (int w = 0; w < iterations; w++) {
                                        Simulation simulation = new Simulation(i, j, l, m, n, o,k, false);
                                        simulation.executeSimulation();
                                        percentage5min = OutputCSVsimulation.percentage5min;
                                        percentage10min = OutputCSVsimulation.percentage10min;
                                        percentage3min = OutputCSVsimulation.percentage3min;
                                        percentage7min = OutputCSVsimulation.percentage7min;
                                        count5min += percentage5min;
                                        count10min += percentage10min;
                                        count3min += percentage3min;
                                        count7min += percentage7min;
                                        totalCost = OutputCSVsimulation.totalCost;
                                        totalCostCount += totalCost;
                                    }
                                    averagePercentage5min = count5min / iterations;
                                    averagePercentage10min = count10min / iterations;
                                    averagePercentage3min = count3min / iterations;
                                    averagePercentage7min = count7min / iterations;
                                    double averageTotalCost = (double)totalCostCount/(double)iterations;
                                    if (averagePercentage5min >= 90 && averagePercentage10min >= 95 && averagePercentage3min >= 95 && averagePercentage7min >= 99) {
                                        System.out.println("-----------------------");
                                        System.out.println("Corporate shift 1 = " + i);
                                        System.out.println("Corporate shift 2 = " + j);
                                        System.out.println("Corporate shift 3 = " + l);
                                        System.out.println("Consumer shift 1 = " + m);
                                        System.out.println("Consumer shift 2 = " + n);
                                        System.out.println("Consumer shift 3 = " + o);
                                        System.out.println("k = " + k);
                                        System.out.println("averageTotalCost = " + averageTotalCost);
                                        System.out.println();
                                        System.out.println("averagePercentage5min = " + averagePercentage5min);
                                        System.out.println("averagePercentage10min = " + averagePercentage10min);
                                        System.out.println("averagePercentage3min = " + averagePercentage3min);
                                        System.out.println("averagePercentage7min = " + averagePercentage7min);
                                    }
                                }
                            }
                            }
                        }
                    }
                }
            }
        }
         */

        // simulates the final chosen configuration many times and computes the average. It also checks if the performance guarantees are met.
        for (int i = 0; i < 1; i++) {
            totalCostCount = 0;
            double count5min = 0;
            double count10min = 0;
            double count3min = 0;
            double count7min = 0;
            int iterations = 1;
            for (int j = 0; j < iterations; j++) {
                SimulationSolution simulation = new SimulationSolution(6,5,2,4,5,1, 1, false);
                simulation.executeSimulation();
                percentage5min = OutputCSVsimulation.percentage5min;
                percentage10min = OutputCSVsimulation.percentage10min;
                percentage3min = OutputCSVsimulation.percentage3min;
                percentage7min = OutputCSVsimulation.percentage7min;
                count5min += percentage5min;
                count10min += percentage10min;
                count3min += percentage3min;
                count7min += percentage7min;
                totalCost = OutputCSVsimulation.totalCost;
                totalCostCount += totalCost;
            }
            averagePercentage5min = count5min/iterations;
            averagePercentage10min = count10min/iterations;
            averagePercentage3min = count3min/iterations;
            averagePercentage7min = count7min/iterations;
            double averageTotalCost = (double)totalCostCount/(double)iterations;
            if (averagePercentage5min >= 90 && averagePercentage10min >= 95 && averagePercentage3min >= 95 && averagePercentage7min >= 99) {
                System.out.println("-----------------------");
                System.out.println("averageTotalCost = " + averageTotalCost);
                System.out.println();
                System.out.println("averagePercentage5min = " + averagePercentage5min);
                System.out.println("averagePercentage10min = " + averagePercentage10min);
                System.out.println("averagePercentage3min = " + averagePercentage3min);
                System.out.println("averagePercentage7min = " + averagePercentage7min);
            }
        }
    }
}
