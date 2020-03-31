package ms.xplaneparser.utility

import kotlin.math.*

object LatLongDistance {
    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    fun distance(lat1: Double, lat2: Double, lon1: Double,
                 lon2: Double, el1: Double, el2: Double): Double {
        val earthRadius = 6371 // Radius of the earth
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = (sin(latDistance / 2) * sin(latDistance / 2)
                + (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
                * sin(lonDistance / 2) * sin(lonDistance / 2)))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        var distance = earthRadius * c * 1000 // convert to meters
        val height = el1 - el2
        distance = distance.pow(2) + height.pow(2)
        return sqrt(distance)
    }
}