package Simulation;

/**
 * Represents a CSA corporate. A CSA has a status (idle or busy)
 */
public class CSAcorporate implements Employee {

    private String status;

    public CSAcorporate() {
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
        return "CSAcorporate{" +
                "status='" + status + '\'' +
                '}';
    }
}
