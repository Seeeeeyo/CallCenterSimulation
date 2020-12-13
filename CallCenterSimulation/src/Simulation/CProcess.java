package Simulation;

/**
 * Classes that implement this interface can process events
 */
public interface CProcess {
    /**
     * Method to have this object process an event
     *
     * @param type The type of the event that has to be executed
     * @param tme  The current time
     */
	void execute(int type, double tme);
}

