package mats_tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestSuite {

	public static void main(String[] args) { 	
		junit.textui.TestRunner.run(AllTests.class);
	} 
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for MATS User System Application"); 
		suite.addTest(new 	TestSuite(MATSComponentTest.class)); 	
		suite.addTest(new 	TestSuite(MATSControllerTest.class)); 
		suite.addTest(new 	TestSuite(MATSModelTest.class));
		suite.addTest(new 	TestSuite(MATSProjectCollectionTest.class));
		suite.addTest(new 	TestSuite(MATSSubprojectTest.class));
		suite.addTest(new 	TestSuite(MATSTaskStateTest.class));
		suite.addTest(new 	TestSuite(MATSTaskTest.class));
		suite.addTest(new 	TestSuite(MATSUserDatabaseTest.class));
		suite.addTest(new 	TestSuite(MATSUserTest.class));
		suite.addTest(new 	TestSuite(MATSViewTest.class));
		
		return suite; 
		
	} 
} 


