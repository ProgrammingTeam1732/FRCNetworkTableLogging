import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Logger {

	// Final/Helper fields
	private static final String	ConfigFileName	= "Config";
	private static final String	tableKey		= "SmartDashboard";
	// private static final String networkIdentity = "networkIdentity";
	private static boolean	wasConnected		= true;
	private static boolean	waitingToConnect	= true;

	// Config fields / fields dependant on config fields
	private static int			teamNumber;
	private static String[]		keys;
	private static NetworkTable	table;

	// Logging fields
	// private static LogFile logfile = new LogFile();

	public static void main(String[] args) {
		System.out.println("Application starting");
		readConfigFile();
		initializeNetworkTableClient();

		while (!table.isConnected()) {}

		initializeLogging();

		while (true) {
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
		// NetworkTable.setNetworkIdentity(networkIdentity); needed?
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
		openNewLogFile();
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
		closeLogFile();
		openNewLogFile();
		waitingToConnect = false;
		wasConnected = true;
	}

	private static void whileDisconnected() {}

	private static void whileConnected() {
		writeToLogFile();
	}

	private static void closeLogFile() {}

	private static void openNewLogFile() {}

	private static void writeToLogFile() {

	}

}