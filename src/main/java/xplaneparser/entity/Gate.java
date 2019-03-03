package xplaneparser.entity;

public class Gate {
	private String name;
	private double latitude;
	private double longitude;
	private double heading;
	
	public Gate(String name, double latitude, double longitude, double heading) {
		this.setName(name);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setHeading(heading);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "GATE : " +name+ " Lat: "+latitude+ " Long: "+longitude;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}
}
