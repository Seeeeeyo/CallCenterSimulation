package Simulation;
/**
 * A Call is an object with a type, a service time and a time. This is used to generator generates calls
 */
public class Call {
    int type; // 1 if corporate and 0 if consumer
    double time; //can store a inter arrival call time or a call time
    double serviceTime; //service time needed for this call

    public int getType() {
        return type;
    }
    public boolean isConsumer(){return type==0;}
    public boolean isCorporate(){return type==1;}

    public void setType(int type) {
        this.type = type;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Call(int type, double time, double serviceTime) {
        this.type = type;
        this.time = time;
        this.serviceTime = serviceTime;
    }

//    print formatting
    @Override
    public String toString() {
        return "Call{" +
                "type=" + type +
                ", time=" + time +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
