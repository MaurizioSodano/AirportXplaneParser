package ms.xplaneparser.entity

import java.util.*

class Airport {
    var icao: String? = null
    var country: String? = null
    var state: String? = null
    var city: String? = null
    @JvmField
	var airportName: String? = null
    var latitude = 0.0
    var longitude = 0.0
    var elevation = 0
    @JvmField
	val runways: MutableList<Runway> = ArrayList()
    @JvmField
	val taxiways: MutableList<Taxiway> = ArrayList()
    @JvmField
	val gates: MutableList<Gate> = ArrayList()
    @JvmField
	val taxiPoints: MutableMap<String, LatLong> = HashMap()
    val isFilled: Boolean
        get() {
            if (icao == null) return false
            if (airportName == null) return false
            if (java.lang.Double.isNaN(latitude)) {
                return false
            }
            if (java.lang.Double.isNaN(longitude)) {
                return false
            }
            if (country == null) return false
            return if (city == null) false else true
        }

    override fun toString(): String {
        return "ICAO: $icao | Name: $airportName | City: $city | State: $state | Country: $country | Lat: $latitude | Lon: $longitude   | Elv: $elevation"
    }
}