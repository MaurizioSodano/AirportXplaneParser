package xplaneparser.entity;

import java.util.HashMap;
import java.util.Map;

public class Runway {
	
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

    public SurfaceTypes surfaceType;

    public double startLat;

    public double startLon;

    public double endLat;

    public double endLon;

    public double length;

    public String runwayId;

    public Runway(double startLat, double startLon, double endLat, double endLon, double length, String runwayId, SurfaceTypes surfaceType)
    {
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
        this.length = length;
        this.runwayId = runwayId;
        this.surfaceType = surfaceType;
    }

    @Override
	public String toString()
    {
        return "--Runway ID:  "+runwayId+  " Length: " + length + " Surface: " + surfaceType;
    }
}
