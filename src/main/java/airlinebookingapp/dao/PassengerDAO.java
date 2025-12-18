package airlinebookingapp.dao;

import airlinebookingapp.model.Passenger;
import airlinebookingapp.util.DBConnection;

import java.sql.*;

public class PassengerDAO {
	public void savePassenger(Passenger passenger) {
        String sql = "INSERT INTO passengers (passenger_id, name, email, phone) " +
                     "VALUES (?, ?, ?, ?) ON CONFLICT (passenger_id) DO NOTHING";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, passenger.getPassengerId());
            pstmt.setString(2, passenger.getName());
            pstmt.setString(3, passenger.getEmail());
            pstmt.setInt(4, passenger.getPhone());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
