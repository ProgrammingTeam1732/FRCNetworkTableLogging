# FIRST FRC Team 1732's Networktables Data Logger

Developed for the 2017 season to more easily view shooter velocity graphs.

It uses the WPI Libraries's Networktables code in java.
WPILibrary: http://first.wpi.edu/

Steps to use this:

1. Download and extract the ZIP file above.

2. Save your team number in the "config" file on the first line.

3. To log values from the Networktables, save the key Strings for those values on seperate lines below the team number

4. For further clarification, look inside the "example_config" file inside the zip.

5. Run this by using the batch file inside the zip.

Make sure you have java installed.

This code was written for a DriverStation NetworkTables client running Windows 10. It will almost definetly not work on other operating systems, but you can try.

How values are saved

1. Inside the extracted zip, there is a folder claled "logfiles"

2. This folder contains all the csv files the logger has saved

3. Each time the logger connects to the Networktables, a new csv log file is created.

4. To run this, use the command line or, if on windows, just run the batch file inside the folder.

5. To view how the log file is formatted, inside the extracted zip folder, click on the "log_file_format" file.
