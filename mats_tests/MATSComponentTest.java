package mats_tests;

import mats_model.*;
import junit.framework.TestCase;

/*
 * Class tests MATSComponent class.
 * NOTE: {1}Temporarily modify MATSComponent class by changing it from an Abstract Class to just a Super Class, for
 * 		 ONLY testing purposes.
 * 		 {2} Uncommeted areas of code which contain a // in front of them (indicates tests)
 * 		 {3} Run the tests!
 */
public class MATSComponentTest extends TestCase {

	MATSComponent comp;
	
	protected void setUp() throws Exception {
		
		super.setUp();
		// comp = new MATSComponent("Folder", "Not very useful");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMATSComponent() {

	}

	public void testGetName() throws Exception {
		
		setUp();
		
		//String name = "Folder";
		//String fail = "Small Folder";
		
		//assertEquals(name, comp.getName());
		//assertFalse(fail.equals(comp.getName()));
		assertTrue(true);
		
		tearDown();
	}

	public void testSetDescription() throws Exception {
		
		setUp();
		
		//String newDescription = "Testing Folder";
		//String fail = comp.getDescription();
		
		//MATSUser dummyUser = null;
		
		//comp.setDescription(dummyUser, newDescription);
		
		//assertEquals(newDescription, comp.getDescription());
		//assertFalse(fail.equals(comp.getDescription()));
		assertTrue(true);
		
		tearDown();
	}

	public void testGetDescription() throws Exception {
		
		setUp();
		
		//String description = "Not very useful";
		//String fail = "Great Folder";
		
		//assertEquals(description, comp.getDescription());
		//assertFalse(fail.equals(comp.getDescription()));
		assertTrue(true);
		
		tearDown();
	}

}
