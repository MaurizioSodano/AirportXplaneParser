package ms.xplaneparser.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class LatLong {
	protected static final double THRESHOLD = 1e-5;
	private double latitude;
	private double longitude;

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
		return (diff1 < THRESHOLD) && (diff2 < THRESHOLD);
	}
}
