package airlinebookingapp.model;

public class Flight {
	
	private String flightId;
	private String flightNumber;
	private String source;
	private String destination;
	private double price;
	private int totalSeats;
	private int availableSeats;
	
	public Flight(String flightId, String flightNumber, String source, String destination, double price, int totalSeats,
			int availableSeats) {
		this.flightId = flightId;
		this.flightNumber = flightNumber;
		this.source = source;
		this.destination = destination;
		this.price = price;
		this.totalSeats = totalSeats;
		this.availableSeats = availableSeats;
	} 
	
	//for junit testing
	public Flight() {}              
    

	//getters
	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public String getSource() {
		return source;
	}

	public String getDestination() {
		return destination;
	} 

	public double getPrice() {
		return price;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}
	
	
	public void setPrice(double price) {
		this.price = price;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	//booked
	public synchronized boolean bookSeat(int seats) {
		if (availableSeats>=seats) {
			availableSeats-=seats;
			return true;
		}
		return false;
	}
	
	//cancelled
	public synchronized void releaseSeat(int seats) {
		availableSeats+=seats;
	}

	@Override
	public String toString() {
		return "FlightId:" + flightId + " | FlightNumber:" + flightNumber + " | " + source
				+ " -> " + destination + " | Price:" + price + " | TotalSeats:" + totalSeats
				+ " | AvailableSeats:" + availableSeats;
	}
	
	
	
	
	
	
}
