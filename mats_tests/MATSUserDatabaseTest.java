//Apurva
package mats_tests;
import mats_model.*;

import junit.framework.TestCase;

public class MATSUserDatabaseTest extends TestCase {

	MATSUserDatabase userDatabase;
	String users;
	
	protected void setUp() throws Exception {
		super.setUp();
		userDatabase = new MATSUserDatabase();
		users = "The users of this database are: ";
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMATSUserDatabase() {//don't need to test the constructor
		
	}

	public void testAddUser() throws Exception {
		setUp();
		
		assertNull(userDatabase.findUser("Brian")); //Assume this method has been tested and works
		assertNull(userDatabase.findUser("Raj")); //No one is in the database so should return null
		
		assertTrue(userDatabase.findAll().equals(users));
		userDatabase.addUser("Brian", "Password");	//Brian was just added to the database
		users+= "Brian"+"\n";
		
		assertTrue((userDatabase.findAll()).equals(users)); //Brian is in the list of users
		
		userDatabase.addUser("Raj", "Password");	//Added another person with same password successfully
		users+="Raj"+"\n";
		
		assertTrue((userDatabase.findAll()).equals(users)); //Raj is added to the list of all users
		
		userDatabase.addUser("Raj","Password");
		
		assertTrue(userDatabase.findAll().equals(users)); //Raj was not added again so the list did not change
		tearDown();
		
	}

	public void testFindUser() throws Exception {
		setUp();
		
		assertNull(userDatabase.findUser("Brian")); //Assume this method has been tested and works
		assertNull(userDatabase.findUser("Raj")); //No one is in the database so should return null
		
		userDatabase.addUser("Brian", "Password");	//Brian was just added to the database
		users+="Brian\n";
		
		assertTrue(userDatabase.findAll().equals(users)); //Brian is in the list of users
		
		userDatabase.addUser("Raj", "Password");
		users+="Raj\n";
		
		userDatabase.addUser("Eug", "Password");
		users+="Eug\n";
		
		assertNull(userDatabase.findUser("Apurva")); //Apurva is not in the database
		assertNotNull(userDatabase.findUser("Brian")); //Brian is in the database
		tearDown();
	}

	public void testVerifyUser() throws Exception {
		setUp();
		assertNull(userDatabase.findUser("Brian")); //Assume this method has been tested and works
		assertNull(userDatabase.findUser("Raj")); //No one is in the database so should return null
		
		userDatabase.addUser("Brian", "Pass");	//Brian was just added to the database
		users+="Brian\n";
		
		assertTrue(userDatabase.findAll().equals(users)); //Brian is in the list of users
		
		userDatabase.addUser("Raj", "Password34");	//add Raj to list
		users+="Raj\n";
		
		userDatabase.addUser("Eug", "password34"); //add Eugene to list
		users+="Eug\n";
		
		assertNull(userDatabase.verifyUser("Brian", "pass"));	//testing with the password uncapitalized
		assertNull(userDatabase.verifyUser("Brian", "pass1")); //testing with an extra letter in password
		assertNotNull(userDatabase.verifyUser("Brian", "Pass"));
		
		assertNull(userDatabase.verifyUser("Apurva", "Password")); //Apurva's not in the database
		assertNull(userDatabase.verifyUser("Raj", "Pass"));	//testing with part of actual password in given password
		assertNotNull(userDatabase.verifyUser("Eug", "password34")); //should work
		tearDown();
	}

	public void testFindAll() throws Exception {
		setUp();
		assertTrue(userDatabase.findAll().equals(users)); //No one's in the list so should work
		
		userDatabase.addUser("Brian", "Pass");	//Brian was just added to the database
		users+="Brian\n";
		
		assertTrue(userDatabase.findAll().equals(users)); //Brian is in the list of users
		
		users+="Raj\n";
		assertFalse(userDatabase.findAll().equals(users)); //Havn't added Raj to list yet so shouldn't work
		
		userDatabase.addUser("Raj", "Password34");	//add Raj to list
		assertTrue(userDatabase.findAll().equals(users));
		
		users+="Eug\n";
		assertFalse(userDatabase.findAll().equals(users));
		
		userDatabase.addUser("Eug", "password34"); //add Eugene to list
		assertTrue(userDatabase.findAll().equals(users));
		tearDown();
	}
	
	public void testGetAllUsers() throws Exception {
		setUp();
		
		userDatabase.addUser("Brian", "Pass"); //Add Brian to database
		assertEquals (userDatabase.getAllUsers().get(0), "Brian");
		
		userDatabase.addUser("Eug", "Pass"); //Add Eug to database
		assertFalse(userDatabase.getAllUsers().get(1).equals( "Brian"));
		assertTrue (userDatabase.getAllUsers().get(1).equals("Eug"));
		
		
		
		
		tearDown();
		
	}

}
