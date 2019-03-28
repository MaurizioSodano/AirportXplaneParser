package ms.xplaneparser.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Airport
{
	
	public String ICAO;
	public String country;
    public String state;
	public String city;
	public String airportName;
	public double latitude;
	public double longitude;
	public int elevation;
	public boolean hasProperLatLon;

    public final List<Runway> runways = new ArrayList<>();
    public final List<Taxiway> taxiways = new ArrayList<>();
    public final List<Gate> gates = new ArrayList<>();

    public final Map<String, LatLong> taxipoints=new HashMap<>();
    
    
	public boolean isFilled()
	{
		if (ICAO == null)
			return false;

		if (airportName == null)
			return false;

		if (Double.isNaN(latitude)) {
			return false;
		}

		if (Double.isNaN(longitude)) {
			return false;
		}
		if (country == null)
			return false;

		if (city == null)
			return false;

		return true;
	}

	@Override
	public String toString()
	{
		StringBuffer sb= new StringBuffer("ICAO: " + ICAO + " | Name: " + airportName + " | City: " + city + " | State: " + state + " | Country: " + country + " | Lat: " + latitude + " | Lon: " + longitude + "   | Elv: " + elevation);
//		String runyayDescription = runways.stream()
//			.map(Runway::toString)
//			.collect(Collectors.joining(" "));
//		sb.append(runyayDescription);
		return sb.toString();
		
	}
}
