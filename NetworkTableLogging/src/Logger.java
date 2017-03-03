import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Logger {

	// Final fields
	private static final String tableKey = "SmartDashboard";

	// Config fields / depends on config fields
	private static int			teamNumber;
	private static String[]		keys;
	private static NetworkTable	table;

	// Helper fields
	private static boolean	wasConnected		= true;
	private static boolean	waitingToConnect	= true;

	// Logging fields
	// private static LogFile logfile = new LogFile();

	public static void main(String[] args) {
		readConfigFile();
		initializeNetworkTableClient();

		// wait for connection (or end if code is canceled)
		while (!table.isConnected()) {
			if (Thread.interrupted())
				System.out.println("Program ending");
			return;
		}

		initializeLogging();

		// run until code is canceled
		while (!Thread.interrupted()) {
			if (!table.isConnected()) {
				if (wasConnected)
					afterDisconnected();
				whileDisconnected();
			} else {
				if (waitingToConnect)
					afterConnected();
				whileConnected();
			}
		}

		System.out.println("Program ending, saving current data");
		saveLogFile();
	}

	/**
	 * Reads in the data from the config file
	 */
	private static void readConfigFile() {
		teamNumber = 1732; // read from config
	}

	/**
	 * Initialized NetworkTable Client
	 */
	private static void initializeNetworkTableClient() {
		System.out.println("Disconnected, initializing NetworkTable Client");

		NetworkTable.setClientMode();
		// NetworkTable.setNetworkIdentity("Name"); needed?
		NetworkTable.setTeam(teamNumber);
		NetworkTable.initialize();
		table = NetworkTable.getTable(tableKey);

		// Print status
		System.out.println("NetworkTable Client initialized");
		System.out.println("Disconnected, logging not initialized");
	}

	/**
	 * Initialize logging code<br>
	 * Executes after connected to the NetworkTable for the first time
	 */
	private static void initializeLogging() {
		System.out.println("Connected, initializing logging");

		waitingToConnect = false;
		wasConnected = true;

		System.out.println("Logging initialized");
	}

	/**
	 * Executes after disconnecting from the NetworkTable
	 */
	private static void afterDisconnected() {
		System.out.println("Disconnected");

		wasConnected = false;
		waitingToConnect = true;
	}

	/**
	 * Executes after connecting to the NetworkTable
	 */
	private static void afterConnected() {
		System.out.println("Connected");

		saveLogFile();
		newLogFile();

		waitingToConnect = false;
		wasConnected = true;
	}

	private static void whileDisconnected() {}

	private static void whileConnected() {}

	private static void saveLogFile() {}

	private static void newLogFile() {}

}