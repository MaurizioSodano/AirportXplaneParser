package ms.xplaneparser.entity

import java.util.*

/**
 *
 * @author MaurizioSodano
 * some useful combinations:
 * 1 101 Taxiway centre lines
 *
 * 6 103 Hold lines
 * 4 103 Runway Hold Position
 * 3 102 Tawiway Boundary , 3 Taxiway edge lines
 */
enum class LineTypes(val value: Int) {
    NOTHING(0),
    TAXIWAY_CENTERLINE(1),
    BOUNDARY(2),
    TAXIWAY_EDGELINE(3),
    RUNWAY_HOLDING(4),
    HOLDING(5),
    ILS_HOLDING(6),
    SAFETY_TAXIWAY_CENTERLINE(7),
    QUEUEING(8),
    QUEUEING_2(9),
    ROADWAY_1(20),
    ROADWAY_2(21),
    ROADWAY_3(22),
    CONCRETE_SURFACE_1(51),
    CONCRETE_SURFACE_2(52),
    CONCRETE_SURFACE_3(53),
    CONCRETE_SURFACE_4(54),
    CONCRETE_SURFACE_5(55),
    CONCRETE_SURFACE_6(56),
    CONCRETE_SURFACE_7(57),
    CONCRETE_SURFACE_8(58),
    CONCRETE_SURFACE_9(59),
    TAXIWAY_CENTERLINE_2(101),
    TAXIWAY_EDGE_2(102),
    HOLDING_2(103),
    RUNWAY_HOLDING_2(104),
    SAFETY_TAXIWAY_CENTERLINE_2(105),
    DANGEROUS(105),
    UNDEFINED(999);

    companion object {
        private val map: MutableMap<Int, LineTypes> = HashMap()
        fun valueOf(pageType: Int): LineTypes? {
            return map[pageType]
        }

        fun isTaxiwayCenterLine(type: String?): Boolean {
            return if (type == null || type.isEmpty()) false else type == "" + TAXIWAY_CENTERLINE.value || type == "" + TAXIWAY_CENTERLINE_2.value
        }

        fun isRunwayHoldPosition(type: String?): Boolean {
            return if (type == null || type.isEmpty()) false else type == "" + RUNWAY_HOLDING.value || type == "" + HOLDING_2.value
        }

        init {
            for (pageType in values()) {
                map[pageType.value] = pageType
            }
        }
    }

}