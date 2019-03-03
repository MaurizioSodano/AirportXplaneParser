package xplaneparser.entity;

import java.util.HashMap;
import java.util.Map;

public enum LineTypes
{
	Nothing (0),
	TaxiwayCenterline( 1),
    Boundary ( 2),
    TaxiwayEdgeline(3),
    RunwayHolding( 4),
    Holding (5),
    ILSHolding ( 6),
    SafetyTaxiwayCenterline(7),
    Queueing ( 8),
    Queueing2 ( 9),
    Roadway1(20),
    Roadway2(21),
    Roadway3(22),
    ConcreteSurface1(51),
    ConcreteSurface2(52),
    ConcreteSurface3(53),
    ConcreteSurface4(54),
    ConcreteSurface5(55),
    ConcreteSurface6(56),
    ConcreteSurface7(57),
    ConcreteSurface8(58),
    ConcreteSurface9(59),
    TaxiwayCenterline2 (101),
    TaxiwayEdge2 (102),
    Holding2(103),
    RunwayHolding2(104),
    SafetyTaxiwayCenterline2(105),
    Dangerous(105),
    Undefined ( 999);
    
    private int value;
    private static Map<Integer,LineTypes> map = new HashMap<>();

    private LineTypes(int value) {
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

    public int getValue() {
        return value;
    }
}
