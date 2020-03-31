package xplaneparser.entity

import ms.xplaneparser.entity.LineTypes
import org.junit.Assert
import org.junit.Test

class LineTypesTest {
    @get:Test
    val isTaxiwayTest: Unit
        get() {
            val taxicenterline = "1"
            Assert.assertTrue(LineTypes.isTaxiwayCenterLine(taxicenterline))
            val taxicenterline2 = "101"
            Assert.assertTrue(LineTypes.isTaxiwayCenterLine(taxicenterline2))
        }

    @get:Test
    val isNotTaxiwayTest: Unit
        get() {
            val taxicenterline = "20"
            Assert.assertFalse(LineTypes.isTaxiwayCenterLine(taxicenterline))
            val taxicenterline2 = ""
            Assert.assertFalse(LineTypes.isTaxiwayCenterLine(taxicenterline2))
        }
}