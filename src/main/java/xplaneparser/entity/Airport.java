package xplaneparser.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Airport
{
	
	public String ICAO;

	public String country;

    public String state;

	public String city;

	public String AirportName;

	public double latitude;

	public double longitude;

	public int elevation;

	public boolean hasProperLatLon;

    public final List<Runway> runways = new ArrayList<>();

	/// <summary>
	/// Is the airport data filled (is it done and ready for processing?)
	/// </summary>
	/// <returns>True if the airport has completed being parsed</returns>
	public boolean isFilled()
	{
		if (ICAO == null)
			return false;

		if (AirportName == null)
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
		StringBuffer sb= new StringBuffer("ICAO: " + ICAO + " | Name: " + AirportName + " | City: " + city + " | State: " + state + " | Country: " + country + " | Lat: " + latitude + " | Lon: " + longitude + "   | Elv: " + elevation);
		String runyayDescription = runways.stream()
			.map(Runway::toString)
			.collect(Collectors.joining(" "));
		sb.append(runyayDescription);
		return sb.toString();
		
	}
}
