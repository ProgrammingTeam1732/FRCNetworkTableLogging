import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import com.opencsv.CSVWriter;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Logger {

	// Helper fields
	private static boolean	wasConnected	= true;
	private static long		startTime;

	// Config fields
	private static final String	configFileName		= "Config.txt";
	private static final String	loggerFilesFolder	= "LoggerFiles";
	private static final String	tableKey			= "SmartDashboard";
	// private static final String networkIdentity = "networkIdentity";
	private static int			teamNumber;
	private static String[]		keys;
	private static String[]		types;
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
		System.out.println("Reading config file");
		ArrayList<String> keyList = new ArrayList<String>(0);
		ArrayList<String> typeList = new ArrayList<String>(0);
		try (BufferedReader br = new BufferedReader(new FileReader(configFileName))) {
			String line = br.readLine();
			if (line != null) {
				teamNumber = Integer.parseInt(line);
			} else {
				return;
			}
			String line1;
			String line2;
			while ((line1 = br.readLine()) != null && (line2 = br.readLine()) != null) {
				keyList.add(line1.trim());
				typeList.add(line2.trim().toLowerCase());
			}
		} catch (FileNotFoundException e) {
			System.err.print("Error opening config file");
			e.printStackTrace();
		} catch (IOException e1) {
			System.out.println("Error reading config file");
			e1.printStackTrace();
		}
		keyList.add("Time");
		keyList.toArray(Logger.keys);
		typeList.toArray(Logger.types);
		System.out.println("Config file:");
		System.out.println("Team number: " + teamNumber);
		System.out.println("Keys: " + Arrays.toString(keys));
		System.out.println("Ty[es: " + Arrays.toString(types));
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
			System.err.println("Error closing csvWriter");
			e.printStackTrace();
		}
	}

	private static void openNewLogFile() {
		try {
			csvWriter = new CSVWriter(new FileWriter(getCSVFileName()));
		} catch (IOException e) {
			System.err.println("Error opening csvWriter");
			e.printStackTrace();
		}
		csvWriter.writeNext(keys);
		startTime = System.currentTimeMillis();
	}

	private static void writeToLogFile() {
		String[] values = new String[keys.length];
		for (int i = 0; i < values.length - 1; i++) {
			if (types[i].equals("string")) {
				values[i] = table.getString(keys[i], "null");
			} else if (types[i].equals("number")) {
				values[i] = "" + table.getNumber(keys[i], 0);
			} else if (types[i].equals("boolean")) {
				values[i] = "" + table.getBoolean(keys[i], false);
			}
		}
		values[keys.length - 1] = "" + getElapsedTime();
		csvWriter.writeNext(values);
	}

	private static final String logFront = "Log:";

	private static String getCSVFileName() {
		return loggerFilesFolder + "\\" + logFront + dtf.format(LocalDateTime.now()) + ".csv";
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