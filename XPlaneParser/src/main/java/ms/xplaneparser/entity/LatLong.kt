package ms.xplaneparser.entity

import kotlin.math.abs

open class LatLong(var latitude: Double, var longitude: Double) {
    override fun toString(): String {
        return "-- Lat: $latitude Long: $longitude"
    }

    fun getMirror(coord: LatLong): LatLong {
        return getMirror(coord.latitude, coord.longitude)
    }

    fun getMirror(latitude: Double, longitude: Double): LatLong {
        return LatLong(2 * latitude - this.latitude, 2 * longitude - this.longitude)
    }

    fun isCloseTo(taxipoint: LatLong): Boolean {
        return isCloseTo(taxipoint.latitude, taxipoint.longitude)
    }

    fun isCloseTo(latitude: Double, longitude: Double): Boolean {
        val diff1: Double = abs(latitude - this.latitude)
        val diff2: Double = abs(longitude - this.longitude)
        return diff1 < THRESHOLD && diff2 < THRESHOLD
    }

    companion object {
        protected const val THRESHOLD = 1e-5
    }

}