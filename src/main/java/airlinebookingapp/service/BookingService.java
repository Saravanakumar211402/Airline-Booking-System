package airlinebookingapp.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import airlinebookingapp.dao.FlightDAO;
import airlinebookingapp.dao.BookingDAO;
import airlinebookingapp.dao.PassengerDAO;
import airlinebookingapp.exception.BookingNotFoundException;
import airlinebookingapp.exception.FlightFullException;
import airlinebookingapp.model.Booking;
import airlinebookingapp.model.Flight;
import airlinebookingapp.model.Passenger;


public class BookingService {

	// Data Storage
	List<Booking> bookingList = new ArrayList<>();
	HashMap<String, Booking> bookingId = new HashMap<String, Booking>();
	protected  PassengerDAO passengerDAO = new PassengerDAO();
	protected  BookingDAO bookingDAO = new BookingDAO();
	protected FlightDAO flightDAO = new FlightDAO();

	// Creating Booking
	public synchronized Booking createBooking(Flight flight, Passenger passenger, int seats)
			throws FlightFullException {

		if (!flight.bookSeat(seats)) {
			throw new FlightFullException("Sorry only " + flight.getAvailableSeats() + " Available!");
		}
		String timeStamp = java.time.LocalDate.now().toString();
		Booking booking = new Booking(passenger, flight, seats, timeStamp);

		passengerDAO.savePassenger(passenger);
		bookingDAO.saveBooking(booking);
		boolean updated = flightDAO.updateAvailableSeats(flight.getFlightId(), -seats);
		if (!updated) {
			throw new FlightFullException("Not enough seats available in DB!");
		}

		return booking; 
	}
	
	
	// for junit test without DB using list memory
	public synchronized Booking createBookingTest(Flight flight, Passenger passenger, int seats)
			throws FlightFullException {

		if (!flight.bookSeat(seats)) {
			throw new FlightFullException("Sorry only " + flight.getAvailableSeats() + " Available!");
		}
		String timeStamp = java.time.LocalDate.now().toString();
		Booking booking = new Booking(passenger, flight, seats, timeStamp);
		bookingList.add(booking);
		bookingId.put(booking.generateBookingId(), booking);
		return booking;
	}

	public Booking searchByBookingId(String bookingId) throws BookingNotFoundException {
		return bookingDAO.findBookingById(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));
	}

	public void cancelBooking(Booking booking, String bookingId) {
		bookingDAO.cancelBooking(bookingId);
		flightDAO.updateAvailableSeats(booking.getFlight().getFlightId(), booking.getSeats());
		booking.setStatus("CANCELLED");
	}

	public int confirmedCount() {
		return bookingDAO.countBookingsByStatus("CONFIRMED");
	}

	public int cancelledCount() {
		return bookingDAO.countBookingsByStatus("CANCELLED");
	}

	public double calculateRevenue() {
		return bookingDAO.calculateRevenue();
	}

	// for getting all confirmed booking
	public List<Booking> getAllConfirmedBooking() {
		List<Booking> confirmedBooking = bookingList.stream().filter(b -> b.getStatus().equalsIgnoreCase("CONFIRMED"))
				.collect(Collectors.toList());
		return confirmedBooking;
	}

	public List<Booking> getBookingByPassenger(String paasnegerId) {
		List<Booking> passengerDetails = bookingList.stream()
				.filter(b -> b.getPassenger().getPassengerId().equalsIgnoreCase(paasnegerId))
				.collect(Collectors.toList());
		return passengerDetails;
	}

	// MultiThreading
	public synchronized Booking createBookingConcurrent(Flight flight, Passenger passenger, int seats, String bookDate)
			throws FlightFullException {

		if (flight.getAvailableSeats() < seats) {
			throw new FlightFullException("Not enough seats available!");
		}
		flight.setAvailableSeats(flight.getAvailableSeats() - seats);

		Booking booking = new Booking(passenger, flight, seats, bookDate);
		bookingList.add(booking);
		return booking;
	}

}
