package xplaneparser.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ms.xplaneparser.entity.LineTypes;

public class LineTypesTest {

	
	   @Test
	   public void isTaxiwayTest() {
		   String taxicenterline="1";
		   assertTrue(LineTypes.isTaxiwayCenterLine(taxicenterline));
		   String taxicenterline2="101";
		   assertTrue(LineTypes.isTaxiwayCenterLine(taxicenterline2));
	   }
	   
	   
	   @Test
	   public void isNotTaxiwayTest() {
		   String taxicenterline="20";
		   assertFalse(LineTypes.isTaxiwayCenterLine(taxicenterline));
		   String taxicenterline2="";
		   assertFalse(LineTypes.isTaxiwayCenterLine(taxicenterline2));
	   }
}
