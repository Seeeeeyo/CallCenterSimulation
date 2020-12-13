package Simulation;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class used to generate calls from different distributions.
 */
public class Generator {

    /**
     * @param a arraylist of call times
     * @return arraylist call objects shaped to be a consumer call
     */
    private static ArrayList<Call> toCallObjConsumers(ArrayList<Double> a){
        ArrayList<Call> out = new ArrayList<>();
        for(Double d : a){
            //0 is the type of consumers
            // setting the service time of
            out.add(new Call(0,d, consumerNormalDist()));
        }
        return out;
    }

    /**
     * @param a arraylist of call times
     * @return arraylist call objects shaped to be a corporate call
     */
    private static ArrayList<Call> toCallObjCorporates(ArrayList<Double> a){
        ArrayList<Call> out = new ArrayList<>();
        for(Double d : a){
            out.add(new Call(1,d, corporateNormalDist()));
        }
        return out;
    }

    /**
     * generates the call times of customers, times are in seconds
     * @param timeFrom time from the call starts
     * @param timeTo time from the call ends
     * @return arraylist of call times in seconds
     */
    public static ArrayList<Call> customersCallTimes(double timeFrom, double timeTo){
        ArrayList<Call> out = new ArrayList<>();

        //        merging the consumers call times and the corporates call times
        for(Double d :consumersCallTimes(timeFrom,timeTo)){
            out.add(new Call(0,d, consumerNormalDist()));
        }
        for(Double d :corporatesCallTimes(timeFrom,timeTo)){
            out.add(new Call(1,d, corporateNormalDist()));
        }
        //        sorting the array
        out.sort(new Comparator<Call>() {

            @Override
            public int compare(Call t1, Call t2) {
                return Double.compare(t1.time,t2.time);
            }
        });
        return out;
    }

    public static ArrayList<Call> customersCallTimesInterArrival(double timeFrom, double timeTo) {
        return toInterArrival(customersCallTimes(timeFrom,timeTo));
    }
    /**
     * converting an call times to an array of inter arrivals by computing the difference between the call times
     * @param a arraylist of call times
     * @return arraylist of call inter arrival times in seconds
     */
    public static ArrayList<Call> toInterArrival(ArrayList<Call> a) {
        ArrayList<Call> out = new ArrayList<>();
        double current_time = a.remove(0).time;
        for(Call c : a){
            double time = c.getTime();
                out.add(new Call(c.type, time - current_time, c.getServiceTime()));
            current_time  = time;
        }
        return out;
    }

    /**
     * converting an call inter arrivals to an array of calls times by summing the inter arrivals times
     * @param a arraylist of call inter arrival times
     * @return arraylist of call times in seconds
     */
    public static ArrayList<Call> toCallTimes(ArrayList<Call> a) {
        ArrayList<Call> out = new ArrayList<>();
        double current_time = a.get(0).time;
        for(Call c : a){
            double time = c.getTime();
            out.add(new Call(c.type, time + current_time, c.getServiceTime()));
//            summing the inter arrival calls
            current_time  += time;
        }
        return out;
    }
    /**
     *
     * @param timeFrom time from the call starts
     * @param timeTo time from the call ends
     * @return arraylist of consumers inter arrival call times in seconds
     */
    public static ArrayList<Call> consumersCallTimesInterArrival(double timeFrom, double timeTo) {
        return toInterArrival(toCallObjConsumers(consumersCallTimes(timeFrom,timeTo)));
    }
    /**
     *
     * @param timeFrom time from the call starts
     * @param timeTo time from the call ends
     * @return arraylist of corporates inter arrival call times in seconds
     */
    public static ArrayList<Call> corporatesCallTimesInterArrival(double timeFrom, double timeTo) {
        return toInterArrival(toCallObjCorporates(corporatesCallTimes(timeFrom,timeTo)));
    }

  //used to make tests
    public static void main(String [] args){
        ArrayList<Double> c = corporatesCallTimes(0,24*60*60);
        for(Double cll:c){
          System.out.println(cll+";  ");
        }
    }

    /**
     * Method returning calls times of consumers in a specific time range
     * @param timeFrom start of the period when calls should arrive, should be in seconds.
     * @return an array containing  the exact time when the Call happen
     */
    public static ArrayList<Double> consumersCallTimes(double timeFrom, double timeTo) {
        double[] times  = dayFormat(timeFrom,timeTo);
        timeFrom = times[0];
        timeTo = times[1];
        ArrayList<Double> callTimes = new ArrayList<>();

        do {
            double lambdaStar = 3.8; //max lambda
            // gets the lambda for the corresponding time
            double lambda = consumersLambdaPerTime(timeFrom);
            double u1 = uniformDist();
            // Lewis without thinning
            if(u1<=lambda/lambdaStar) {
                lambda = lambda / 60; //lambda per second
                double u2 = uniformDist();
                double nextTime = timeFrom - (1 / lambda) * Math.log(u2);
                if (nextTime <= timeTo) {
                    callTimes.add(nextTime);
                }
                timeFrom = nextTime;
            }
        } while (timeFrom <= timeTo);

        return callTimes;
    }

    /**
     * Method returning calls times of corporates in a specific time range
     *
     * @param timeFrom start of the period when calls should arrive, should be in seconds.
     * @return an array containing  the exact time when the Call happen
     */
    public static ArrayList<Double> corporatesCallTimes(double timeFrom, double timeTo) {
        double[] times  = dayFormat(timeFrom,timeTo);
        timeFrom = times[0];
        timeTo = times[1];

        ArrayList<Double> callTimes = new ArrayList<>();

        do{
            double u = uniformDist();
            double lambda = corporatesLambdaPerHour(h(timeFrom));
            lambda = lambda/60;
            double nextTime = timeFrom - (1 / lambda) * Math.log(u);
            if(nextTime < timeTo) {
                callTimes.add(nextTime);
            }
            timeFrom = nextTime;
        } while(timeFrom<=timeTo);
        return callTimes;
    }

    /**
     * implementation of the constraints return the lambda calls for a specific second
     * returning the arrival rate per minute
     * @param second time indicator in second
     * @return the lambda calls for a specific time
     */
    public static double consumersLambdaPerTime(double second) {
        double height = 2 - 0.2;
        double transX = (6-9) * 60 * 60;
        double transY = 2;
        double length = 24 * 60 * 60;

        // sinusoidal function
        return transY + height * Math.sin(2 * Math.PI * (second + transX) / length);
    }

    // implementation of the constraints return the lambda calls for a specific hour
//    returning the arrival rate per minute
    private static double corporatesLambdaPerHour(double hour) {
        double out = 0;
        hour +=6;
        if (8 <= hour && hour < 18)
            return 1;
        else if (18 <= hour || hour < 8)
            return 0.2;
        else
            System.out.println("Issue here:  " + hour);

        return out;
    }

    public static double normalDist(double mu, double sigma) {
        double s = 1 - uniformDist();
        double t = uniformDist();
        // box-muller algorithm
        double out = Math.sqrt(-2 * Math.log(s)) * Math.cos(2 * Math.PI * t);
        return out * sigma + mu;
    }

    /**
     * Generating a random variate from a normal distribution
     * @return service time in seconds
     */
    public static double consumerNormalDist() {
        double s = 1 - uniformDist();
        double t = uniformDist();
//        shape of the distribution
        double mu = 1.2 * 60;
        double sigma = 35;
        double minTime = 25;

        // box-muller algorithm
        double out = 0;
        do {
            s = 1 - uniformDist();
            t = uniformDist();
            out = Math.sqrt(-2 * Math.log(s)) * Math.cos(2 * Math.PI * t);
            out = out * sigma + mu;
//            if the time is smaller than minimum time required
        }while (out < minTime);
        return out;
    }


    /**
     *
     * @param number of calls, used to generate the same amount of service time as the amount of calls along one day
     * @return and array of the service times (1 call correspond to 1 service time) along the day
     */
    public static double[] getServiceTimeCorporateAlongDay(int number){
        double[] serviceTimes = new double[number];
        for (int i = 0; i < number; i++) {
            serviceTimes[i] = corporateNormalDist();
        }
        return serviceTimes;
    }

    /**
     *
     * @param number of calls, used to generate the same amount of service time as the amount of calls along one day
     * @return and array of the service times (1 call correspond to 1 service time) along the day
     */
    public static double[] getServiceTimeConsumerAlongDay(int number){
        double[] serviceTimes = new double[number];
        for (int i = 0; i < number; i++) {
            serviceTimes[i] = consumerNormalDist();
        }
        return serviceTimes;
    }

    /**
     * Generating a random variate from a normal distribution
     * @return service time in seconds
     */
    public static double corporateNormalDist() {
        double s = 1 - uniformDist();
        double t = uniformDist();
        //        shape of the distribution
        double mu = 3.6 * 60;
        double sigma = 1.2 * 60;
        double minTime = 45;

        // box-muller algorithm
        double out = 0;

        do {
            s = 1 - uniformDist();
            t = uniformDist();
            out = Math.sqrt(-2 * Math.log(s)) * Math.cos(2 * Math.PI * t);
            out = out * sigma + mu;
            //            if the time is smaller than minimum time required
        } while (out < minTime);

        return out;
    }

//    generating a random variate form an uniform distribution
    public static double uniformDist() {
        return uniformDist(1);
    }

    /**
     *
     * @param size shape of the uniform distribution
     * @return  random variate form an uniform distribution
     */
    public static double uniformDist(double size) {
        return size * Math.random();
    }


    /**
     *
     * @param sec1 should be a timefrom parameter
     * @param sec2 should be a timeto parameter
     * @return formating sec1 and sec2
     */
    private static double[] dayFormat(double sec1, double sec2){
        double time1 =  sec1%24*60*60;
        double diff = sec1 -time1;
        double time2 = sec2-diff;
        return new double[]{time1,time2};
    }

//    converting a time in hour
    private static int h(double sec) {
        return (int) Math.floor(sec / (60 * 60));
    }

    //    converting a time in seconds
    private static int min(double sec) {
        return (int) Math.floor(sec / 60);
    }


//   print formatting
    public static String time(double time){
        double hour = h(time);
        time-=hour*60*60;
        double min = min(time);
        time-=min*60;
        return (hour+"h "+min+"min "+time+"sec ");
    }
}
