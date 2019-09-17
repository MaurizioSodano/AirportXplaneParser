package ms.xplaneparser.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Getter @Setter
public class Taxiway implements Comparable<Taxiway>  {
	private static final String newLine = System.getProperty("line.separator");
	private String name;
	
	private List<LatLong> nodes=new ArrayList<>();
	private boolean centerLine =false;
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
	@Override
	public String toString() {
		
		StringBuilder sb=new StringBuilder( "--Taxiway ID:  " + name);
		String nodesDescriptor = nodes.stream()
			.map(LatLong::toString)
			.collect(Collectors.joining(newLine));
		sb.append(nodesDescriptor);
		return sb.toString();

	}

	@Override
	public int compareTo(Taxiway t) {
		return name.compareTo(t.getName());
	}
	
	

}
