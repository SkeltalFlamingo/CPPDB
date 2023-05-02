// this is the driver class for the Transit Manager APP designed for Lab4

public class TransitManager {
	
	
	
	public static void main (String[] args) {
		
		
		// create DB manager
		DBManager manager = new DBManager("localhost:3306", "lab4", "JDBC", "tigerblood");
		
		System.out.println(manager.changeTripOfferingBuss("3", "2008-10-08", "12:33:09", "8"));
		
		// create messenger
		Messenger hermes = new Messenger(manager);
		
		// start messenger
		hermes.start();
		
	}
}
