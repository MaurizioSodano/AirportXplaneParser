package ms.xplaneparser.utility

import ms.xplaneparser.entity.LatLong
import java.util.stream.Collectors
import java.util.stream.IntStream

object BezierUtility {
    fun bezierQuadratic(steps: Int, vararg p: LatLong): List<LatLong> {
        return IntStream.range(0, steps + 1)
                .mapToDouble { step: Int -> step.toDouble() / steps }
                .mapToObj { mu: Double -> bezierQuadratic(mu, *p) }
                .map{it as LatLong}
                .collect(Collectors.toList() )
    }

    fun bezierQuadratic(mu: Double, vararg p: LatLong): LatLong? {
        val n = p.size
        return if (n == 3) {
            val mu2 = mu * mu
            val mum1 = 1 - mu
            val mum12 = mum1 * mum1
            val x = p[0].latitude * mum12 + 2 * p[1].latitude * mum1 * mu + p[2].latitude * mu2
            val y = p[0].longitude * mum12 + 2 * p[1].longitude * mum1 * mu + p[2].longitude * mu2
            LatLong(x, y)
        } else {
            null
        }
    }

    fun bezierCubic(steps: Int, vararg p: LatLong): List<LatLong> {
        return IntStream.range(0, steps)
                .mapToDouble { step: Int -> step.toDouble() / steps }
                .mapToObj { mu: Double -> bezierCubic(mu, *p) }
                .map{it as LatLong}
                .collect(Collectors.toList())
    }

    /**
     *
     * General Bezier curve q(t) = p1(1-t)3 + 3p2t(1-t)2 + 3p3t2 + p4t3
     * q(t)=p0(1-t)3+3*p1(t)(1-t)2+3*p2*t2*(1-t)+p3*t3
     */
    fun bezierCubic(mu: Double, vararg p: LatLong): LatLong? {
        val n = p.size
        return if (n == 4) {
            val mu3 = mu * mu * mu
            val mum1 = 1 - mu
            val mum13 = mum1 * mum1 * mum1
            val x = p[0].latitude * mum13 + 3 * p[1].latitude * mum1 * mum1 * mu + 3 * p[2].latitude * mu * mu * mum1 + p[3].latitude * mu3
            val y = p[0].longitude * mum13 + 3 * p[1].longitude * mum1 * mum1 * mu + 3 * p[2].longitude * mu * mu * mum1 + p[3].longitude * mu3
            LatLong(x, y)
        } else {
            null
        }
    }
}