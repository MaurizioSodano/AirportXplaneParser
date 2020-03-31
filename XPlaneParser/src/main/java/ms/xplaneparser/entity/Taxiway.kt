package ms.xplaneparser.entity

import java.util.*
import java.util.stream.Collectors

class Taxiway : Comparable<Taxiway> {
    var name: String? = null
    var nodes: MutableList<LatLong> = ArrayList()
    var isCenterLine = false
    var isRunwayHold = false
    fun addNode(latitude: Double, longitude: Double) {
        nodes.add(LatLong(latitude, longitude))
    }

    val lastNode: LatLong?
        get() = if (nodes.isEmpty()) null else nodes[nodes.size - 1]

    fun closeLinearLoop() {
        nodes.add(nodes[0])
    }

    override fun toString(): String {
        val sb = StringBuilder("--Taxiway ID:  $name")
        val nodesDescriptor = nodes.stream()
                .map { obj: LatLong -> obj.toString() }
                .collect(Collectors.joining(newLine))
        sb.append(nodesDescriptor)
        return sb.toString()
    }

    override fun compareTo(t: Taxiway): Int {
        return name!!.compareTo(t.name!!)
    }



    companion object {
        private val newLine = System.getProperty("line.separator")
    }
}