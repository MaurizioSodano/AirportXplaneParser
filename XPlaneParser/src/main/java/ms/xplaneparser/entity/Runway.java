package ms.xplaneparser.entity;

public class Runway {

	public SurfaceTypes surfaceType;
	public double startLat;
	public double startLon;
	public double endLat;
	public double endLon;
	public double length;
	public String runwayId;

	public Runway(double startLat, double startLon, double endLat, double endLon, double length, String runwayId,
			SurfaceTypes surfaceType) {
		this.startLat = startLat;
		this.startLon = startLon;
		this.endLat = endLat;
		this.endLon = endLon;
		this.length = length;
		this.runwayId = runwayId;
		this.surfaceType = surfaceType;
	}

	@Override
	public String toString() {
		return "--Runway ID:  " + runwayId + " Length: " + length + " Surface: " + surfaceType;
	}

	public String getName() {
		return runwayId;
	}
}
