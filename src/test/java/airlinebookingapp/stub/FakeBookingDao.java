package airlinebookingapp.stub;

import airlinebookingapp.dao.BookingDAO;
import airlinebookingapp.model.Booking;


public class FakeBookingDao extends BookingDAO {
	
	public boolean wasCalled = false;

    @Override
    public void saveBooking(Booking booking) {
        wasCalled = true;
        System.out.println("Fake saveBooking called for: " + booking.getBookingId());
    }

    @Override
    public double calculateRevenue() {
        return 25000.0; // fixed value for testing
    } 
}
