# Airport Parser

What is this? Its an X-Plane 11 apt.dat Java parser. 
Inspired and ported in Java from https://github.com/njbrown09/XPlaneAirportParser


## Instructions
compile with maven: mvn clean install

Run the following 
java -cp XPlane-1.0-SNAPSHOT-jar-with-dependencies.jar;. -Djava.security.policy=java.policy -Xms500m -Xmx2500m -Duser.language=en -Duser.region=EN xplaneparser.gui.XplaneParser 

## Notes

## ToDo
Add missing Airport details 
Gate and taxiway handlings
