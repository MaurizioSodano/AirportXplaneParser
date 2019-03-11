# Airport Parser

What is this? Its an X-Plane 11 apt.dat Java parser. 
Inspired and ported in Java from a C# project https://github.com/njbrown09/XPlaneAirportParser

It could be used to parse an Xplane airport file (apt.dat)

## Instructions
compile with maven: mvn clean install

Run the following command to print the main airport features
 
java -cp XPlane-1.0-SNAPSHOT-jar-with-dependencies.jar;. -Djava.security.policy=java.policy -Xms500m -Xmx2500m -Duser.language=en -Duser.region=EN simplegui.XplaneParser 

## Notes
Any collaboration/improvement is welcome 

## Features
Parses the following items:
1. Runway
2. Taxiway centerlines
3. Gates
4. Taxiway Edges
It display it in a simple 2D panel

## ToDo

