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
	
	public LatLong getMirror(LatLong coord) {
		return getMirror(coord.latitude,coord.longitude);
		
	}
	public LatLong getMirror(double latitude, double longitude) {
		return new LatLong(2*latitude-this.latitude,2*longitude-this.longitude);
	}
}
