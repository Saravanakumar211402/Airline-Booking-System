package airlinebookingapp.stub;


import airlinebookingapp.dao.FlightDAO;

public class FakeFlightDao extends FlightDAO{
	public boolean wasCalled = false;

    @Override
    public boolean updateAvailableSeats(String flightId, int seatsChange) {
        wasCalled = true;
        System.out.println("Fake updateAvailableSeats called for flight: " + flightId);
        return true;
    } 

}
