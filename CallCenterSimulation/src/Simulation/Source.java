package Simulation;

import java.util.ArrayList;

/**
 * A source of products
 * This class implements CProcess so that it can execute events.
 * By continuously creating new events, the source keeps busy.
 */
public class Source implements CProcess {
    /** Eventlist that will be requested to construct events */
    private final CEventList list;
    /** Queue that buffers products for the machine */
    private final ProductAcceptor queue;
    /** Name of the source */
    private final String name;
    /** Mean interarrival time */
    private final double meanArrTime;
    /** Interarrival times (in case pre-specified) */
    private ArrayList<Call> callArrayList;
    /** Interarrival time iterator */
    private int interArrCnt;

    /**
     *
     * @param q The receiver of the products
     * @param l The eventlist that is requested to construct events
     * @param n Name of object
     * @param calls The list of generated calls (from which we get the interarrival times)
     * @param type type of the products that this source deals with. Type 0 is consumers and type 1 is corporate
     */
    public Source(ProductAcceptor q, CEventList l, String n, ArrayList<Call> calls, int type) {
        list = l;
        queue = q;
        name = n;
        meanArrTime = -1;
        callArrayList = calls;
        interArrCnt = 0;
        // put first event in list for initialization
        list.add(this, type, callArrayList.get(0).getTime()); //target,type,time
    }

    @Override
    public void execute(int type, double tme) {
        Product p = new Product();
        p.stamp(tme, "Creation type " + type, name);

        if(type == 0)
            System.out.println("\tProduct = \u001b[36;1mConsumer\u001b[0m (0)");
        else
            System.out.println("\tProduct = \u001b[33;1mCorporate\u001b[0m (1)");

        System.out.println("\tEvent type = \u001b[32;1mArrival\u001b[0m");
        // show arrival
        System.out.println("\tEvent time = " + tme);
        // give arrived product to queue

        queue.giveProduct(p);
            interArrCnt++;
            //System.out.println("ia time " + interarrivalTimes.length);
            System.out.println("interarrcount " + interArrCnt);
            if (callArrayList.size() > interArrCnt) {
                if (callArrayList.get(0).type == 0) {
                    list.add(this, 0, tme + callArrayList.get(interArrCnt).getTime()); //target,type,time
                }
                else {
                    list.add(this, 1, tme + callArrayList.get(interArrCnt).getTime()); //target,type,time
                }
            } else {
                list.stop();
            }
    }


    @Override
    public String toString() {
        return "Source{" +
                "name='" + name + '\'' +
                ", meanArrTime=" + meanArrTime +
                ", interarrivalTimes size=" + callArrayList.size() +
                ", interArrCnt=" + interArrCnt +
                '}';
    }
}