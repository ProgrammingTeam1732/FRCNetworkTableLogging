import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Logger {

	public static void main(String[] args) {

		NetworkTable.setClientMode();
		NetworkTable.setTeam(1732);
		NetworkTable table = NetworkTable.getTable("SmartDashboard");

		boolean printedConnection = false;
		while (true) {
			if (!table.isConnected()) {
				if (!printedConnection) {
					System.out.println("Not connected");
					printedConnection = true;
				}
				// do stuff when not connected here
			} else {
				if (printedConnection) {
					System.out.println("Connected");
					printedConnection = false;
				}
				// do stuff when connected here

				// insert logging code

			}
		}
	}

}