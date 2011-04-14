//Raj
package mats_tests;
import mats_model.*;

import junit.framework.TestCase;
/*
 * Class used to test the MATSUser class methods to see if they return the proper output
 */
public class MATSUserTest extends TestCase {
	
	MATSUser Raj;	//create two MATSUsers to test the methods
	MATSUser Apurva;

	protected void setUp() throws Exception {
		super.setUp();
		Raj = new MATSUser ("Raj","Password" );	//initialize the two MATSUser's with names and passwords
		Apurva = new MATSUser ("Apurva", "Password2");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMATSUser() {	//Don't need to test the constructor method for now
		
	}
	
	/*
	 * Testing the getPassword() method in the MATSUser class to see if it returns the right output
	 */
	public void testGetPassword() throws Exception {
		setUp();
		
		String testPass = "Password";	
		
		assertFalse(Apurva.getPassword().equals(testPass));	//testing with one password having a letter missing
		assertTrue(Raj.getPassword().equals(testPass));	//should be the same password
		
		testPass = "password";
		assertFalse(Apurva.getPassword().equals(testPass));	//testing with one password having a letter missing 
		assertFalse(Raj.getPassword().equals(testPass));	//testing with one password not being capitalized
															//passwords are case-sensitive
		testPass = "Pass";	//testing with a password that shouldn't work under any circumstances
		assertFalse(Apurva.getPassword().equals(testPass));
		assertFalse(Raj.getPassword().equals(testPass));
		
		testPass = "password2";
		
		assertFalse(Apurva.getPassword().equals(testPass));	//testing with one password not being capitalized
		assertFalse(Raj.getPassword().equals(testPass));	//testing with one password with extra character
		
		testPass = "Password2";	
		
		assertTrue(Apurva.getPassword().equals(testPass));	//should be the same password
		assertFalse(Raj.getPassword().equals(testPass));	//testign wtih one password with extra character
		
		tearDown();
	}

	/*
	 * Testing the toString() method in the MATSUser class to see that it returns the right output
	 */
	public void testToString() throws Exception {
		setUp();
		
		String testUser = "Raj";
		
		assertFalse(Apurva.toString().equals(testUser));	//testing with not the same name as Apurva
		assertTrue(Raj.toString().equals(testUser));		//testing with the same names
		
		testUser = "raj";
		assertFalse(Apurva.toString().equals(testUser));	//testing with not the same name as Apurva
		assertFalse(Raj.toString().equals(testUser));	//testing with the same name as Raj, but one not capitalized
		
		testUser = "Brian";	//testing with a name that shouldn't work under any circumstances
		assertFalse(Apurva.toString().equals(testUser));
		assertFalse(Raj.toString().equals(testUser));
		
		testUser = "Apurva";
		
		assertTrue(Apurva.toString().equals(testUser));	//testing with a name that is the same
		assertFalse(Raj.toString().equals(testUser));	//testing with a different name
		
		testUser = "apurvaR";
		
		assertFalse(Apurva.toString().equals(testUser));	//testing with a name that's the same but with one extra letter
		assertFalse(Raj.toString().equals(testUser));	//testing with a different method
		
		tearDown();
	}

}
