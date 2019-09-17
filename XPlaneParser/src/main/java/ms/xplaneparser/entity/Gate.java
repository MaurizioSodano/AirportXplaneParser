package ms.xplaneparser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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


	@Override
	public String toString() {
		return "GATE : " +name+ " Lat: "+latitude+ " Long: "+longitude;
	}

}
