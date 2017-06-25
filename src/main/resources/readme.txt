This application is a command line application meant to be called from FME to load Custom Territory layers

-- Java Execution Command
java -jar spatialdataloader-0.0.1-SNAPSHOT.jar --data=c:\temp\datainput.csv --ds=jdbc:postgresql://localhost/CustomTerritoryDB --user=postgres --pwd=password

--data - The path to the csv file that contains the path to individual layers, custom layer tag, 2 digit state code if partitioned by state
--ds - Postgres datasource url
--user - Postgres username
--pwd - Postgres password