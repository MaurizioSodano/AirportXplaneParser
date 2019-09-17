package ms.xplaneparser.simplegui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

import javax.swing.JPanel;

import ms.xplaneparser.entity.Airport;
import ms.xplaneparser.entity.LatLong;
import ms.xplaneparser.entity.Taxiway;

public class PloyPane extends JPanel {


	private static final int MAX_WIDTH = 800;

	private static final int MAX_HEIGHT = 600;
	private double maxX;
	private double minX;
	private double maxY;
	private double minY;
	private double spanX;
	private double spanY;
	private static final long serialVersionUID = 1L;
	private int[] xPoints;
	private int[] yPoints;
	private List<XYList> xyPoints = new ArrayList<>();

	private Airport airport;



	private void setScale() {
		spanX = (maxX - minX);
		spanY = (maxY - minY);
			}

	public PloyPane(Airport airport) {

		this.airport = airport;
		addComponentListener(new ResizeListener());
		OptionalDouble optMaxLat = airport.taxiPoints.values().stream().mapToDouble(LatLong::getLatitude).max();
		OptionalDouble optMaxLon = airport.taxiPoints.values().stream().mapToDouble(LatLong::getLongitude).max();
		OptionalDouble optMinLat = airport.taxiPoints.values().stream().mapToDouble(LatLong::getLatitude).min();
		OptionalDouble optMinLon = airport.taxiPoints.values().stream().mapToDouble(LatLong::getLongitude).min();

		maxX = optMaxLat.orElse(0.0);
		minX = optMinLat.orElse(0.0);
		maxY = optMaxLon.orElse(0.0);
		minY = optMinLon.orElse(0.0);

		setScale();

		drawPolygons();

	}

	private void drawPolygons() {

		airport.taxiways.forEach(this::addTaxiway);
	}

	class ResizeListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			drawPolygons();
		}
	}

	private void addTaxiway(Taxiway taxiway) {
		int[] currXPoints = taxiway.getNodes().stream()
				.mapToInt(node -> (int)((node.getLatitude()-minX)*getWidth()/spanX ))
				.toArray();
		int[] currYPoints = taxiway.getNodes().stream()
				.mapToInt(node -> (int)((node.getLongitude()-minY)*getHeight()/spanY ))
				.toArray();
		
		xyPoints.add(new XYList(currXPoints, currYPoints));
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(MAX_WIDTH, MAX_HEIGHT);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();

		xyPoints.forEach(xy -> {
			g2d.setColor(Color.CYAN);
			xPoints = xy.xPoints;
			yPoints = xy.yPoints;
			g2d.setStroke(new BasicStroke(2));
			g2d.drawPolyline( xPoints, yPoints, xPoints.length);
		});

		g2d.setColor(Color.black);
		g2d.dispose();
	}

	private Color getRandomColor() {
		Random r = new Random();
		return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
	}

	public class XYList {
		public XYList(int[] xPoints, int[] yPoints) {
			this.xPoints = xPoints;
			this.yPoints = yPoints;
		}

		public int [] xPoints;
		public int [] yPoints;
	}
}