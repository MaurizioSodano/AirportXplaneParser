package xplaneparser.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Taxiway implements Comparable<Taxiway>  {
	private static final String newLine = System.getProperty("line.separator");
	private String name;
	
	private List<LatLong> nodes=new ArrayList<>();
	private boolean centerline=false;
	private boolean runwayHold=false;
	
	public void addNode(double latitude,double longitude) {
		nodes.add(new LatLong(latitude,longitude));
	}
	public LatLong getLastNode() {
		if (nodes.isEmpty()) return null;
		return nodes.get(nodes.size()-1);
	}
	public void closeLinearLoop() {
		nodes.add(nodes.get(0));
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

	public void setCenterline(boolean centerline) {
		this.centerline=centerline;
	}
	
	public boolean isCenterline() {
		return centerline;
	}

	public boolean isRunwayHold() {
		return runwayHold;
	}
	public void setRunwayHold(boolean runwayHold) {
		this.runwayHold = runwayHold;
	}
	@Override
	public int compareTo(Taxiway t) {
		return name.compareTo(t.getName());
	}
	
	

}
