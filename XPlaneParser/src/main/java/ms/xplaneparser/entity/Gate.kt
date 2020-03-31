package ms.xplaneparser.entity

class Gate(var name: String?, var latitude: Double, var longitude: Double, var heading: Double) {
    override fun toString(): String {
        return "GATE : $name Lat: $latitude Long: $longitude"
    }
}