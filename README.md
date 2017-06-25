This is a command line Spring Boot Application to be used by the ETL team to load Custom Territory MapInfo tab files for Spatial Replatform project.

Usage:

java -jar spatialdataloader-0.0.1-SNAPSHOT.jar --data=<csv input> --ds=jdbc:postgresql://localhost/<dbname> --user=<user> --pwd=<password>

--data - The path to the csv file that contains the path to individual layers, custom layer tag, 2 digit state code if partitioned by state
--ds - Postgres datasource url
--user - Postgres username
--pwd - Postgres password
