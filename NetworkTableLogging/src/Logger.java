import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.opencsv.CSVWriter;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Logger {

	// Helper fields
	private static boolean	wasConnected	= true;
	private static long		startTime;

	// Config fields
	private static final String	ConfigFileName		= "Config.txt";
	private static final String	LoggerFilesFolder	= "LoggerFiles";
	private static final String	tableKey			= "SmartDashboard";
	// private static final String networkIdentity = "networkIdentity";
	private static int			teamNumber;
	private static String[]		keys;
	private static NetworkTable	table;

	// Logging fields
	private static final DateTimeFormatter	dtf	= DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss");
	private static CSVWriter				csvWriter;

	public static void main(String[] args) {
		System.out.println("Application starting");
		readConfigFile();
		initializeNetworkTableClient();

		while (!table.isConnected()) {}

		initializeLogging();

		while (true) {
			if (!table.isConnected()) {
				if (wasConnected())
					afterDisconnected();
				whileDisconnected();
			} else {
				if (wasWaitingToConnect())
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
		keys = new String[] { "A", "B", "C", "Time" };
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
		System.out.println("Connected, initializing logging (opening new log file)");
		openNewLogFile();
		wasConnected = true;
		System.out.println("Logging initialized");
	}

	/**
	 * Executes after disconnecting from the NetworkTable
	 */
	private static void afterDisconnected() {
		System.out.println("Disconnected, closing log file");
		closeLogFile();
		wasConnected = false;
	}

	/**
	 * Executes after connecting to the NetworkTable
	 */
	private static void afterConnected() {
		System.out.println("Connected, opening new log file");
		openNewLogFile();
		wasConnected = true;
	}

	private static void whileDisconnected() {}

	private static void whileConnected() {
		writeToLogFile();
	}

	private static void closeLogFile() {
		try {
			if (csvWriter != null)
				csvWriter.close();
		} catch (IOException e) {
			System.out.println("Error closing csvWriter");
			e.printStackTrace();
		}
	}

	private static void openNewLogFile() {
		try {
			csvWriter = new CSVWriter(new FileWriter(getCSVFileName()));
		} catch (IOException e) {
			System.out.println("Error opening csvWriter");
			e.printStackTrace();
		}
		csvWriter.writeNext(keys);
		startTime = System.currentTimeMillis();
	}

	private static void writeToLogFile() {
		String[] values = new String[keys.length];
		for (int i = 0; i < values.length - 1; i++) {
			values[i] = table.getString(keys[i], "0");
		}
		values[keys.length - 1] = "" + getElapsedTime();
		csvWriter.writeNext(values);
	}

	private static final String logFront = "Log:";

	private static String getCSVFileName() {
		return LoggerFilesFolder + "\\" + logFront + dtf.format(LocalDateTime.now()) + ".csv";
	}

	private static long getElapsedTime() {
		return System.currentTimeMillis() - startTime;
	}

	public static boolean wasWaitingToConnect() {
		return !wasConnected;
	}

	public static boolean wasConnected() {
		return wasConnected;
	}
}