package ms.xplaneparser.parser

import ms.xplaneparser.entity.*
import ms.xplaneparser.utility.BezierUtility
import ms.xplaneparser.utility.LatLongDistance
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.lang.invoke.MethodHandles
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Collectors
import javax.swing.JOptionPane

class Parser {
    private var xplaneFormatVersion: String? = null
    private var airport: Airport? = null
    private var currentTaxiway: Taxiway? = null
    private var previousIsBezier = false
    private var controlPoint1: LatLong? = null
    private var controlPoint2: LatLong? = null
    fun parse(filename: String): Airport? {
        val file = File(filename)
        if (!file.exists()) {
            logger.error("File {} do not exists", filename)
            JOptionPane.showMessageDialog(null, "File " + filename + "do not exists", "Error",
                    JOptionPane.ERROR_MESSAGE)
            return null
        }
        var readLine: String? = null
        var count = 0
        try {
            BufferedReader(FileReader(file)).use { bufReader ->
                while (bufReader.readLine().also { readLine = it } != null) {
                    count++
                    if (count == 2) { // Version
                        val splitData = readLine!!.split(Regex("\\s+")).toTypedArray()
                        xplaneFormatVersion = splitData[0]
                    }
                    if (count <= 3) {
                        continue  // Header
                    }
                    if (readLine!!.isNotEmpty()) {
                        val splitData = readLine!!.split(Regex("\\s+")).toTypedArray()
                        currentTaxiway = addLineAttribute(currentTaxiway, splitData)
                        when (splitData[0]) {
                            AIRPORT_PREFIX -> airport = parseAirportLine(splitData)
                            RUNWAY_PREFIX -> parseRunway(airport, splitData)
                            TAXIWAY_START_PREFIX -> {
                                currentTaxiway = parseStartTaxiway(splitData)
                                previousIsBezier = false
                            }
                            TAXIWAY_NODE_PREFIX -> parseNodeTaxiway(currentTaxiway, splitData, count)
                            TAXIWAY_BEZIER_NODE_PREFIX -> parseNodeBezierTaxiway(currentTaxiway, splitData, count)
                            TAXIWAY_END_BEZIER_PREFIX -> {
                                parseNodeBezierTaxiway(currentTaxiway, splitData, count)
                                if (currentTaxiway != null) { // STARTED TAXIWAY
                                    airport!!.taxiways.add(currentTaxiway!!)
                                    currentTaxiway = null
                                } else {
                                    logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count)
                                }
                            }
                            TAXIWAY_END_LINE_PREFIX -> {
                                parseNodeTaxiway(currentTaxiway, splitData, count)
                                if (currentTaxiway != null) { // STARTED TAXIWAY
                                    airport!!.taxiways.add(currentTaxiway!!)
                                    currentTaxiway = null
                                } else {
                                    logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count)
                                }
                            }
                            TAXIWAY_CLOSE_LOOP_BOUNDARY_PREFIX -> {
                                parseNodeTaxiway(currentTaxiway, splitData, count)
                                if (currentTaxiway != null) { // STARTED TAXIWAY
                                    currentTaxiway!!.closeLinearLoop()
                                } else {
                                    logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count)
                                }
                            }
                            GATE_PREFIX -> parseGate(airport, splitData)
                            else -> {
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            logger.error(e.message)
        }
        createCenterlinesTaxipoints()
        return airport
    }

    /**
     * Create Points From Taxiways, that are not too close
     * and assign a unique name
     */
    private fun createCenterlinesTaxipoints() {
        val atomicInteger = AtomicInteger(0)
        airport!!.taxiways
                .filter{ it.isCenterLine}
                .sorted()
                .forEach { taxiway: Taxiway ->
                    taxiway.nodes.forEach(Consumer { node: LatLong ->
                        val tmpTaxipoint = LatLong(node.latitude, node.longitude)
                        val pointExists = airport!!.taxiPoints.values.stream()
                                .anyMatch { point: LatLong -> tmpTaxipoint.isCloseTo(point.latitude, point.longitude) }
                        if (!pointExists) {
                            atomicInteger.getAndIncrement()
                            val name = POINT_PREFIX + atomicInteger.get()
                            airport!!.taxiPoints[name] = tmpTaxipoint
                        }
                    })
                }
    }

    private fun parseNodeBezierTaxiway(currentTaxiway: Taxiway?, segments: Array<String>, count: Int) {
        /**
         * Shoud use this formula to interpolate: B(t)=(1-t)*P0+t*P1, t in [0,1] P =
         * (1−t)2P1 + 2(1−t)tP2 + t2P3
         *
         * For 4 control points:
         *
         * P = (1−t)3P1 + 3(1−t)2tP2 +3(1−t)t2P3 + t3P4
         */
        val startLat = segments[1].toDouble()
        val startLon = segments[2].toDouble()
        val endLat = segments[3].toDouble()
        val endLon = segments[4].toDouble()
        if (currentTaxiway != null) { // STARTED TAXIWAY
            val lastNode = currentTaxiway.lastNode
            if (lastNode != null) { // Exists a previous node
                if (lastNode.latitude != startLat || lastNode.longitude != startLon) {
                    previousIsBezier = if (!previousIsBezier) { // create a quadratic curve with mirror control point
                        val p2 = LatLong(startLat, startLon)
                        val p1 = LatLong(endLat, endLon).getMirror(p2)
                        val curvedPoints = BezierUtility.bezierQuadratic(10, lastNode, p1, p2)
                        curvedPoints.forEach(Consumer { point: LatLong -> currentTaxiway.addNode(point.latitude, point.longitude) })
                        false
                    } else { // create a cubic curve with mirror control point
                        val p1 = controlPoint1
                        val p3 = LatLong(startLat, startLon)
                        val p2 = LatLong(endLat, endLon).getMirror(p3)
                        val curvedPoints = BezierUtility.bezierCubic(10, lastNode, p1!!, p2, p3)
                        curvedPoints.forEach(Consumer { point: LatLong -> currentTaxiway.addNode(point.latitude, point.longitude) })
                        false
                    }
                } else {
                    currentTaxiway.addNode(startLat, startLon)
                    controlPoint1 = LatLong(endLat, endLon)
                    controlPoint2 = LatLong(startLat, startLon)
                    previousIsBezier = true
                }
            } else {
                currentTaxiway.addNode(startLat, startLon)
                controlPoint1 = LatLong(endLat, endLon)
                controlPoint2 = LatLong(startLat, startLon)
                previousIsBezier = true
            }
        } else {
            logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count)
        }
    }

    private fun parseGate(airport: Airport?, segments: Array<String>) {
        val latitude = segments[1].toDouble()
        val longitude = segments[2].toDouble()
        val heading = segments[3].toDouble()
        val gateType = segments[4]
        if (GATE_TYPE.equals(gateType, ignoreCase = true)) {
            val gateName = segments[7]
            airport!!.gates.add(Gate(gateName, latitude, longitude, heading))
        }
    }

    private fun addLineAttribute(currentTaxiway: Taxiway?, segments: Array<String>): Taxiway? {
        val isCenterline = Arrays.stream(segments).skip(1).anyMatch { type: String? -> LineTypes.isTaxiwayCenterLine(type) }
        val isRunwayHoldPosition = Arrays.stream(segments).skip(1)
                .anyMatch { type: String? -> LineTypes.isRunwayHoldPosition(type) }
        if (currentTaxiway != null) { // STARTED TAXIWAY
            if (isCenterline) {
                currentTaxiway.isCenterLine = true
            } else if (isRunwayHoldPosition) {
                currentTaxiway.isRunwayHold = true
            }
        }
        return currentTaxiway
    }

    private fun parseNodeTaxiway(currentTaxiway: Taxiway?, segments: Array<String>, count: Int) {
        val latitude = segments[1].toDouble()
        val longitude = segments[2].toDouble()
        if (currentTaxiway != null) { // STARTED TAXIWAY
            if (previousIsBezier) { // Previous Node was a Bezier, create a quadratic curve
                val p0 = currentTaxiway.lastNode
                val p1 = controlPoint1
                val p2 = LatLong(latitude, longitude)
                val curvedPoints = BezierUtility.bezierQuadratic(10, p0!!, p1!!, p2)
                curvedPoints.forEach(Consumer { point: LatLong -> currentTaxiway.addNode(point.latitude, point.longitude) })
            } else {
                currentTaxiway.addNode(latitude, longitude)
            }
            previousIsBezier = false
        } else {
            logger.debug(TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE, count)
        }
    }

    private fun parseStartTaxiway(segments: Array<String>): Taxiway {
        val taxiway = Taxiway()
        val name = Arrays.stream(segments).skip(1).collect(Collectors.joining(" "))
        taxiway.name = name
        return taxiway
    }

    private fun parseRunway(airport: Airport?, segments: Array<String>?) {
        if (segments != null && segments.size >= 19) {
            val startLat = segments[9].toDouble()
            val startLon = segments[10].toDouble()
            val endLat = segments[18].toDouble()
            val endLon = segments[19].toDouble()
            val runwayID1 = segments[8]
            val runwayID2 = segments[17]
            val rwyLenght = LatLongDistance.distance(startLat, startLon, endLat, endLon, airport!!.elevation.toDouble(),
                    airport.elevation.toDouble())
            val surface = SurfaceTypes.valueOf(segments[2].toInt())
            val runway1 = Runway(startLat, startLon, endLat, endLon, rwyLenght, runwayID1, surface!!)
            airport.runways.add(runway1)
            val runway2 = Runway(endLat, endLon, startLat, startLon, rwyLenght, runwayID2, surface!!)
            airport.runways.add(runway2)
        }
    }

    private fun parseAirportLine(segments: Array<String>): Airport {
        val currentAiport = Airport()
        currentAiport.icao = segments[4] // Set the airports ICAO
        currentAiport.elevation = segments[1].toInt()
        val sb = StringBuilder()
        // Processes the name of the airport
        for (i in 5 until segments.size) {
            if (i == 5) {
                sb.append(segments[5])
            } else {
                sb.append(" ").append(segments[i])
            }
        }
        currentAiport.airportName = sb.toString()
        return currentAiport
    }

    companion object {
        private const val POINT_PREFIX = "TP_"
        private val logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().simpleName)
        const val AIRPORT_PREFIX = "1"
        const val METADATA_PREFIX = "1302"
        const val RUNWAY_PREFIX = "100"
        const val VIEWPORT_PREFIX = "14"
        const val TAXIWAY_START_PREFIX_ARBITRARY = "110" //Skipped because not centerline
        const val TAXIWAY_START_PREFIX = "120"
        const val TAXIWAY_NODE_PREFIX = "111"
        const val TAXIWAY_BEZIER_NODE_PREFIX = "112"
        const val TAXIWAY_CLOSE_LOOP_BOUNDARY_PREFIX = "113"
        const val TAXIWAY_CLOSE_LOOP_BEZIER_PREFIX = "114"
        const val TAXIWAY_END_LINE_PREFIX = "115"
        const val TAXIWAY_END_BEZIER_PREFIX = "116"
        const val GATE_PREFIX = "1300"
        const val GATE_TYPE = "GATE"
        const val TAXIWAY_NOT_ENDED_PROPERLY_110_NOT_FOUND_LINE = "TAXIWAY not ended properly (110 not found) line {}"
    }
}