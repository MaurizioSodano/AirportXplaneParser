package ms.xplaneparser.entity;

import java.util.HashMap;
import java.util.Map;

public enum SurfaceTypes
{
    Asphalt ( 1),
    Concrete ( 2),
    Grass (3),
    Dirt ( 4),
    Gravel (5),
    DryLakebed ( 12),
    Water  (13),
    SnowOrIce ( 14),
    Other  (15),
    Undefined ( 999);
    
    private int value;
    private static Map<Integer,SurfaceTypes> map = new HashMap<>();

    private SurfaceTypes(int value) {
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

    public int getValue() {
        return value;
    }
}
