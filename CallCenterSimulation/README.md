# Call Center Simulation 
# Mathematical Simulation class - Data Science & Knoweldge Engineering - Maastricht University

Given a 24/7 call center which is dealing with 2 types of customers, 2 types of customer service agents (CSA) and running with 3 different shifts; the task of this project is to come up with a strategy for which certain constraints are respected and the total cost is kept as low as possible. 
The 2 types of customers are called consumers and corporates. 
The 2 types of CSA are called CSA consumers and CSA corporates. They can both handle consumers but only CSA corporates can handle corporate clients.
Moreover, the cost of a CSA corporate is €60 per hour and €35 per hour for a CSA consumer. 
As stated above, there are 3 shifts along the day and the shift changes are at: 6am, 2pm, and 10pm.
The arrival rate of both type of clients, as well as the service time respect some specific-predefined distributions.
The decision variables that must be taken are the number of each type of employee for each shift, the number and the conditions under which the CSA corporates can help their colleague if necessary (to handle consumers).
This simulation with the chosen strategy is implemented in Java 10. The data analysis is done in Matlab. 


To run the simulation, simply run the main method in the "Simulation.class". It will simulate a complete day at the call center with the chosen strategy and roster. You can visualize all the events in the terminal. The result of the simulation is stored into a CSV file. 
