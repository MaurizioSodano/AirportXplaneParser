package ms.xplaneparser.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ms.xplaneparser.entity.Airport;
import ms.xplaneparser.entity.Gate;
import ms.xplaneparser.entity.LatLong;
import ms.xplaneparser.entity.LineTypes;
import ms.xplaneparser.entity.Runway;
import ms.xplaneparser.entity.SurfaceTypes;
import ms.xplaneparser.entity.Taxiway;
import ms.xplaneparser.utility.BezierUtility;
import ms.xplaneparser.utility.LatLongDistance;

public class Parser {
    private static final String POINT_PREFIX = "TP_";

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    public static final String AIRPORT_PREFIX = "1";
    public static final String METADATA_PREFIX = "1302";
    public static final String RUNWAY_PREFIX = "100";
    public static final String VIEWPORT_PREFIX = "14";
    public static final String TAXIWAY_START_PREFIX = "120";
    public static final String TAXIWAY_NODE_PREFIX = "111";
    public static final String TAXIWAY_BEZIER_NODE_PREFIX = "112";
    public static final String TAXIWAY_CLOSE_LOOP_BOUNDARY_PREFIX = "113";
    public static final String TAXIWAY_CLOSE_LOOP_BEZIER_PREFIX = "114";
    public static final String TAXIWAY_END_LINE_PREFIX = "115";
    public static final String TAXIWAY_END_BEZIER_PREFIX = "116";
    public static final String GATE_PREFIX = "1300";
    public static final String GATE_TYPE = "GATE";
    public static final String TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE = "TAXIWAY not ended properly (110 not found) line {}";

    private String xplaneFormatVersion;

    private Airport airport;

    private Taxiway currentTaxiway;

    private boolean previousIsBezier = false;
    private LatLong controlPoint1;
    private LatLong controlPoint2;

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
                    currentTaxiway = addLineAttribute(currentTaxiway, splitData);
                    switch (splitData[0]) {
                        case AIRPORT_PREFIX: // Airport
                            airport = parseAirportLine(splitData);
                            break;
                        case RUNWAY_PREFIX: //
                            parseRunway(airport, splitData);
                            break;

                        case TAXIWAY_START_PREFIX:
                            currentTaxiway = parseStartTaxiway(splitData);
                            previousIsBezier = false;
                            break;
                        case TAXIWAY_NODE_PREFIX:
                            parseNodeTaxiway(currentTaxiway, splitData, count);
                            break;
                        case TAXIWAY_BEZIER_NODE_PREFIX:
                            parseNodeBezierTaxiway(currentTaxiway, splitData, count);
                            break;
                        case TAXIWAY_END_BEZIER_PREFIX:
                            parseNodeBezierTaxiway(currentTaxiway, splitData, count);
                            if (currentTaxiway != null) {// STARTED TAXIWAY
                                airport.taxiways.add(currentTaxiway);
                                currentTaxiway = null;
                            } else {
                                logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count);
                            }
                            break;
                        case TAXIWAY_END_LINE_PREFIX:
                            parseNodeTaxiway(currentTaxiway, splitData, count);
                            if (currentTaxiway != null) {// STARTED TAXIWAY
                                airport.taxiways.add(currentTaxiway);
                                currentTaxiway = null;
                            } else {
                                logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count);
                            }

                            break;
                        case TAXIWAY_CLOSE_LOOP_BOUNDARY_PREFIX:
                            parseNodeTaxiway(currentTaxiway, splitData, count);
                            if (currentTaxiway != null) {// STARTED TAXIWAY
                                currentTaxiway.closeLinearLoop();
                            } else {
                                logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count);
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

        createCenterlinesTaxipoints();
        return airport;

    }

    /**
     * Create Points From Taxiways, that are not too close
     * and assign a unique name
     */
    private void createCenterlinesTaxipoints() {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        airport.taxiways.stream()
                .filter(ms.xplaneparser.entity.Taxiway::isCenterLine)
                .sorted()
                .forEach(taxiway -> taxiway.getNodes().forEach(node -> {
                    LatLong tmpTaxipoint = new LatLong(node.getLatitude(), node.getLongitude());


                    boolean pointExists = airport.taxiPoints.values().stream()
                            .anyMatch(point -> tmpTaxipoint.isCloseTo(point.getLatitude(), point.getLongitude()));

                    if (!pointExists) {
                        atomicInteger.getAndIncrement();
                        String name = POINT_PREFIX + atomicInteger.get();
                        airport.taxiPoints.put(name, tmpTaxipoint);
                    }
                }));

    }

    private void parseNodeBezierTaxiway(Taxiway currentTaxiway, String[] segments, int count) {
        /**
         * Shoud use this formula to interpolate: B(t)=(1-t)*P0+t*P1, t in [0,1] P =
         * (1−t)2P1 + 2(1−t)tP2 + t2P3
         *
         * For 4 control points:
         *
         * P = (1−t)3P1 + 3(1−t)2tP2 +3(1−t)t2P3 + t3P4
         **/
        double startLat = Double.parseDouble(segments[1]);
        double startLon = Double.parseDouble(segments[2]);
        double endLat = Double.parseDouble(segments[3]);
        double endLon = Double.parseDouble(segments[4]);

        if (currentTaxiway != null) {// STARTED TAXIWAY
            LatLong lastNode = currentTaxiway.getLastNode();
            if (lastNode != null) { // Exists a previous node
                if (lastNode.getLatitude() != startLat || lastNode.getLongitude() != startLon) {
                    if (!previousIsBezier) {// create a quadratic curve with mirror control point
                        LatLong p0 = lastNode;
                        LatLong p2 = new LatLong(startLat, startLon);
                        LatLong p1 = new LatLong(endLat, endLon).getMirror(p2);

                        List<LatLong> curvedPoints = BezierUtility.bezierQuadratic(10, p0, p1, p2);
                        curvedPoints.forEach(point -> currentTaxiway.addNode(point.getLatitude(), point.getLongitude()));
                        previousIsBezier = false;
                    } else {// create a cubic curve with mirror control point
                        LatLong p0 = lastNode;
                        LatLong p1 = controlPoint1;
                        LatLong p3 = new LatLong(startLat, startLon);

                        LatLong p2 = new LatLong(endLat, endLon).getMirror(p3);
                        List<LatLong> curvedPoints = BezierUtility.bezierCubic(10, p0, p1, p2, p3);
                        curvedPoints.forEach(point -> currentTaxiway.addNode(point.getLatitude(), point.getLongitude()));
                        previousIsBezier = false;
                    }

                } else {
                    currentTaxiway.addNode(startLat, startLon);
                    controlPoint1 = new LatLong(endLat, endLon);
                    controlPoint2 = new LatLong(startLat, startLon);
                    previousIsBezier = true;
                }

            } else {
                currentTaxiway.addNode(startLat, startLon);
                controlPoint1 = new LatLong(endLat, endLon);
                controlPoint2 = new LatLong(startLat, startLon);
                previousIsBezier = true;
            }

        } else {
            logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count);
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

    private Taxiway addLineAttribute(Taxiway currentTaxiway, String[] segments) {
        boolean isCenterline = Arrays.stream(segments).skip(1).anyMatch(LineTypes::isTaxiwayCenterLine);
        boolean isRunwayHoldPosition = Arrays.stream(segments).skip(1)
                .anyMatch(LineTypes::isRunwayHoldPosition);
        if (currentTaxiway != null) {// STARTED TAXIWAY
            if (isCenterline) {
                currentTaxiway.setCenterLine(true);
            } else if (isRunwayHoldPosition) {
                currentTaxiway.setRunwayHold(true);
            }
        }
        return currentTaxiway;

    }

    private void parseNodeTaxiway(Taxiway currentTaxiway, String[] segments, int count) {
        double latitude = Double.parseDouble(segments[1]);
        double longitude = Double.parseDouble(segments[2]);

        if (currentTaxiway != null) {// STARTED TAXIWAY
            if (previousIsBezier) {// Previous Node was a Bezier, create a quadratic curve
                LatLong p0 = currentTaxiway.getLastNode();
                LatLong p1 = controlPoint1;
                LatLong p2 = new LatLong(latitude, longitude);
                List<LatLong> curvedPoints = BezierUtility.bezierQuadratic(10, p0, p1, p2);
                curvedPoints.forEach(point -> currentTaxiway.addNode(point.getLatitude(), point.getLongitude()));

            } else {
                currentTaxiway.addNode(latitude, longitude);
            }
            previousIsBezier = false;
        } else {
            logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count);
        }
    }

    private Taxiway parseStartTaxiway(String[] segments) {
        Taxiway taxiway = new Taxiway();
        String name = Arrays.stream(segments).skip(1).collect(Collectors.joining(" "));
        taxiway.setName(name);
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

        currentAiport.icao = segments[4]; // Set the airports ICAO
        currentAiport.elevation = Integer.parseInt(segments[1]);

        StringBuilder sb = new StringBuilder();
        // Processes the name of the airport
        for (int i = 5; i < segments.length; i++) {
            if (i == 5) {
                sb.append(segments[5]);
            } else {
                sb.append(" ").append(segments[i]);
            }
        }

        currentAiport.airportName = sb.toString();
        return currentAiport;
    }

}
