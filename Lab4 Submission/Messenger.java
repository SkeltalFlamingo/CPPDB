
// handles providing user with console prompts, and processing user input
import java.util.Scanner;

public class Messenger {

	Scanner userIn;

	private boolean running;
	private DBManager manager;

	private Menu activeMenu;

	private Menu mainMenu;
	private Menu tripsMenu;
	private Menu editTripsMenu;
	private Menu driversMenu;
	private Menu bussesMenu;

	private String backText;
	private promptReaction backReaction;

	Messenger(DBManager manager) {
		this.manager = manager;
		running = false;

		// define back option features
		backText = "Back to previous menu";
		backReaction = () -> activeMenu = activeMenu.getParent();

		// setup mainMenu
		String[] mainMenuPrompts = { "Manage Trips", "Manage Drivers", "Manage Busses", "Exit Application" };
		promptReaction[] mainMenuPromptReactions = { 
				() -> activeMenu = tripsMenu, 
				() -> activeMenu = driversMenu,
				() -> activeMenu = bussesMenu, 
				() -> System.exit(0), };
		mainMenu = new Menu(
				"Welcome to Transit Manager! What would you like to do?"
						+ "\nEnter the number for the option you want, then hit enter.",
				mainMenuPrompts, mainMenuPromptReactions);

		// setup tripsMenu
		String[] tripsMenuPrompts = { backText, "Find trips based on start and end destinations", "Edit trip offerings",
				"Display stops for a trip", };
		promptReaction[] tripsMenuPromptReactions = { 
				backReaction, 
				() -> displayFromToDialog(),
				() -> activeMenu = editTripsMenu, 
				() -> displayStopsDialog(), 
				};
		tripsMenu = new Menu("Trip Management Options:", tripsMenuPrompts, tripsMenuPromptReactions, mainMenu);

		// setup editTripsMenu
		String[] editTripsMenuPrompts = { backText, "Delete Trip Offering", "Add Trip Offering",
				"Change Driver for a trip", "change Bus for a trip" };
		promptReaction[] editTripsMenuPromptReactions = { 
				backReaction, 
				() -> deleteTripOfferingDialog(),
				() -> addTripOfferingDialog(), 
				() -> changeTripOfferingDriverDialog(),
				() -> changeTripOfferingBusDialog(), 
				};
		editTripsMenu = new Menu("Edit Trip Options:", editTripsMenuPrompts, editTripsMenuPromptReactions, tripsMenu);

		// setup driversMenu
		String[] driversMenuPrompts = { backText, "Add driver", "Display a driver's schedule", };
		promptReaction[] driversMenuPromptReactions = { 
				backReaction, 
				() -> addDriverDialog(),
				() -> getDriverScheduleDialog(), 
				};
		driversMenu = new Menu("Driver Management Options:", driversMenuPrompts, driversMenuPromptReactions, mainMenu);

		// setup bussesMenu
		String[] bussesMenuPrompts = { backText, "Add bus", "Delete Buss", };
		promptReaction[] bussesMenuPromptReactions = { 
				backReaction, 
				() -> addBusDialog(),
				() -> deleteBusDialog(), 
				};
		bussesMenu = new Menu("Bus Management Options:", bussesMenuPrompts, bussesMenuPromptReactions, mainMenu);

		// first menu should be mainMenu
		activeMenu = mainMenu;
	}

	public void start() {
		running = true;

		// Scanner for user input
		userIn = new Scanner(System.in);

		// loop to prompt and collect user input then react
		while (running) {

			// print prompt header
			System.out.print(activeMenu.getPrompt());

			// user input should be a integer for menu selection
			int responseCode = userIn.nextInt();
			userIn.nextLine(); // consume remaining newline character

			activeMenu.handleResponse(responseCode);

		}

		userIn.close();
	}

	public boolean isRunning() {
		return running;
	}

	// menu item dialogs
	public void deleteBusDialog() {

		System.out.println("----------------------------------------------------\n"
				+ "Please type the trip Number of the bus you would like to delete. to cancel, type c"
				+ "\n----------------------------------------------------");

		boolean validInput = false;
		while (!validInput) {
			String input = userIn.nextLine();

			if (input.equals("c")) {
				validInput = true;
			} else if (manager.deleteBus(input) > 0) {
				System.out.println("Deletion Successful!");
				validInput = true;
			} else {
				System.out.println("I'm sorry, that Bus ID number doesn't exist");
			}

		}
	}

	public void addBusDialog() {

		boolean validInput = false;
		int ID = 0;
		String Model = "";
		int	Year = 0;

		// get bus ID
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the ID for the bus you'd like to add"
					+ "\n----------------------------------------------------");

			try {
				ID = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("I'm sorry, but the ID must be an integer.");
			}
		}
		validInput = false;

		// get bus Model
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the Model for the bus you'd like to add"
					+ "\n----------------------------------------------------");

			
			Model = userIn.nextLine();
			validInput = true;
		}
		validInput = false;
		
		// get bus Year
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the year for the bus you'd like to add"
					+ "\n----------------------------------------------------");

			
			try {
				Year = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("I'm sorry, but the Year must be an integer.");
			}

		}
		validInput = false;

		// confirm addition
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Are you sure you want to add this Bus [Y/N]:"
					+ "ID: " + ID + " Model: " + Model + " Year: " + Year
					+ "\n----------------------------------------------------");


			String decision = userIn.nextLine();
			
			if (decision.equalsIgnoreCase("y")) {
				if (manager.addBus(String.valueOf(ID), Model, String.valueOf(Year)) > 0) {
					System.out.println("Bus added");
					
				} else System.out.println("Failed to add bus");
				
				
				validInput = true;
			}
			else if (decision.equalsIgnoreCase("n")) {
				System.out.println("cancelling");
				validInput = true;
				return;
			}
			else {
				System.out.println("please type y or n");
			}

		}
		
	}

	public void addDriverDialog() {

		boolean validInput = false;
		String name = "";
		int phoneNumber = -1;

		// get name
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
				+ "Please type the Name of the Driver you'd like to add"
				+ "\n----------------------------------------------------");

					
			name = userIn.nextLine();
			validInput = true;
		}
		validInput = false;
				
		
		// get phone number
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the phone number for the driver you'd like to add"
					+ "\n----------------------------------------------------");

			try {
				phoneNumber = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("I'm sorry, but the ID must be an integer.");
			}
		}
		validInput = false;

		
		// confirm addition
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Are you sure you want to add this Driver [Y/N]:"
					+ "Name: " + name + " Phone: " + phoneNumber
					+ "\n----------------------------------------------------");


			String decision = userIn.nextLine();
			
			if (decision.equalsIgnoreCase("y")) {
				if (manager.addDriver(name, String.valueOf(phoneNumber)) > 0) {
					System.out.println("Driver added");
					
				} else System.out.println("Failed to add driver");
				
				
				validInput = true;
			}
			else if (decision.equalsIgnoreCase("n")) {
				System.out.println("cancelling");
				validInput = true;
				return;
			}
			else {
				System.out.println("please type y or n");
			}

		}

	}

	public void getDriverScheduleDialog() {

		boolean validInput = false;
		String name = "";
		String date = "";

		// get name ID
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the name of the driver"
					+ "\n----------------------------------------------------");

			try {
				name = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Your input failed. please try again.");
			}
		}
		validInput = false;
		
		// get year
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the date of the driver in this exact format: YYYY-MM-DD"
					+ "\n----------------------------------------------------");

			try {
				date = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Your input failed. please try again.");
			}
		}
		validInput = false;
		
		String schedule = manager.getDriverSchedule(name, date);
		if (!schedule.equals("bad perams")) {
			System.out.println("Trip Number:     Scheduled Start Time:");
			System.out.print(schedule);
		} else System.out.println("invalid driver/date combo");
		
		
	}
	
	public void displayStopsDialog() {

		boolean validInput = false;
		int tripNo = -1;
		String date = "";

		// get trip number
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the trip number"
					+ "\n----------------------------------------------------");

			try {
				tripNo = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("Your input failed. please try again.");
			}
		}
		validInput = false;
		
		
		String schedule = manager.getStops(String.valueOf(tripNo));
		if (!schedule.equals("bad perams")) {
			System.out.println("Sequence Number: Stop Address:    Driving Time:");
			System.out.print(schedule);
		} else System.out.println("invalid driver/date combo");
		
		

	}

	public void displayFromToDialog() {

		boolean validInput = false;
		String startLocation = "";
		String endLocation = "";
		String date = "";

		// get start location
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the starting location"
					+ "\n----------------------------------------------------");

			try {
				startLocation = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Your input failed. please try again.");
			}
		}
		validInput = false;
		
		// get start location
				while (!validInput) {
					System.out.println("----------------------------------------------------\n"
							+ "Please type the destination"
							+ "\n----------------------------------------------------");

					try {
						endLocation = userIn.nextLine();
						validInput = true;
					} catch(Exception e) {
						System.out.println("Your input failed. please try again.");
					}
				}
				validInput = false;
		
		
		String trips = manager.getTripsToFrom(startLocation, endLocation);
		if (!trips.equals("bad perams")) {
			System.out.println("Scheduled Arrival Time: Scheduled Start Time:   Trip Number:");
			System.out.print(trips);
		} else System.out.println("invalid driver/date combo");
		
		

	
	}

	public void deleteTripOfferingDialog() {
		boolean validInput = false;
		int tripNumber = -1;
		String date = "";
		String scheduledStartTime = "";

		// get tripNumber
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
				+ "Please type the Trip Number of the Trip Offering you'd like to delete"
				+ "\n----------------------------------------------------");

					
			try {
				tripNumber = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("Your input must be an integer.");
			}
		}
		validInput = false;
				
		
		// get date
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the date of the Trip Offering you'd like to delete. Format: YYYY-MM-DD"
					+ "\n----------------------------------------------------");

			try {
				date = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Invalid Input. please try again.");
			}
		}
		validInput = false;
		
		// get start time
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the Scheduled Start Time of the Trip Offering you'd like to delete. Format: HH:MM:SS"
					+ "\n----------------------------------------------------");

			try {
				scheduledStartTime = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Invalid Input. please try again.");
			}
		}
		validInput = false;
		
		// confirm addition
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Are you sure you want to delete this Trip Offering? [Y/N]:"
					+ "Trip Number: " + tripNumber + " date: " + date + " Scheduled Start Time: " + scheduledStartTime
					+ "\n----------------------------------------------------");


			String decision = userIn.nextLine();
			
			if (decision.equalsIgnoreCase("y")) {
				if (manager.deleteTripOffering(String.valueOf(tripNumber), date, scheduledStartTime) > 0) System.out.println("Trip Offering Deleted");
				else System.out.println("Failed to delete Trip Offering");
				
				
				validInput = true;
			}
			else if (decision.equalsIgnoreCase("n")) {
				System.out.println("cancelling");
				validInput = true;
				return;
			}
			else {
				System.out.println("please type y or n");
			}

		}
	}
 
	public void addTripOfferingDialog() {
		boolean validInput = false;
		int tripNumber = -1;
		String date = "";
		String driverName = "";
		String scheduledStartTime = "";
		String scheduledArrivalTime = "";
		int busID = -1;

		// get tripNumber
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
				+ "Please type the Trip Number of the new Trip Offering."
				+ "\n----------------------------------------------------");

					
			try {
				tripNumber = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("Your input must be an integer.");
			}
		}
		validInput = false;
				
		
		// get date
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the date of the new Trip Offering. Format: YYYY-MM-DD"
					+ "\n----------------------------------------------------");

			try {
				date = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Invalid Input. please try again.");
			}
		}
		validInput = false;
		
		// get start time
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the Scheduled Start Time of the new Trip Offering. Format: HH:MM:SS"
					+ "\n----------------------------------------------------");

			try {
				scheduledStartTime = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Invalid Input. please try again.");
			}
		}
		validInput = false;
		
		// get arrival time
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
				+ "Please type the Scheduled Arrival Time of the new Trip Offering. Format: HH:MM:SS"
				+ "\n----------------------------------------------------");

			try {
				scheduledArrivalTime = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Invalid Input. please try again.");
			}
		}
		validInput = false;
		
		// get bus ID
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the ID for the bus you'd like to add"
					+ "\n----------------------------------------------------");

			try {
				busID = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("I'm sorry, but the ID must be an integer.");
			}
		}
		validInput = false;

		
		// get name
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
				+ "Please type the Name of the Driver you'd like to add"
				+ "\n----------------------------------------------------");


			driverName = userIn.nextLine();
			validInput = true;
		}
		validInput = false;
		
		// confirm addition
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Are you sure you want to create this Trip Offering? [Y/N]:"
					+ "\nTrip Number: " + tripNumber + " date: " + date + " Scheduled Start Time: " + scheduledStartTime
					+ "\nScheduled Arrival Time: " + scheduledArrivalTime + " driverName: " + driverName
					+ "\n----------------------------------------------------");


			String decision = userIn.nextLine();
			
			if (decision.equalsIgnoreCase("y")) {
				if (manager.addTripOffering(String.valueOf(tripNumber), date, scheduledStartTime, scheduledArrivalTime, driverName, String.valueOf(busID)) > 0) System.out.println("Trip Offering Deleted");
				else System.out.println("Failed to delete Trip Offering");
				
				
				validInput = true;
			}
			else if (decision.equalsIgnoreCase("n")) {
				System.out.println("cancelling");
				validInput = true;
				return;
			}
			else {
				System.out.println("please type y or n");
			}

		}

	}

	public void changeTripOfferingDriverDialog() {

		boolean validInput = false;
		int tripNumber = -1;
		String date = "";
		String scheduledStartTime = "";
		String driverName = "";

		// get tripNumber
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
				+ "Please type the Trip Number of the Trip Offering you'd like to udate"
				+ "\n----------------------------------------------------");

					
			try {
				tripNumber = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("Your input must be an integer.");
			}
		}
		validInput = false;
				
		
		// get date
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the date of the Trip Offering you'd like to update. Format: YYYY-MM-DD"
					+ "\n----------------------------------------------------");

			try {
				date = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Invalid Input. please try again.");
			}
		}
		validInput = false;
		
		
		// get start time
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the Scheduled Start Time of the Trip Offering you'd like to update. Format: HH:MM:SS"
					+ "\n----------------------------------------------------");

			try {
				scheduledStartTime = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Invalid Input. please try again.");
			}
		}
		validInput = false;
		
		
		// get name
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
				+ "Please type the New Driver Name"
				+ "\n----------------------------------------------------");


			driverName = userIn.nextLine();
			validInput = true;
		}
		validInput = false;
		

		// confirm addition
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Are you sure you want to change this Trip Offering's driver? [Y/N]:"
					+ "Trip Number: " + tripNumber + " date: " + date + " Scheduled Start Time: " + scheduledStartTime + "New Driver: " + driverName
					+ "\n----------------------------------------------------");


			String decision = userIn.nextLine();
			
			if (decision.equalsIgnoreCase("y")) {
				if (manager.changeTripOfferingDriver(String.valueOf(tripNumber), date, scheduledStartTime, driverName) > 0) System.out.println("Trip Offering Driver Updated");
				else System.out.println("Failed to update Trip Offering Driver");
				
				
				validInput = true;
			}
			else if (decision.equalsIgnoreCase("n")) {
				System.out.println("cancelling");
				validInput = true;
				return;
			}
			else {
				System.out.println("please type y or n");
			}

		}
	
	}

	public void changeTripOfferingBusDialog() {


		boolean validInput = false;
		int tripNumber = -1;
		String date = "";
		String scheduledStartTime = "";
		int busID = -1;

		// get tripNumber
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
				+ "Please type the Trip Number of the Trip Offering you'd like to update"
				+ "\n----------------------------------------------------");

					
			try {
				tripNumber = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("Your input must be an integer.");
			}
		}
		validInput = false;
				
		
		// get date
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the date of the Trip Offering you'd like to update. Format: YYYY-MM-DD"
					+ "\n----------------------------------------------------");

			try {
				date = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Invalid Input. please try again.");
			}
		}
		validInput = false;
		
		
		// get start time
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Please type the Scheduled Start Time of the Trip Offering you'd like to update. Format: HH:MM:SS"
					+ "\n----------------------------------------------------");

			try {
				scheduledStartTime = userIn.nextLine();
				validInput = true;
			} catch(Exception e) {
				System.out.println("Invalid Input. please try again.");
			}
		}
		validInput = false;
		
		
		// get bus
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
				+ "Please type the New bus ID"
				+ "\n----------------------------------------------------");

			try {
				busID = Integer.parseInt(userIn.nextLine());
				validInput = true;
			} catch(Exception e) {
				System.out.println("Bus ID needs to be an integer. please try again.");
			}
		}
		validInput = false;
		

		// confirm addition
		while (!validInput) {
			System.out.println("----------------------------------------------------\n"
					+ "Are you sure you want to change this Trip Offering's bus? [Y/N]:"
					+ "Trip Number: " + tripNumber + " date: " + date + " Scheduled Start Time: " + scheduledStartTime + " New Bus: " + busID
					+ "\n----------------------------------------------------");


			String decision = userIn.nextLine();
			
			if (decision.equalsIgnoreCase("y")) {
				if (manager.changeTripOfferingBuss(String.valueOf(tripNumber), date, scheduledStartTime, String.valueOf(busID)) > 0) 
					System.out.println("Trip Offering Bus Updated");
				else System.out.println("Failed to update Trip Offering Bus");
				
				
				validInput = true;
			}
			else if (decision.equalsIgnoreCase("n")) {
				System.out.println("cancelling");
				validInput = true;
				return;
			}
			else {
				System.out.println("please type y or n");
			}

		}
	
	
	}
}
