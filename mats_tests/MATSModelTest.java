//Apurva
package mats_tests;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import mats_model.*;
import junit.framework.TestCase;

public class MATSModelTest extends TestCase {

	MATSSubproject subproject1;
	MATSSubproject subproject2;
	MATSSubproject subproject3;
	
	MATSUser Raj;
	MATSUser Apurva;
	MATSUser Brian;
	MATSUser Eug;
	
	MATSTask task1;
	MATSTask task2;
	MATSTask task3;
	MATSTask task4;
	MATSTask task5;
	
	MATSUserDatabase userDatabase;
	String users;
	String currentDate;
	
	MATSModel model;
	
	protected void setUp() throws Exception {
		super.setUp();
		 model = new MATSModel();
		
		 Calendar c = Calendar.getInstance();
		 Date cDate =  c.getTime();
		 Date cdDate = new Date(cDate.getYear(), cDate.getMonth(), cDate.getDate());
		
		 currentDate = cdDate.toString();
		 //currentDate = "Mon Mar 03 00:00:00 EST 2008"; //WHEN TESTING THIS CLASS, CHANGE THIS TO THE CURRENT
															//DATE IN ORDER FOR TESTS TO PASS
		 
		 Raj = new MATSUser ("Raj", "Password");
		 Apurva = new MATSUser ("Apurva", "Password2");
		 Brian = new MATSUser ("Brian", "Password");
		 Eug = new MATSUser ("Eug", "Password2");
		 
		 task1 = new MATSTask(Raj, Apurva, new Date ("03/05/08"), "task1", "Do task1");
		 task2 = new MATSTask(Brian, Eug, new Date ("03/10/08"), "task2", "Do task2");	
		 task3 = new MATSTask(Brian, Eug, new Date ("03/01/08"), "task3", "Do task2");
		 task4 = new MATSTask(Apurva, Raj, new Date ("03/05/08"), "task4", "Do task4");
		 task5 = new MATSTask(Eug, Apurva, new Date ("03/05/08"), "task5", "Do task5");
		 
		
		 subproject1 = new MATSSubproject("subproject1", "description1");
		 subproject2 = new MATSSubproject("subproject2", "description2");
		 subproject3 = new MATSSubproject("subproject3", "description3");
		 
		userDatabase = new MATSUserDatabase();
		
		userDatabase.addUser("Raj", "Password");
		userDatabase.addUser("Brian", "Password");
		userDatabase.addUser("Eug", "Password2");
		
		users = "Users: \n";
		

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMATSModel() {	//the construct does not set any values or give it a state
									//so don't need to test this
		
	}

	public void testLogin() throws Exception {
		setUp();
		

		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();	//have to log out as this sets the current user
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();

		assertFalse(model.login("Apurva", "Password")); //Apurva's not in user database so returns null
		assertFalse(model.login("Raj", "password34")); //Raj is in database, but password is incorrect as is uncapitalized
		assertTrue(model.login("Raj", "Password34"));
		model.logout();
		
		assertFalse(model.login("Brian", "Pass1")); //Brian is in database but password is incorrect
		assertTrue(model.login("Brian", "Pass"));
		
		assertFalse(model.login("Brian", "Pass1")); //Brian is in database but password is incorrect
		assertTrue(model.login("Eug", "password34")); //login as another person without logging out
		
		tearDown();
	}

	public void testLoggedIn() throws Exception {
		setUp();
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();	//have to log out as this sets the current user
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass");
		
		assertFalse(model.loggedIn("Apurva")); //Apurva is not currently logged in
		assertFalse(model.loggedIn("Walker")); //Walker isn't even in database
		assertTrue(model.loggedIn("Brian"));
		
		model.logout(); //logout Brian
		model.login("Raj", "Password34");
		
		assertTrue(model.loggedIn("Raj"));	//Now raj has logged in
		assertFalse(model.loggedIn("Eug"));
		
		tearDown();
	}

	public void testLogout() throws Exception {
		setUp();
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		assertNull(model.getWhoAmI());
		
		model.login("Brian", "Pass");	//Brian logged in
		
		assertTrue(model.getWhoAmI().toString().equals(userDatabase.findUser("Brian").toString()));
		
		model.logout(); //Brian logged out
		
		assertNull(model.getWhoAmI());
		
		model.logout(); //if logout is called again (which it can't be), should still return null
		
		assertNull(model.getWhoAmI());
		
		model.login("Raj", "Password34"); //Now Raj logs in
		
		assertTrue(model.getWhoAmI().toString().equals(userDatabase.findUser("Raj").toString()));
		tearDown();
	}

	public void testAddUser() throws Exception {
		
		setUp();
		assertNull(model.getWhoAmI());
		
		model.addUser("Apurva", "pass"); //Apurva got added and should be the current user	
		assertNull(model.getWhoAmI());
		assertTrue(model.login("Apurva","pass"));
		assertTrue(model.getWhoAmI().toString().equals("Apurva"));
		model.logout();//logout Apurva
		
		
		model.addUser("Brian","Pass"); //Brian is added to the database
		assertTrue(model.login("Brian","Pass"));
		assertTrue(model.getWhoAmI().toString().equals("Brian"));
		assertFalse(model.getWhoAmI().toString().equals("Apurva"));
		tearDown();
	}


	public void testAssignTask() throws Exception {
		setUp();
		//subproject1.addComponent(task1); //task1 is added into subproject1
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		assertTrue(model.login("Raj", "Password34")); //Raj is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Brian", new Date ("03/05/08"), "Do task1");
		
		model.addNewProject("Project2", "descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");
		
		model.addComponent("task2", "Task", "Project2", "Eug",new Date ("03/05/08"), "Do task1");
		model.logout(); //Raj logs out
		
		MATSTask t = (MATSTask) model.getComponent("task1");
		
		assertTrue(model.login ("Eug", "password34")); //person with no authority tries to reassign task
		model.assignTask("task1","Raj");
		
		t= (MATSTask) model.getComponent("task1");
		assertFalse(t.getOwner().equals("Raj"));
		model.logout();
		
		assertTrue(model.login("Raj", "Password34"));
		model.assignTask("task1", "Brian"); //person who has authority tries to reassign task
		t= (MATSTask) model.getComponent("task1");

		assertTrue(t.getOwner().equals("Brian"));
		
		model.assignTask("task1","Raj"); //assigning a task to one's self
		t= (MATSTask) model.getComponent("task1");
		
		assertTrue(t.getOwner().equals("Raj"));
		
		t= (MATSTask) model.getComponent("task1");
		model.assignTask("task1", "Walker"); //try assigning a task to someone not in database
		assertFalse(t.getOwner().equals("Walker"));
		tearDown();
	}

	public void testAddNewProject() throws Exception {
		setUp();
		assertNull(model.getComponent("project1")); //project1 is not in the project collection yet
		model.addNewProject("project1", "description"); // add the project
		
		assertTrue(model.getComponent("project1").getName().equals("project1"));  //project1 is in the model now
		
		assertNull(model.getComponent("project2")); //project2 hasn't been added yet
		model.addNewProject("project2", "description"); //it's added now
		
		assertTrue(model.getComponent("project2").getName().equals("project2"));
		
		assertTrue(model.getComponent("project1").getName().equals("project1"));  //project1 is still in there at this point
		
		tearDown();
	}

	public void testGetTasksOwned() throws Exception {
		setUp();
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		String tasksOwned = "The tasks owned by user Rajare:\n";
		
		assertTrue(model.login("Brian", "Pass")); //Brian is th ecreator right now
		
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task1"); //Raj is owner of another task
		
		
		model.getTasksOwned("Raj");
		assertFalse(model.getTasksOwned("Raj").toString().equals(tasksOwned)); //Havn't updated the comparing string yet
		
		tasksOwned= "task1";
	
		assertTrue(model.getTasksOwned("Raj").get(0).equals(tasksOwned)); //Updated the string
		
		
		
		model.addNewProject("Project2","descrip");
		model.addComponent("task2","Task", "Project2", "Raj", new Date ("03/05/08"), "Do task1"); //Raj is owner of another task
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");
		model.addComponent("task3", "Task", "subproject1", "Eug",new Date ("03/05/08"), "Do task1"); //the task that Raj owns

		
			
		assertFalse(model.getTasksOwned("Raj").get(1).equals(tasksOwned));;
		

		assertFalse(model.getTasksOwned("Raj").toString().equals(tasksOwned)); //Havn't updated the comparing string yet
		//System.out.println(model.getDisplayString());
		
		tasksOwned= "task2";
		//System.out.println(tasksOwned);
		assertTrue(model.getTasksOwned("Raj").get(1).equals(tasksOwned)); //Updated the string
		
		tearDown();
		
	}

	public void testGetTasksCreated() throws Exception {
		setUp();
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		String tasksCreated = "The tasks created by user Brianare:\n";
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		
		tasksCreated= "task1";
		assertTrue(model.getTasksCreated("Brian").size()==0); //Havn't updated the comparing string yet
		
		
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task1"); //Raj is owner of another task
		

		assertTrue(model.getTasksCreated("Brian").get(0).equals(tasksCreated)); //Updated the string
		
		
		model.addNewProject("Project2", "descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");
		
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task1");
		assertFalse(model.getTasksCreated("Brian").get(1).equals(tasksCreated)); //Updated the string
		tasksCreated= "task2";	//will find task3 first with our traversal
		
		assertTrue(model.getTasksCreated("Brian").get(1).equals(tasksCreated)); //Updated the string
		
		
		model.addComponent("task3", "Task", "subproject1", "Eug",new Date ("03/05/08"), "Do task1"); //the task that Raj owns
		
		assertFalse(model.getTasksCreated("Brian").get(1).equals(tasksCreated)); //Havn't updated the comparing string yet
		tasksCreated="task3";
		//System.out.println(tasksCreated);
		//System.out.println(model.getDisplayString());
		assertTrue(model.getTasksCreated("Brian").get(1).equals(tasksCreated)); //Updated the string
		//In our traversal, task3 will be found first
		
		tearDown();
		
	}

	public void testFindComponent() {
		//method basically calls another method already tested in MATSProjectCollection
		//so we run some simple tests to make sure it still works
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("subproject2", "Subproject", "Project1", "Eug", new Date ("03/05/08"), "Do task1");
		model.addComponent("task1","Task", "subproject2", "Raj", new Date ("03/05/08"), "Do task1"); //Raj is owner of another task
		
		assertNotNull(model.getComponent("task1"));
		//System.out.println(model.findComponent("task1").getName());
		assertTrue(model.getComponent("task1").getName().equals("task1")); //task1 should be found now
		assertNull(model.getComponent("task2")); //task2 is not added yet
		
		
		model.addNewProject("Project2", "descrip");
		
		assertNull(model.getComponent("subproject1")); //subproject1 not added yet
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");
		assertTrue(model.getComponent("subproject1").getName().equals("subproject1")); //subproject1 was added		
		
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task1");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task1"); //the task that Raj owns
		assertTrue(model.getComponent("task3").getName().equals("task3")); //subproject1 was added		
	}

	public void testGetWhoAmI() {
		
		assertNull(model.getWhoAmI()); 
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.login("Brian", "Pass");
		assertTrue(model.getWhoAmI().toString().equals("Brian"));	//Brian is added and logged in	
		
		model.logout();
		assertNull(model.getWhoAmI()); //Brian was logged out
		model.addUser("Raj", "Password34");
		model.login("Raj", "Password34");
		assertTrue(model.getWhoAmI().toString().equals("Raj"));
		model.logout(); //Raj logs out
		
		assertNull(model.getWhoAmI());
		model.login("Brian", "Pass"); //Brian logs in and should set WhoAmI
		
		assertTrue(model.getWhoAmI().toString().equals("Brian"));	//Brian is added and logged in	

	}

	public void testContainsUser() {
		assertFalse(model.containsUser("Brian")); //Brian and Raj aren't in database right now
		assertFalse(model.containsUser("Raj"));
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		assertTrue(model.containsUser("Brian")); //is Brian in user database even after he logs out
		model.addUser("Raj", "Password34"); //add Raj to the database
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		assertTrue(model.containsUser("Brian"));
		assertTrue(model.containsUser("Eug"));
		assertFalse(model.containsUser("Apurva")); //Apurva's not in the database
	}

	public void testGetAllUsers() {
		
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		List<String> users = new ArrayList<String>();
		assertFalse(model.getAllUsers().toString().equals(users.toString()));
		users.add("Brian");
		//model.printAllUsers();

		assertTrue(model.getAllUsers().toString().equals(users.toString()));
		
		model.logout();
		


		users.add("Raj"); //add Raj to the list of users used to compare (we have a traversal that puts Raj first)
		//model.printAllUsers();
		assertFalse(model.getAllUsers().toString().equals(users.toString()));
		model.addUser("Raj", "Password34"); //add Raj to the database
		//model.printAllUsers();


		assertTrue(model.getAllUsers().toString().equals(users.toString()));
		model.logout();
		
		users.add("Eug");
		
		
		model.addUser("Eug", "password34"); //add Eugene to the user database
		model.logout();
		
		//model.printAllUsers();
		//System.out.println(model.getDisplayString());
		//System.out.println(users3);
		assertTrue(model.getAllUsers().toString().equals(users.toString()));
		
		model.addUser("Random", "test");
		//model.printAllUsers();
		assertFalse(model.getAllUsers().toString().equals(users.toString())); //Test should no longer pass as number of users have been modified
		
		
	}

	public void testFindTasksOfState() {
		String state = "ONGOING";
		List <String> tasks = new ArrayList<String> ();;
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task1"); //Raj is owner of another task	
		
		
		assertFalse(model.findTasksOfState("ONGOING").toString().equals(tasks.toString()));
		tasks.add("task1");

		assertTrue(model.findTasksOfState("ONGOING").toString().equals(tasks.toString()));
		
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");	
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task1");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task1");
		model.findTasksOfState("ONGOING");
		
		assertFalse(model.findTasksOfState("ONGOING").toString().equals(tasks.toString()));
		tasks.add("task3");	//task3 will be found first as it's in subproject1
		tasks.add("task2");

		assertTrue(model.findTasksOfState("ONGOING").toString().equals(tasks.toString()));
		
		String state1 = "COMPLETED";
		List<String> tasks1= new ArrayList<String>();
		
		model.changeTaskState("task1", "COMPLETED");
		model.findTasksOfState("COMPLETED");
		assertFalse(model.findTasksOfState("COMPLETED").toString().equals(tasks1.toString()));
		tasks1.add("task1");
		//System.out.println(tasks1);
		//System.out.println(model.getDisplayString());
		assertTrue(model.findTasksOfState("COMPLETED").toString().equals(tasks1.toString()));
		
		
	}

	public void testChangeDeadline() {
		String oldDate;
		String newDate;
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task1"); //Raj is owner of another task	
		MATSTask t = (MATSTask) model.getComponent("task1");
		oldDate = t.getDeadline();
		
		model.changeDeadline("task1", new Date ("03/02/08")); //Brian can change the deadline but can't change to a passed date
		t = (MATSTask) model.getComponent("task1");
		newDate = t.getDeadline();
		
		assertTrue(newDate.equals(oldDate)); //the date should not change
		

		model.changeDeadline("task1", new Date ("04/25/08")); //Brian can change the deadline
		t = (MATSTask) model.getComponent("task1");
		oldDate = t.getDeadline();
		
		assertFalse(newDate.equals(oldDate));
		
		newDate = (new Date("04/25/08")).toString();
		
		assertTrue(newDate.equals(oldDate));
		
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");	
		
		
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task1");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task1");
		model.logout();
		
		model.login("Raj", "Password34");
		t = (MATSTask) model.getComponent("task1");
		oldDate = t.getDeadline();
		
		model.changeDeadline("task1", new Date ("03/02/08")); //Raj can't change the deadline but can't change to a passed date
		t = (MATSTask) model.getComponent("task1");
		newDate = t.getDeadline();
		
		assertTrue(newDate.equals(oldDate)); //the date should not change
		

		
	}

	public void testRefresh() {
		
		int oldState;
		int newState;
		
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("04/25/08"), "Do task1"); //Raj is owner of another task	
		
		MATSTask t = (MATSTask) model.getComponent("task1");
		oldState = t.getState();
		
		model.refresh(); //task1 is not overdue
		t = (MATSTask) model.getComponent("task1");
		newState = t.getState();
		assertTrue(oldState==newState);
		
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");	
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("04/25/08"), "Do task1");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/01/08"), "Do task1");
		
		t = (MATSTask) model.getComponent("task3");
		oldState = t.getState();
		
		model.refresh(); //task3 is overdue and should have its state chagned accordingly
		t = (MATSTask) model.getComponent("task3");
		newState = t.getState();
		assertFalse(oldState==newState);
		assertTrue(newState==1);
		  
		
		 
		assertTrue(true);
	}

	public void testChangeTaskState() {
		int oldState;
		int newState;
		
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("04/25/08"), "Do task1"); //Raj is owner of another task	

		MATSTask t = (MATSTask) model.getComponent("task1");
		oldState = t.getState();
		
		
		model.changeTaskState("task1","OVERDUE"); //Brian shouldn't be allowed to change the state
		t = (MATSTask) model.getComponent("task1");
		newState = t.getState();
		
		assertTrue(newState==oldState); //the state shouldn't change
		
		model.changeTaskState("task1","DELETED"); //Brian should be allowed to change the state
		t = (MATSTask) model.getComponent("task1");
		newState = t.getState();
		
		assertFalse(newState==oldState);
		assertTrue(newState==MATSTaskState.DELETED);
		
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");	
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("04/25/08"), "Do task1");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task1");
		model.logout();
		
		model.login("Raj", "Password34"); //Rak is not the creator for anything so should not be able to modify anything
		
		t = (MATSTask) model.getComponent("task2");
		oldState = t.getState();
		
		model.changeTaskState("task2","DELETED");
		t = (MATSTask) model.getComponent("task2");
		newState = t.getState();
		assertTrue(newState==oldState);
		
		assertTrue(true);
	}

	public void testChangeTaskOwner() throws Exception {
		//same code as assign_task method, but allowed smelly code at this point
		setUp();
		subproject1.addComponent(task1); //task1 is added into subproject1
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Raj", "Password34"); //Raj is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task1");
		
		model.addNewProject("Project2", "descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");
		
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task1");
		model.logout(); //Raj logs out
		
		model.login ("Eug", "password34"); //person with no authority tries to reassign task
		model.changeTaskOwner("task1","Raj");
		assertFalse(task1.getOwner().equals("Raj"));
		model.logout();
		
		model.login("Raj", "Password34");
		model.changeTaskOwner("task1", "Brian"); //person who has authority tries to reassign task
		MATSTask t = (MATSTask) model.getComponent("task1");
		
		
		assertTrue(t.getOwner().equals("Brian"));
		
		model.changeTaskOwner("task1","Raj"); //assigning a task to one's self
		assertFalse(task1.getOwner().equals("Raj"));
		
		model.changeTaskOwner("task1", "Walker"); //try assigning a task to someone not in database
		assertFalse(task1.getOwner().equals("Walker"));
		tearDown();
	}
	
	public void testMoveProject() throws Exception {
		setUp();
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Raj", "Password34"); //Raj is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task1");
		
		model.addNewProject("Project2", "descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");
		
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task1");

		assertFalse(model.moveProject("Project2","subproject1")); //this should not be allowed
		
		assertTrue(model.moveProject("Project2", "Project1"));
		
		MATSSubproject subproj = (MATSSubproject) model.getComponent("Project1");
		assertTrue(subproj.containsSubproject("Project2")); //Project2 was moved properly
		
		assertFalse(model.moveProject("Project3","Project2")); //invalid name
		
		assertTrue(model.moveProject("Project2","Project Collection")); //should move it under project collection
		assertTrue(model.getNames().contains("Project2")); //Project 2 was placed properly
		
		
		
		tearDown();
		
	}

	public void testChangeComponentDescription() throws Exception {
		setUp();
		String description1;
		String description2;
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task1"); //Raj is owner of another task	
		model.changeComponentDescription("task1", "d"); //Brian can change the description
		
		description1= "d"; 
		assertTrue(model.getComponent("task1").getDescription().equals(description1));
		
		
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");	
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task1");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task1");
		model.logout();
		
		model.login("Raj", "Password34"); //Raj shouldn't have privilege to change description
		model.changeComponentDescription("task1", "d2");
		
		description2 = "d2";
		assertFalse(model.getComponent("task1").getDescription().equals(description2));
		assertTrue(model.getComponent("task1").getDescription().equals(description1));
		tearDown();
	}

	public void testPrintTaskHistory() throws Exception {
		setUp();
		String taskHistory =  "The history of this task is: \n";
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task1"); //Raj is owner of another task	
		
		taskHistory+= currentDate+ ": "+"This task was created on this date by Brian\n";
		
		model.getTaskHistory("task1");
		
		assertTrue(model.getTaskHistory("task1").equals(taskHistory));
		
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");	
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task1");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task1");
		
		taskHistory =  "The history of this task is: \n";
		taskHistory+= currentDate+ ": "+"This task was created on this date by Brian\n";
		
		assertTrue(model.getTaskHistory("task3").equals(taskHistory));
		
		model.changeTaskState("task3", "DELETED");
		model.getTaskHistory("task3");
		assertFalse(model.getTaskHistory("task3").equals(taskHistory)); //task history was changed
		
		taskHistory+= currentDate+": "+"The task's state was changed to DELETED\n";
		assertTrue(model.getTaskHistory("task3").equals(taskHistory));
		
		model.logout();
		tearDown();
		
	}

	public void testGetTaskState() throws Exception {
		setUp();
		String taskState =  "ONGOING";
		String taskState3 =  "ONGOING";
		String taskState4 =  "DELETED";
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task1"); //Raj is owner of another task	
				
		

		assertTrue(taskState.equals(model.getTaskState("task1")));
		
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");	
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task1");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task1");

		model.getTaskState("task3");
		assertTrue(taskState3.equals(model.getTaskState("task3")));
		assertFalse(taskState4.equals(model.getTaskState("task3")));
		
		model.changeTaskState("task3", "DELETED");
		assertFalse(taskState3.equals(model.getTaskState("task3")));
		model.getTaskState("task3");
		
		assertTrue(taskState4.equals(model.getTaskState("task3")));
		
		model.logout();
		tearDown();
	}

	public void testGetComponentDescription() throws Exception {
		setUp();
		String description1 = "The component with the name task1has a description of: \n ";
		description1 = "Do task4";
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task4"); //Raj is owner of another task	
		
		

		assertTrue(model.getComponentDescription("task1").equals(description1));
		
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/05/08"), "Do task1");	
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("03/05/08"), "Do task2");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task3");

		model.getComponentDescription("task2");
		assertFalse(model.getComponentDescription("task2").equals(description1));
		//description1 = "The component with the name task2has a description of: \n ";
		description1= "Do task2";
		assertTrue(model.getComponentDescription("task2").equals(description1));
		
	
		assertFalse(model.getComponentDescription("subproject1").equals(description1));
		//description1 = "The component with the name subproject1has a description of: \n ";
		description1= "Project description: \n";
		description1+= "Do task1";
		assertTrue(model.getComponentDescription("subproject1").equals(description1));
		
		tearDown();
		
	}

	public void testGetTaskDeadline() throws Exception {
		setUp();
		String deadline3 =  new Date ("04/25/08").toString();
		String deadline4 = new Date ("04/26/08").toString();
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("04/25/08"), "Do task4"); //Raj is owner of another task	
		
		
		model.getTaskDeadline("task1");
		String deadline1= "The task with the name task1has a deadline of ";
		deadline1=deadline3;
		assertTrue(deadline1.equals(model.getTaskDeadline("task1")));
		
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/06/08"), "Do task1");	
		model.addComponent("task2", "Task", "Project2", "Raj",new Date ("04/26/08"), "Do task2");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("04/27/08"), "Do task3");

		
		model.getTaskDeadline("task2");
		String deadline2= "The task with the name task2has a deadline of ";
		deadline2= deadline4;
		assertFalse(deadline1.equals(model.getTaskDeadline("task2")));

		assertTrue(deadline2.equals(model.getTaskDeadline("task2")));
		
		model.changeDeadline("task2", new Date("04/25/08"));
		model.getTaskDeadline("task2");
		deadline4 = new Date ("04/25/08").toString();
		deadline2 = deadline4;

		assertFalse(deadline1.equals(model.getTaskDeadline("task3")));
		assertTrue(deadline2.equals(model.getTaskDeadline("task1")));
		assertTrue(deadline2.equals(model.getTaskDeadline("task2")));
		
		tearDown();
	}

	public void testAddView() {
		//Don't have to implement as has to do with the interface with the view and can't be tested
		//as not testing our view class
	}

	public void testRemoveView() {
		//Don't have to implement as has to do with the interface with the view and can't be tested
		//as not testing our view class
	}

	public void testNotifyViews() {
		//Don't have to implement as has to do with the interface with the view and can't be tested
		//as not testing our view class
	}
	
	public void testIsSubproject() throws Exception {
		setUp();
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task4"); //Raj is owner of another task	
		
		model.getSubprojectDirectoryList("Project1");
		
		assertFalse(model.isSubproject("task1"));
		assertTrue(model.isSubproject("Project1"));
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/06/08"), "Do task1");	
		model.addComponent("task2", "Task", "subproject1", "Raj",new Date ("03/06/08"), "Do task2");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task3");

		assertTrue(model.isSubproject("subproject1"));
		assertFalse(model.isSubproject("task2"));
		assertFalse(model.isSubproject("task3"));
		
		tearDown();
	}
	
	public void testIsTask() throws Exception {
		setUp();
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task4"); //Raj is owner of another task	
		
		model.getSubprojectDirectoryList("Project1");
		
		assertFalse(model.isTask("Project1"));
		assertTrue(model.isTask("task1"));
		
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/06/08"), "Do task1");	
		model.addComponent("task2", "Task", "subproject1", "Raj",new Date ("03/06/08"), "Do task2");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task3");

		assertFalse(model.isTask("Project2"));
		assertTrue(model.isTask("task2"));
		assertTrue(model.isTask("task3"));
		
		tearDown();
	}

	public void testGetDirectorySubproject()throws Exception {
		setUp();
		String description1 = "";
		
		model.addUser("Brian", "Pass");	//add the people to the user databse in the model
		model.logout();
		model.addUser("Raj", "Password34");
		model.logout();
		model.addUser("Eug", "password34");
		model.logout();
		
		model.login("Brian", "Pass"); //Brian is the creator right now
		model.addNewProject("Project1","descrip");
		model.addComponent("task1","Task", "Project1", "Raj", new Date ("03/05/08"), "Do task4"); //Raj is owner of another task	
		
		model.getSubprojectDirectoryList("Project1");
		
		assertFalse(model.getSubprojectDirectoryList("Project1").get(0).equals(task1));
		description1= "task1";
		
		assertTrue(model.getSubprojectDirectoryList("Project1").get(0).equals(description1));
		
		model.addNewProject("Project2","descrip");
		model.addComponent("subproject1", "Subproject", "Project2", "", new Date ("03/06/08"), "Do task1");	
		model.addComponent("task2", "Task", "subproject1", "Raj",new Date ("03/06/08"), "Do task2");
		model.addComponent("task3", "Task", "subproject1", "Apurva",new Date ("03/05/08"), "Do task3");

		model.getSubprojectDirectoryList("subproject1");
		assertFalse(model.getSubprojectDirectoryList("subproject1").equals(description1));
		//description1= "The directory of this subproject is : \n";
		description1="task2"; //it will not be found in a weird order (our traversal is weird)
		
		
		assertNotNull(model.getComponent("subproject1"));
		
		assertTrue(model.getSubprojectDirectoryList("subproject1").get(0).equals(description1));
		
		description1="task3";
		assertFalse(model.getSubprojectDirectoryList("subproject1").get(0).equals(description1));
		assertTrue(model.getSubprojectDirectoryList("subproject1").get(1).equals(description1));
		
		tearDown();
	}

}
