package airlinebookingapp.test;

import airlinebookingapp.dao.BookingDAO;
import airlinebookingapp.dao.PassengerDAO;
import airlinebookingapp.dao.FlightDAO;
import airlinebookingapp.model.Booking;
import airlinebookingapp.model.Flight;
import airlinebookingapp.model.Passenger;
import airlinebookingapp.service.BookingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*the Role of Mocks

Mocks are for interaction testing:
- “Is my service layer wired correctly to its dependencies?”
- “Does it call the right DAO methods with the right data?”
- “Does it handle the DAO’s responses properly?”
*/

@ExtendWith(MockitoExtension.class)
class BookingServiceDaoTest {

	@Mock
	PassengerDAO passengerDAO;

	@Mock
	BookingDAO bookingDAO;

	@Mock
	FlightDAO flightDAO;

	@InjectMocks
	BookingService bookingService;//what we are mocking test and write test for

	@Test
	void testCreateBooking_callsAllDAOs() throws Exception {
		// Arrange
		Flight flight = new Flight("F001", "FN001", "CBE", "DEL", 1000.0, 100, 100);
		Passenger passenger = new Passenger("P001", "Test User", "test@", 12345);

		// Stub DAO behavior
		when(flightDAO.updateAvailableSeats("F001", -2)).thenReturn(true);

		// Act
		Booking booking = bookingService.createBooking(flight, passenger, 2);

		// AssertS
		assertNotNull(booking);
		assertEquals(2000.0, booking.getFarePaid());

		// Verify interactions
		verify(passengerDAO).savePassenger(passenger);
		verify(bookingDAO).saveBooking(any(Booking.class));
		verify(flightDAO).updateAvailableSeats("F001", -2);
	}

	@Test
	void testCalculateRevenue_usesDAO() {
		when(bookingDAO.calculateRevenue()).thenReturn(25000.0);

		double revenue = bookingService.calculateRevenue();

		assertEquals(25000.0, revenue); 
		verify(bookingDAO).calculateRevenue();
	}

	@Test
	void testCountBookingsByStatus() {
		when(bookingDAO.countBookingsByStatus("CONFIRMED")).thenReturn(5);

		int count = bookingService.confirmedCount();

		assertEquals(5, count);
		verify(bookingDAO).countBookingsByStatus("CONFIRMED");
	}

	@Test
	void testCancelBooking_callsDAO() {
		bookingDAO.cancelBooking("B001"); 

		verify(bookingDAO).cancelBooking("B001");
	}
}
