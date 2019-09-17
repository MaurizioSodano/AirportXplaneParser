package ms.xplaneparser.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author MaurizioSodano
 *  some useful combinations:
 *  1 101 Taxiway centre lines

 *  6 103 Hold lines
 *  4 103 Runway Hold Position
 *  3 102 Tawiway Boundary , 3 Taxiway edge lines 
 */
public enum LineTypes
{
	NOTHING(0),
	TAXIWAY_CENTERLINE( 1),
    BOUNDARY( 2),
    TAXIWAY_EDGELINE(3),
    RUNWAY_HOLDING( 4),
    HOLDING(5),
    ILS_HOLDING( 6),
    SAFETY_TAXIWAY_CENTERLINE(7),
    QUEUEING( 8),
    QUEUEING_2( 9),
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
    UNDEFINED( 999);
    
    @Getter private int value;
    private static Map<Integer,LineTypes> map = new HashMap<>();

    LineTypes(int value) {
        this.value = value;
    }

    static {
        for (LineTypes pageType : LineTypes.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static LineTypes valueOf(int pageType) {
        return map.get(pageType);
    }


    public static boolean isTaxiwayCenterLine(String type){
    	if (type==null || type.isEmpty()) return false;
    	return type.equals(""+ TAXIWAY_CENTERLINE.getValue()) || type.equals(""+ TAXIWAY_CENTERLINE_2.getValue());
    }
    
    public static boolean isRunwayHoldPosition(String type){
    	if (type==null || type.isEmpty()) return false;
    	return type.equals(""+ RUNWAY_HOLDING.getValue()) || type.equals(""+ HOLDING_2.getValue());
    }
}
