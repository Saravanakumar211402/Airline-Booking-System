package airlinebookingapp.dao;


import airlinebookingapp.model.Booking;
import airlinebookingapp.util.DBConnection;

import java.sql.*;
import java.util.Optional;

public class BookingDAO {
	
	public void saveBooking(Booking booking) {
        String sql = "INSERT INTO bookings (booking_id, passenger_id, flight_id, seats, book_date, status, fare_paid) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, booking.getPassenger().getPassengerId());
            pstmt.setString(3, booking.getFlight().getFlightId());
            pstmt.setInt(4, booking.getSeats());
            pstmt.setString(5, booking.getBookDate());
            pstmt.setString(6, booking.getStatus());
            pstmt.setDouble(7, booking.getFarePaid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	 
	public Optional<Booking> findBookingById(String bookingId) {
        String sql = "SELECT b.booking_id, b.passenger_id, b.seats, b.book_date, b.status, b.fare_paid, " +
                "f.flight_id, f.flight_number " +
                "FROM bookings b " +
                "JOIN airplanes f ON b.flight_id = f.flight_id " +
                "WHERE LOWER(b.booking_id) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookingId.toLowerCase());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Booking booking = new Booking(
                    rs.getString("booking_id"),
                    rs.getString("passenger_id"),
                    null, // passenger details can be fetched separately
                    rs.getString("flight_id"),
                    rs.getInt("seats"),
                    rs.getString("book_date"),
                    rs.getString("status"),
                    rs.getDouble("fare_paid")
                );
                booking.getFlight().setFlightNumber(rs.getString("flight_number"));
                return Optional.of(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

	public void cancelBooking(String bookingId) {
        String sql = "UPDATE bookings SET status='CANCELLED' WHERE LOWER(booking_id)=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookingId.toLowerCase());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public int countBookingsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE status=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; 
    }

    public double calculateRevenue() {
        String sql = "SELECT SUM(fare_paid) FROM bookings WHERE status='CONFIRMED'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}
