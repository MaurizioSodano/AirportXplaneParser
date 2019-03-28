package ms.xplaneparser.entity;


public class LatLong {
	protected static final double TRESHOLD = 1e-5;

	public LatLong(double latitude, double longitude) {
		this.latitude=latitude;
		this.longitude=longitude;
	}
	private double latitude;
	private double longitude;
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
		return "-- Lat: " +latitude +" Long: "+longitude;
	}
	
	public LatLong getMirror(LatLong coord) {
		return getMirror(coord.latitude,coord.longitude);
		
	}
	public LatLong getMirror(double latitude, double longitude) {
		return new LatLong(2*latitude-this.latitude,2*longitude-this.longitude);
	}
	
	public boolean isCloseTo(LatLong taxipoint ) {
		return isCloseTo(taxipoint.latitude,taxipoint.longitude);
	}
	
	public boolean isCloseTo(double latitude, double longitude) {
		double diff1;
		double diff2;
		diff1 = Math.abs(latitude- this.latitude);
		diff2 = Math.abs(longitude - this.longitude);
		return (diff1 < TRESHOLD) && (diff2 < TRESHOLD);
	}
}
