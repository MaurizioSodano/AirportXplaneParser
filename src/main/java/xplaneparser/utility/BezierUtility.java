package xplaneparser.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import xplaneparser.entity.LatLong;

public class BezierUtility {

	public static List<LatLong> bezierQuadratic(int steps, LatLong... p) {
		return IntStream.range(0, steps)
				.mapToDouble(step -> ((double) step) / steps)
				.mapToObj(mu -> bezierQuadratic(mu, p))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public static LatLong bezierQuadratic(double mu, LatLong... p) {
		int n = p.length;
		if (n == 3) {
			double mu2 = mu * mu;
			double mum1 = 1 - mu;
			double mum12 = mum1 * mum1;

			double x = p[0].latitude * mum12 + 2 * p[1].latitude * mum1 * mu + p[2].latitude * mu2;
			double y = p[0].longitude * mum12 + 2 * p[1].longitude * mum1 * mu + p[2].longitude * mu2;

			return new LatLong(x, y);
		} else {
			return null;
		}
	}

	
	public static List<LatLong> bezierCubic(int steps, LatLong... p) {
		return IntStream.range(0, steps)
				.mapToDouble(step -> ((double) step) / steps)
				.mapToObj(mu -> bezierCubic(mu, p))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * 
	 * General Bezier curve q(t) = p1(1-t)3 + 3p2t(1-t)2 + 3p3t2 + p4t3
	 * q(t)=p0(1-t)3+3*p1(t)(1-t)2+3*p2*t2*(1-t)+p3*t3
	 */
	public static LatLong bezierCubic(double mu, LatLong... p) {
		int n = p.length;
		if (n == 4) {
			double mu3 = mu * mu * mu;
			double mum1 = 1 - mu;
			double mum13 = mum1 * mum1 * mum1;

			double x = p[0].latitude * mum13 + 3 * p[1].latitude * mum1 * mum1 * mu + 3*p[2].latitude * mu * mu * mum1
					+ p[3].latitude * mu3;
			double y = p[0].longitude * mum13 + 3 * p[1].longitude * mum1 * mum1 * mu + 3*p[2].longitude * mu * mu * mum1
					+ p[3].longitude * mu3;

			return new LatLong(x, y);
		} else {
			return null;
		}
	}


}
