package Simulation;

/**
 * Represents a CSA consumer. Each CSA has a status (idle or busy)
 */
public class CSAconsumer implements Employee {

    private String status;

    public CSAconsumer() {
        setStatus("i");
    }
    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        if (status.equals("i")) {
            this.status = "i";
        } else if (status.equals("b")) {
            this.status = "b";
        }
    }

    @Override
    public String toString() {
        return "CSAconsumer{" +
                "status='" + status + '\'' +
                '}';
    }
}
