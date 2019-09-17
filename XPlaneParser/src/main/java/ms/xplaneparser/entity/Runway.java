package ms.xplaneparser.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Runway {
	public double startLat;
	public double startLon;
	public double endLat;
	public double endLon;
	public double length;
	public String runwayId;
	public SurfaceTypes surfaceType;


	@Override
	public String toString() {
		return "--Runway ID:  " + runwayId + " Length: " + length + " Surface: " + surfaceType;
	}

	public String getName() {
		return runwayId;
	}
}
