package airlinebookingapp.stub;

import airlinebookingapp.dao.PassengerDAO;
import airlinebookingapp.model.Passenger;


public class FakePassengerDao extends PassengerDAO{
	public boolean wasCalled = false;
	
	@Override
	public void savePassenger(Passenger p) {
		wasCalled=true;
        System.out.println("Fake savePassenger called");
    }

}
