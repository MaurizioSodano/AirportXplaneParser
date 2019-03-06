package xplaneparser.utility;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BezierUtility {

	public static List<Point2D> bezier3(int steps, Point2D... p) {
		return IntStream.range(0, steps)
				.mapToDouble(step -> ((double) step) / steps)
				.mapToObj(mu -> bezier3(mu, p))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public static Point2D bezier3(double mu, Point2D... p) {
		int n = p.length;
		if (n == 3) {
			double mu2 = mu * mu;
			double mum1 = 1 - mu;
			double mum12 = mum1 * mum1;

			double x = p[0].getX() * mum12 + 2 * p[1].getX() * mum1 * mu + p[2].getX() * mu2;
			double y = p[0].getY() * mum12 + 2 * p[1].getY() * mum1 * mu + p[2].getY() * mu2;

			return new Point2D.Double(x, y);
		} else {
			return null;
		}
	}

	
	public static List<Point2D> bezier4(int steps, Point2D... p) {
		return IntStream.range(0, steps)
				.mapToDouble(step -> ((double) step) / steps)
				.mapToObj(mu -> bezier4(mu, p))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * 
	 * General Bezier curve q(t) = p1(1-t)3 + 3p2t(1-t)2 + 3p3t2 + p4t3
	 */
	public static Point2D bezier4(double mu, Point2D... p) {
		int n = p.length;
		if (n == 4) {
			double mu3 = mu * mu * mu;
			double mum1 = 1 - mu;
			double mum13 = mum1 * mum1 * mum1;

			double x = p[0].getX() * mum13 + 2 * p[1].getX() * mum1 * mum1 * mu + p[2].getX() * mu * mu * mum1
					+ p[3].getX() * mu3;
			double y = p[0].getX() * mum13 + 2 * p[1].getY() * mum1 * mum1 * mu + p[2].getY() * mu * mu * mum1
					+ p[3].getY() * mu3;

			return new Point2D.Double(x, y);
		} else {
			return null;
		}
	}
}
