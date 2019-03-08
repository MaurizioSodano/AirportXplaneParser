package xplaneparser.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineTypesTest {

	
	   @Test
	   public void isTaxiwayTest() {
		   String taxicenterline="1";
		   assertTrue(LineTypes.isTaxiwayCenterline(taxicenterline));
		   String taxicenterline2="101";
		   assertTrue(LineTypes.isTaxiwayCenterline(taxicenterline2));
	   }
	   
	   
	   @Test
	   public void isNotTaxiwayTest() {
		   String taxicenterline="20";
		   assertFalse(LineTypes.isTaxiwayCenterline(taxicenterline));
		   String taxicenterline2="";
		   assertFalse(LineTypes.isTaxiwayCenterline(taxicenterline2));
	   }
}
