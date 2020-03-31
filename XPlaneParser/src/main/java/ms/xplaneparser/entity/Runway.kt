package ms.xplaneparser.entity

class Runway(var startLat: Double, var startLon: Double, var endLat: Double, var endLon: Double, var length: Double, var name: String, var surfaceType: SurfaceTypes) {
    override fun toString(): String {
        return "--Runway ID:  $name Length: $length Surface: $surfaceType"
    }

}