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
## ToDo
Add missing Airport details 
Gate and taxiway handlings
