package xplaneparser.entity;

public class LatLong {
	public LatLong(double latitude, double longitude) {
		this.latitude=latitude;
		this.longitude=longitude;
	}
	public double latitude;
	public double longitude;
	
	@Override
	public String toString() {
		return "-- Lat: " +latitude +" Long: "+longitude;
	}
}
