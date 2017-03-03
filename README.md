# FIRST FRC Team 1732's NetworkTable/SmartDashboard Data Logger

Developed for the 2017 season to more easily view shooter velocity graphs.

It uses the WPI Libraries's NetworkTable code. WPILibrary: http://first.wpi.edu/
It also uses opencsv. Opencsv: http://opencsv.sourceforge.net/

Steps to use this:

1. Download and extract the ZIP file above (click the file to view, click download in top right area)

2. Save your team number in the "Config" file on the first line. (The Config starts off as my team's config file as an example, feel free to delete all of the text inside  )

3. To log values from the Networktables, save the key Strings for those values on seperate lines below the team number, followed by the type of the key

4. For further clarification, look inside the "ExampleConfig" file inside the zip.

5. Run this by using the batch file inside the zip. (all this does is use the command line to do "java -jar Logger.jar")

Make sure you have java installed.

This code was written for a DriverStation NetworkTables client running Windows 10. It will almost definetly not work on other operating systems, but you can try.

How values are saved

1. Inside the extracted zip, there is a folder claled "LoggerFiles"

2. This folder contains all the csv files the logger has saved

3. Each time the logger connects to the Networktables, a new csv log file is created.

4. To run this, use the command line or, if on windows, just run the batch file inside the folder.

5. To view how the log file is formatted, inside the extracted zip folder, click on the "LogFileFormat" file.
