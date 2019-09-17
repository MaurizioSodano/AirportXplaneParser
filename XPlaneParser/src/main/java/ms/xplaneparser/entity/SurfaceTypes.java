package ms.xplaneparser.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum SurfaceTypes
{
    ASPHALT( 1),
    CONCRETE( 2),
    GRASS(3),
    DIRT( 4),
    GRAVEL(5),
    DRY_LAKE_BED( 12),
    WATER(13),
    SNOW_OR_ICE( 14),
    OTHER(15),
    UNDEFINED( 999);
    
    @Getter private int value;
    private static Map<Integer,SurfaceTypes> map = new HashMap<>();

    SurfaceTypes(int value) {
        this.value = value;
    }

    static {
        for (SurfaceTypes pageType : SurfaceTypes.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static SurfaceTypes valueOf(int pageType) {
        return map.get(pageType);
    }

}
