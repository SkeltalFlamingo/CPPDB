
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// this class provides the methods that interact with the MySQL database

public class DBManager {
	
	private Connection conn;
	
	DBManager(String dbHost, String dbName, String dbUser, String dbPassword) {
		
		String dbUrl = "jdbc:mysql://" + dbHost + "/" + dbName;
		
		// try to make a connection to the database
		try {
			// this shouldn't be necessary for newer versions of Java, but to keep with the lecture slides, I've included it.
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			System.out.println("Trying connection with " + dbUrl + ", " + dbUser + ", Password Hidden");
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			
			System.out.println("Connection successful");
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	// operations on table bus
	public int deleteBus(String busID) {
		
			String queryText = "DELETE FROM Bus WHERE busID = " + busID;
			
		try {
			
			Statement statement = conn.createStatement();
			return statement.executeUpdate(queryText);
			
		} catch (SQLException e) {
			
			return -1;
		}
	}
	
	public int addBus(String busID, String model, String year) {
		
		String queryText = "INSERT INTO bus VALUES (" + busID + ", '" + model +"', " + year + ")";
		
		try {
			
			Statement statement = conn.createStatement();
			return statement.executeUpdate(queryText);
			
		} catch (SQLException e) {
			return -1;
		}
	}
	
	// operations on table driver
	public int addDriver(String driverName, String DriverTelephoneNumber) {
		
		String queryText = "INSERT INTO driver VALUES ('" + driverName + "', " + DriverTelephoneNumber + ")";
		
		try {
			
			Statement statement = conn.createStatement();
			return statement.executeUpdate(queryText);
			
		} catch (SQLException e) {
			return -1;
		}

		
	}
	
	// operations on trip schedules
	// date format YYYY-MM-DD
	public String getDriverSchedule(String driverName, String date) {

		String out = "";
		String queryText = "Select TripNumber, ScheduledStartTime "
				+ "From tripoffering "
				+ "Where DriverName = '"+ driverName +"' AND date = '" + date +"'";
		
		try {
			
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(queryText);
			
		    while(results.next()){
		        for(int i = 1;i<=2;i++){
		            out +=results.getString(i);
		            out += "                ";
		        }
	            out += "\n";
		    }
			
		} catch (SQLException e) {
			return "bad perams";
		}

		return out;
		
	}

	public String getStops(String tripNo) {

		String out = "";
		String queryText = "Select SequenceNumber, StopAddress, DrivingTime\n"
				+ "From tripstopinfo, stop\n"
				+ "Where stop.StopNumber = tripstopinfo.StopNumber AND TripNumber = " + tripNo + "\n"
				+ "Order By SequenceNumber";
		
		try {
			
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(queryText);
			
		    while(results.next()){
		        for(int i = 1;i<=3;i++){
		            out +=results.getString(i);
		            out += "                ";
		        }
	            out += "\n";
		    }
			
		} catch (SQLException e) {
			return "bad perams";
		}

		return out;
		

	}

	public String getTripsToFrom(String startLocationName, String endLocationName) {

		String out = "";
		String queryText = "Select ScheduledArrivalTime, ScheduledStartTime, trip.tripNumber\n"
				+ "From tripoffering, trip\n"
				+ "Where tripoffering.TripNumber = trip.TripNumber AND StartLocationName = '" + startLocationName + "' AND DestinationName = '" + endLocationName + "'\n"
				+ "Order By ScheduledArrivalTime";
		
		try {
			
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(queryText);
			
		    while(results.next()){
		        for(int i = 1;i<=3;i++){
		            out +=results.getString(i);
		            out += "                ";
		        }
	            out += "\n";
		    }
			
		} catch (SQLException e) {
			return "bad perams";
		}

		return out;
	}

	public int deleteTripOffering(String tripNo, String date, String scheduledStartTime) {
		
		String queryText = "DELETE FROM tripoffering\n"
				+ "WHERE TripNumber = " + tripNo + " AND date = '" + date + "' AND ScheduledStartTime = '" + scheduledStartTime + "'";
		
		try {
			
			Statement statement = conn.createStatement();
			return statement.executeUpdate(queryText);
			
		} catch (SQLException e) {
			return -1;
		}
	}

	public int addTripOffering(String tripNo, String date, String scheduledStartTime, String scheduledArrivalTime, String drivername, String busID) {
		
		String queryText = "INSERT INTO tripoffering VALUES "
				+ "(" + tripNo + ", '" + date + "', '" + scheduledStartTime + "', '" + scheduledArrivalTime + "', '" + drivername + "', " + busID +")";
		
		try {
			
			Statement statement = conn.createStatement();
			return statement.executeUpdate(queryText);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	
	}

	public int changeTripOfferingDriver(String tripNo, String date, String scheduledStartTime, String driverName) {
		
		String queryText = "UPDATE tripoffering\n"
				+ "SET DriverName = '" + driverName + "'\n"
				+ "WHERE TripNumber = " + tripNo + " AND date = '" + date + "' AND ScheduledSTartTime = '" + scheduledStartTime+ "'";
		
		try {
			
			Statement statement = conn.createStatement();
			return statement.executeUpdate(queryText);
			
		} catch (SQLException e) {
			return -1;
	
		}
	}

	public int changeTripOfferingBuss(String tripNo, String date, String scheduledStartTime, String busID) {
		
		String queryText = "UPDATE tripoffering\n"
				+ "SET BusID = " + busID + "\n"
				+ "WHERE TripNumber = " + tripNo + " AND date = '" + date + "' AND ScheduledSTartTime = '" + scheduledStartTime+ "'";
		
		try {
			
			Statement statement = conn.createStatement();
			return statement.executeUpdate(queryText);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
	
		}
	
	}
}
