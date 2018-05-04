RyanAir Test 
Developed By Fidel C. Garbajosa. 04-May-2018
--------------------------------------------

1) BUILDING THE APPLICATION

You can build as it's stated below:

1- In the app directory, run this command and the war file will be generated in the 'target' directory (if you want to execute the test remove the '-DskipTests' but it will take          
more time to package):

mvn clean package -DskipTests

2.- Copy the war file ('RyanAirTest.war') to the 'webapps' directory in Tomcat application:

3.- Start Tomcat server (in my case, using Windows 10, I have to execute 'Startup.bat' in 'bin' directory on Tomcat..)

4.- The application should work introducing this on the navigator:

http://localhost:8080/RyanAirTest/interconnections?departure=DUB&arrival=WRO&departureDateTime=2018-06-04T18:00&arrivalDateTime=2018-06-06T22:00

2) GENERAL STRUCTURE OF THE APP

This application works under Spring Boot and uses Sonarqube 7.1 with PostgresSQL to do the testing coverage. 

1.- Class definition:

'Application' is the one that runs the app. It's written in order to generate a war file.
'RestClient' is the Rest web service client to get the info from RyanAir
'FlightGetter' is the business logic class. It calls methods from 'RestClient'. The main methods on 'FlightGetter' are these two:
    
    Method 'getDirectFlights' returns every direct flight according to the locations, time restrictions and routes.
    Method 'getOneStopFlights' returns every 1 stop flight according to the locations, time restrictions and routes.

'ResultsFromToController' defines the Rest web service server to gather the requests to the app.
It calls methods from 'FlightGetter' and 'RestClient'.
  
The rest of the classes define the Json structure needed by 'RestClient' and 'RestClient'

3) TESTING

1.- It's written an integration test named 'ResultsFromToControllerTest' which has a coverture over 90%

Best Regards

Fidel C. Garbajosa