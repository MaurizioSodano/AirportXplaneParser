package xplaneparser.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xplaneparser.entity.Airport;
import xplaneparser.entity.Gate;
import xplaneparser.entity.LineTypes;
import xplaneparser.entity.Runway;
import xplaneparser.entity.SurfaceTypes;
import xplaneparser.entity.Taxiway;
import xplaneparser.utility.LatLongDistance;

public class Parser {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	public static final String AIRPORT_PREFIX = "1";
	public static final String METADATA_PREFIX = "1302";
	public static final String RUNWAY_PREFIX = "100";
	public static final String VIEWPORT_PREFIX = "14";
	public static final String TAXIWAY_START_PREFIX = "120";
	public static final String TAXIWAY_NODE_PREFIX = "111";
	public static final String TAXIWAY_BEZIER_NODE_PREFIX = "112";
	public static final String TAXIWAY_END_PREFIX = "113";
	public static final String TAXIWAY_END_PREFIX2 = "114";
	public static final String TAXIWAY_END_PREFIX3 = "115";
	public static final String TAXIWAY_END_PREFIX4 = "116";
	public static final String GATE_PREFIX = "1300";
	public static final String GATE_TYPE = "GATE";

	private String xplaneFormatVersion;

	private Airport airport;

	private Taxiway currentTaxiway;

	public Airport parse(String filename) {

		File file = new File(filename);
		if (!file.exists()) {
			logger.error("File {} do not exists", filename);
			JOptionPane.showMessageDialog(null, "File " + filename + "do not exists", "Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		String readLine = null;
		int count = 0;
		try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
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

					case TAXIWAY_START_PREFIX:
						currentTaxiway = parseStartTaxiway(splitData);
						break;
					case TAXIWAY_NODE_PREFIX:
						parseNodeTaxiway(currentTaxiway, splitData, count);
						break;
					case TAXIWAY_BEZIER_NODE_PREFIX:
						parseNodeBezierTaxiway(currentTaxiway, splitData, count);
						break;
					case TAXIWAY_END_PREFIX:
					case TAXIWAY_END_PREFIX3:
						if (currentTaxiway != null) {// STARTED TAXIWAY
							airport.taxiways.add(currentTaxiway);
							currentTaxiway = null;
						} else {
							logger.debug("TAXIWAY not ended properly (110 not found) line", count);
						}

						break;
					case GATE_PREFIX:
						parseGate(airport, splitData);
					default:

					}
				}

			}

			


		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return airport;

	}

	private void parseNodeBezierTaxiway(Taxiway currentTaxiway2, String[] segments, int count) {
		/*
		 * Shoud use this formula to interpolate: B(t)=(1-t)*P0+t*P1, t in [0,1]
		 */
		double startLat = Double.parseDouble(segments[1]);
		double startLon = Double.parseDouble(segments[2]);
		double endLat = Double.parseDouble(segments[3]);
		double endLon = Double.parseDouble(segments[4]);
		if (currentTaxiway != null) {// STARTED TAXIWAY
			currentTaxiway.addNode(startLat, startLon);
			currentTaxiway.addNode(endLat, endLon);
		} else {
			logger.debug("TAXIWAY not Started Properly (110 not found) line " + count);
		}

	}

	private void parseGate(Airport airport, String[] segments) {
		double latitude = Double.parseDouble(segments[1]);
		double longitude = Double.parseDouble(segments[2]);
		double heading = Double.parseDouble(segments[3]);
		String gateType = segments[4];
		if (GATE_TYPE.equalsIgnoreCase(gateType)) {
			String gateName = segments[7];
			airport.gates.add(new Gate(gateName, latitude, longitude, heading));
		}

	}

	private void parseNodeTaxiway(Taxiway currentTaxiway, String[] segments, int count) {
		double latitude = Double.parseDouble(segments[1]);
		double longitude = Double.parseDouble(segments[2]);
		boolean isCenterline = Arrays.asList(segments).stream()
			.anyMatch(token->token.equals("" +LineTypes.TaxiwayCenterline2.getValue()));
			
		if (isCenterline) currentTaxiway.setCenterline(true);
		if (currentTaxiway != null) {// STARTED TAXIWAY
			currentTaxiway.addNode(latitude, longitude);
		} else {
			logger.debug("TAXIWAY not Started Properly (110 not found) line " + count);
		}
	}

	private Taxiway parseStartTaxiway(String[] segments) {
		Taxiway taxiway = new Taxiway();
		StringBuilder sb = new StringBuilder();
		// Processes the name of the airport
		for (int i = 1; i < segments.length; i++) {
			if (i == 1) {
				sb.append(segments[i]);
			} else {
				sb.append(segments[i]);
			}
		}
		taxiway.setName(sb.toString());
		return taxiway;
	}

	private void parseRunway(Airport airport, String[] segments) {
		if (segments != null && segments.length >= 19) {
			double startLat = Double.parseDouble(segments[9]);
			double startLon = Double.parseDouble(segments[10]);
			double endLat = Double.parseDouble(segments[18]);
			double endLon = Double.parseDouble(segments[19]);
			String runwayID1 = segments[8];
			String runwayID2 = segments[17];
			double rwyLenght = LatLongDistance.distance(startLat, startLon, endLat, endLon, airport.elevation,
					airport.elevation);
			SurfaceTypes surface = SurfaceTypes.valueOf(Integer.parseInt(segments[2]));
			Runway runway1 = new Runway(startLat, startLon, endLat, endLon, rwyLenght, runwayID1, surface);
			airport.runways.add(runway1);
			Runway runway2 = new Runway(endLat, endLon, startLat, startLon, rwyLenght, runwayID2, surface);
			airport.runways.add(runway2);

		}

	}

	private Airport parseAirportLine(String[] segments) {
		Airport currentAiport = new Airport();

		currentAiport.ICAO = segments[4]; // Set the airports ICAO
		currentAiport.elevation = Integer.parseInt(segments[1]);

		StringBuilder sb = new StringBuilder();
		// Processes the name of the airport
		for (int i = 5; i < segments.length; i++) {
			if (i == 5) {
				sb.append(segments[5]);
			} else {
				sb.append(" " + segments[i]);
			}
		}

		currentAiport.AirportName = sb.toString();
		return currentAiport;
	}

}
