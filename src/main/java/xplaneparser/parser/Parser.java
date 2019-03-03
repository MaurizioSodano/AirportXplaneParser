package xplaneparser.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xplaneparser.entity.Airport;
import xplaneparser.entity.Runway;
import xplaneparser.entity.Runway.SurfaceTypes;
import xplaneparser.utility.LatLongDistance;

public class Parser {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	public static final String AIRPORT_PREFIX = "1";
	public static final String METADATA_PREFIX = "1302";
	public static final String RUNWAY_PREFIX = "100";
	public static final String VIEWPORT_PREFIX = "14";

	private String xplaneFormatVersion;

	private Airport airport;

	public void parse(String filename) {

		File file = new File(filename);
		if (!file.exists()) {
			logger.error("File {} do not exists", filename);
			JOptionPane.showMessageDialog(null, "File " + filename + "do not exists", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		BufferedReader bufReader = null;
		try {
			bufReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			logger.error(e1.getMessage());
			return;
		}

		String readLine = null;
		int count = 0;
		try {
			while ((readLine = bufReader.readLine()) != null) {
				count++;
				if (count == 2) {// Version
					String[] splitData = readLine.split("\\s+");
					xplaneFormatVersion = splitData[0];

				}
				if (count <= 3) {
					continue; // Header
				}

				if (!readLine.isEmpty()) {
					String[] splitData = readLine.split("\\s+");

					switch (splitData[0]) {
					case AIRPORT_PREFIX: // Airport
						airport = parseAirportLine(splitData);
						break;
					case RUNWAY_PREFIX: //
						parseRunway(airport, splitData);
						break;
					default:

					}
				}

			}
			logger.info(airport.toString());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	private void parseRunway(Airport airport, String[] segments) {
		if (segments != null && segments.length >= 19) {
			double startLat = Double.parseDouble(segments[9]);
			double startLon = Double.parseDouble(segments[10]);
			double endLat = Double.parseDouble(segments[18]);
			double endLon = Double.parseDouble(segments[19]);
			String runwayID1=segments[8];
			String runwayID2=segments[17];
			double rwyLenght=LatLongDistance.distance(startLat, startLon, endLat, endLon, airport.elevation, airport.elevation);
			SurfaceTypes surface=SurfaceTypes.valueOf(Integer.parseInt(segments[2]));
			Runway runway1=new Runway(startLat,startLon,endLat,endLon, rwyLenght, runwayID1, surface);
			airport.runways.add(runway1);
			Runway runway2=new Runway(endLat,endLon,startLat,startLon, rwyLenght, runwayID2, surface);
			airport.runways.add(runway2);
			
		}

	}

	private Airport parseAirportLine(String[] segments) {
		Airport currentAiport = new Airport();

		currentAiport.ICAO = segments[4]; // Set the airports ICAO
		currentAiport.elevation = Integer.parseInt(segments[1]);

		// Processes the name of the airport
		for (int i = 5; i < segments.length; i++) {
			if (i == 5) {
				currentAiport.AirportName = segments[5];
			} else {
				currentAiport.AirportName += " " + segments[i];
			}
		}

		return currentAiport;
	}

}
