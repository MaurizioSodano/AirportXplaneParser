package xplaneparser.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Taxiway {
	private static final String newLine = System.getProperty("line.separator");
	private String name;
	
	private List<LatLong> nodes=new ArrayList<>();
	
	
	public void addNode(double latitude,double longitude) {
		nodes.add(new LatLong(latitude,longitude));
	}
	
	public List<LatLong> getNodes(){
		return nodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb=new StringBuilder( "--Taxiway ID:  " + name);
		String nodesDescriptor = nodes.stream()
			.map(LatLong::toString)
			.collect(Collectors.joining(newLine));
		sb.append(nodesDescriptor);
		return sb.toString();
		
		
		
		
	}
	
	

}
