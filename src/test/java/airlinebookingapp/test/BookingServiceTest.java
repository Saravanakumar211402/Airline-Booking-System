package airlinebookingapp.test;

import airlinebookingapp.exception.BookingNotFoundException;
import airlinebookingapp.exception.FlightFullException;
import airlinebookingapp.model.Booking;
import airlinebookingapp.model.Flight;
import airlinebookingapp.model.Passenger;
import airlinebookingapp.service.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//it is to check real logics 

class BookingServiceTest {

    private BookingService bookingService;

    @BeforeEach 
    void setUp() { 
        bookingService = new BookingService();
    } 
 
    @Test 
    void createBooking_failsWhenFlightHasNotEnoughSeats() { 
        BookingService service = new BookingService();

        Flight flight = new Flight();  //created constructor without arguements
        flight.setFlightId("F100");
        flight.setAvailableSeats(1);                 

        Passenger passenger = new Passenger("P100", "Test User", "test@", 6554); //created constructor without arguements
 
        assertThrows(FlightFullException.class,
                () -> service.createBooking(flight, passenger, 3));        // asks 3 seats
    }
     
    @Test
    void createBooking_successReturnsBooking() throws FlightFullException {
        BookingService service = new BookingService();

        Flight flight = new Flight();
        flight.setFlightId("F107");
        flight.setAvailableSeats(10);

        Passenger passenger = new Passenger("P101", "Test User", "test@", 898);

        Booking booking = service.createBookingTest(flight, passenger, 2);//seperate method using memory to test no DB

        assertNotNull(booking);
        assertEquals(2, booking.getSeats());
        assertEquals("P101", booking.getPassenger().getPassengerId());
        assertEquals("F107", booking.getFlight().getFlightId());
    }
    
    @Test
    void searchByBookingId_notFoundThrowsException() {
        BookingService service = new BookingService(); 

        assertThrows(BookingNotFoundException.class,
                () -> service.searchByBookingId("NON_EXISTING_ID"));      
    }
    
    @Test
    void cancelBooking_setsStatusCancelled() throws FlightFullException {
        BookingService service = new BookingService();

        Flight flight = new Flight();
        flight.setFlightId("F200");
        flight.setAvailableSeats(10);

        Passenger passenger = new Passenger("P200", "User", "user@", 89798);

        
        Booking booking = service.createBookingConcurrent(flight, passenger, 2, "2025-12-14");

        booking.setStatus("CONFIRMED");

        service.cancelBooking(booking, "B200");                              

        assertEquals("CANCELLED", booking.getStatus());
    }
    
    @Test
    void confirmedAndCancelledCount_callsDAO() {
        BookingService service = new BookingService();

        int confirmed = service.confirmedCount();
        int cancelled = service.cancelledCount();
        double revenue = service.calculateRevenue();

        assertTrue(confirmed >= 0);
        assertTrue(cancelled >= 0);
        assertTrue(revenue >= 0.0);
    }
    
    @Test
    void createBookingConcurrent_success_reducesSeatsAndAddsToList() throws FlightFullException {
        Flight flight = new Flight();
        flight.setFlightId("F001");
        flight.setAvailableSeats(5);

        Passenger passenger = new Passenger();
        passenger.setPassengerId("P001");

        String date = "2025-12-14";

        Booking booking = bookingService.createBookingConcurrent(flight, passenger, 3, date); 

        assertNotNull(booking);
        assertEquals(2, flight.getAvailableSeats());        
        assertEquals(3, booking.getSeats());
        assertEquals(passenger, booking.getPassenger());
        assertEquals(flight, booking.getFlight());
    }

    @Test
    void createBookingConcurrent_notEnoughSeats_throwsException() {
        Flight flight = new Flight();
        flight.setAvailableSeats(2);

        Passenger passenger = new Passenger();
        passenger.setPassengerId("P002");

        assertThrows(FlightFullException.class,
                () -> bookingService.createBookingConcurrent(flight, passenger, 3, "2025-12-14"));
    }

    @Test
    void getAllConfirmedBooking_returnsOnlyConfirmed() throws FlightFullException {
        Flight flight = new Flight();
        flight.setAvailableSeats(10);

        Passenger p1 = new Passenger();
        p1.setPassengerId("P1");
        Passenger p2 = new Passenger();
        p2.setPassengerId("P2");

        Booking b1 = bookingService.createBookingConcurrent(flight, p1, 2, "2025-12-14");
        Booking b2 = bookingService.createBookingConcurrent(flight, p2, 2, "2025-12-14"); 

        b1.setStatus("CONFIRMED");
        b2.setStatus("CANCELLED");   

        List confirmed = bookingService.getAllConfirmedBooking(); // uses bookingList

        assertEquals(1, confirmed.size());
        Booking only = (Booking) confirmed.get(0);
        assertEquals("CONFIRMED", only.getStatus());
    }

    @Test
    void getBookingByPassenger_filtersByPassengerId() throws FlightFullException {
        Flight flight = new Flight();
        flight.setAvailableSeats(10);

        Passenger p1 = new Passenger();
        p1.setPassengerId("PX");
        Passenger p2 = new Passenger(); 
        p2.setPassengerId("PY");

        Booking b1 = bookingService.createBookingConcurrent(flight, p1, 1, "2025-12-14");
        Booking b2 = bookingService.createBookingConcurrent(flight, p2, 1, "2025-12-14"); 

        b1.setStatus("CONFIRMED");
        b2.setStatus("CONFIRMED");

        List listForPX = bookingService.getBookingByPassenger("PX"); 

        assertEquals(1, listForPX.size());
        Booking result = (Booking) listForPX.get(0);
        assertEquals("PX", result.getPassenger().getPassengerId());
    }

    @Test
    void searchByBookingId_notFound_throws() {
        assertThrows(BookingNotFoundException.class,
                () -> bookingService.searchByBookingId("NO_SUCH_ID")); 
    }
}
