package ms.xplaneparser.entity

import java.util.*

enum class SurfaceTypes(val value: Int) {
    ASPHALT(1),
    CONCRETE(2),
    GRASS(3),
    DIRT(4),
    GRAVEL(5),
    DRY_LAKE_BED(12),
    WATER(13),
    SNOW_OR_ICE(14),
    OTHER(15),
    UNDEFINED(999);

    companion object {
        private val map: MutableMap<Int, SurfaceTypes> = HashMap()
        fun valueOf(pageType: Int): SurfaceTypes? {
            return map[pageType]
        }

        init {
            for (pageType in values()) {
                map[pageType.value] = pageType
            }
        }
    }

}