//Raj
package mats_tests;
import mats_model.*;

import java.util.*;
import junit.framework.TestCase;
public class MATSTaskTest extends TestCase {
	
	MATSUser Raj;
	MATSUser Apurva;
	MATSUser Brian;
	MATSUser Eug;
	
	MATSTask task1;
	MATSTask task2;
	MATSTask task3;
	
	String currentDate;

	protected void setUp() throws Exception {
		super.setUp();
		Calendar c = Calendar.getInstance();
		 Date cDate =  c.getTime();
		 Date cdDate = new Date(cDate.getYear(), cDate.getMonth(), cDate.getDate());
		
		 currentDate = cdDate.toString();
		 //currentDate = "Mon Mar 03 00:00:00 EST 2008"; //WHEN TESTING THIS CLASS, CHANGE THIS TO THE CURRENT
		 												//DATE IN ORDER FOR TESTS TO PASS
		 Raj = new MATSUser ("Raj", "Password");
		 Apurva = new MATSUser ("Apurva", "Password");
		 Brian = new MATSUser ("Brian", "Password");
		 Eug = new MATSUser ("Eug", "Password");
		 
		 task1 = new MATSTask(Raj, Apurva, new Date ("03/05/08"), "task1", "Do task1");
		 task2 = new MATSTask(Brian, Eug, new Date ("04/10/08"), "task2", "Do task2");	
		 task3 = new MATSTask(Brian, Eug, new Date ("03/01/08"), "task2", "Do task2");	
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSetDescription() throws Exception {
		setUp();
		
		String descrip1 = "Changed description1";
		String descrip2 = "Changed description2";
		
		
		task1.setDescription(Raj, descrip1);	//if the creator tries to change the description, should be allowed
		assertTrue(task1.getDescription().equals(descrip1));
		
		task1.setDescription(Apurva, descrip2); //if someone that's not the creator tries to change, should not change
		assertFalse(task1.getDescription().equals(descrip2));
			
		tearDown();
		
	}

	public void testMATSTask() {	//don't need to test the constructor
		
		
	}

	public void testChangeOwner() throws Exception {
		setUp();
		
		assertFalse(task1.changeOwner(Brian,Eug)); //Someone who's not the creator tries to change the owner
		assertTrue(task1.changeOwner(Eug,Raj)); //Some who is the creator tries to change the owner
		
		task1.changeState(MATSTaskState.COMPLETED, Raj); //Make the task completed (assumes this function was tested)
		
		assertFalse(task1.changeOwner(Eug,Brian)); //Won't be able to add once task is not ongoing
		assertFalse(task1.changeOwner(Brian,Raj));
		
		tearDown();
	}

	public void testChangeDeadline() throws Exception {
		setUp();
		Date oldDate = new Date(task1.getDeadline()); 
		Date newDate1 = new Date ("03/01/08");	//March 1, 2008
		Date newDate2 = new Date ("05/08/08");	//May 8, 2008
		
		task1.changeDeadline(Brian,newDate2);	//user who can't modify it, but a date which is correct 
												//i.e. greater than current date
		assertFalse(task1.getDeadline().toString().equals(newDate2.toString()));
		
		task1.changeDeadline(Raj, newDate1);	//user who can modify deadline but a date which is incorrect
												//i.e. less than current date
		assertFalse(task1.getDeadline().toString().equals(newDate1.toString()));
		
		task1.changeDeadline(Raj, newDate2);	//user who can modify deadline and date is acceptable
		assertTrue(task1.getDeadline().toString().equals(newDate2.toString()));
		tearDown();
	}

	public void testChangeState() throws Exception {
		setUp();
		List<String> history = new ArrayList<String> ();
		history.add(currentDate +": This task was created on this date by " + task1.getCreator().toString());
		//gets added to the history upon construction of the object 
		
		task1.changeState(MATSTaskState.ONGOING,Raj);	//user can change state but can't change back to its current state
		assertTrue(task1.getHistory().equals(history));	//the history hasn't changed so the state wasn't changed
		
		task1.changeState(MATSTaskState.COMPLETED, Brian);	//user can't change state but destination state is plausible
		assertTrue(task1.getHistory().equals(history));	//history wasn't changd so the state wasn't changed

		task1.changeState(MATSTaskState.COMPLETED, Raj); //user can change state and state change is plausible
		assertFalse(task1.getHistory().equals(history));
		tearDown();
	}

	public void testChangeIfOverdue() throws Exception {
		setUp();
		Date oldDate = new Date(task1.getDeadline()); 
		Date newDate1 = new Date ("03/01/08");	//March 1, 2008
		Date newDate2 = new Date ("03/08/08");	//March 5, 2008
		
		List<String> oldHistory = new ArrayList<String> ();
		oldHistory.add(currentDate+": This task was created on this date by " + task2.getCreator().toString());
		//gets added to the history upon construction of the object
		
		task2.changeState(MATSTaskState.COMPLETED, Brian); //Change one of the states to completed for one of the tasks
		oldHistory.add(currentDate+": The task's state was changed to COMPLETED");
		
		task2.changeIfOverdue();	//shouldn't be able to set to overdue because it's not ongoing
		assertTrue(task2.getHistory().get(0).equals(oldHistory.get(0)));	//the history should not have changed for task
	
		
		task2.changeState(MATSTaskState.DELETED, Brian); //Can't change one of the states to completed for one of the tasks
		
		task2.changeIfOverdue();	//shouldn't be able to set to overdue because it's not ongoing
		assertTrue(task2.getHistory().get(0).equals(oldHistory.get(0)));	//the history should not have changed for task
		
		List<String> oldHistory2 = new ArrayList<String> ();
		oldHistory2.add(currentDate+": This task was created on this date by " + task3.getCreator().toString());
		//gets added to the history upon construction of the object
		
		//task3 is already in an ongoing state
		task3.changeIfOverdue();	//task3 has a deadline which would make it overdue
		assertFalse(task3.getHistory().equals(oldHistory2));	
		tearDown();
	}

	public void testWriteToHistory() throws Exception {
		setUp();
		
		List<String> history = new ArrayList<String> ();
		history.add(currentDate+": This task was created on this date by " + task1.getCreator().toString());
		//gets added to the history upon construction of the object
		
		assertTrue(task1.getHistory().equals(history));
		assertFalse(task2.getHistory().equals(history));
		
		task1.writeToHistory("test");	//write a test string to the history and see if it works
		assertFalse(task1.getHistory().equals(history));
		
		history.add(currentDate+": test");		
		assertTrue(task1.getHistory().equals(history));
		tearDown();
		
	}

	public void testGetOwner() throws Exception {
		setUp();
		assertTrue(task1.getOwner().equals("Apurva"));	//the owner of the the first task should be Apurva
		assertFalse(task1.getOwner().equals("Raj"));
		assertFalse(task1.getOwner().equals("Brian"));
		
		assertTrue(task2.getOwner().equals("Eug"));	//the owner of the second task should be Eug
		assertFalse(task2.getOwner().equals("Brian"));
		assertFalse(task2.getOwner().equals("Apurva"));
		tearDown();
	}

	public void testGetCreator() throws Exception {
		setUp();
		assertTrue(task1.getCreator().equals("Raj"));	//the creator of the the first task should be Raj
		assertFalse(task1.getCreator().equals("Apurva"));
		assertFalse(task1.getCreator().equals("Brian"));
		
		assertTrue(task2.getCreator().equals("Brian"));	//the creator of the second task should be Brian
		assertFalse(task1.getCreator().equals("Eug"));
		assertFalse(task1.getCreator().equals("Apurva"));
		tearDown();
	}

	public void testGetDeadline() throws Exception {
		setUp();
		String deadline1 = "Wed Mar 05 00:00:00 EST 2008";
		String deadline3 = "Sat Mar 01 00:00:00 EST 2008";
		
		
		assertTrue(task1.getDeadline().equals(deadline1));	//test if the task returns the proper deadline it was initalized with
		assertFalse(task1.getDeadline().equals(deadline3));
		
		assertTrue(task3.getDeadline().equals(deadline3)); //test second task initialized under the same conditions
		assertFalse(task3.getDeadline().equals(deadline1));
		tearDown();
	}

	public void testGetHistory() throws Exception {
		setUp();
		List<String> history = new ArrayList<String> ();
		history.add(currentDate+": This task was created on this date by " + task1.getCreator().toString());
		//gets added to the history upon construction of the object
		
		assertTrue(task1.getHistory().equals(history));
		assertFalse(task2.getHistory().equals(history));
	
		task1.changeOwner(Eug, Raj);	//should add a message to the history describing the owner change
		assertFalse(task1.getHistory().equals(history));
		
		history.add(currentDate+": This task was reassigned on this date to Eug");		
		assertTrue(task1.getHistory().equals(history));
		tearDown();
		
		
		
	}

	public void testGetState() throws Exception {
		setUp();
		Date oldDate = new Date(task1.getDeadline()); 
		Date newDate1 = new Date (108,02,01);	//March 1, 2008
		Date newDate2 = new Date (108,02,05);	//March 5, 2008
		
		List<String> oldHistory = task2.getHistory(); 
		
		task2.changeState(MATSTaskState.COMPLETED, Brian); //Change one of the states to completed for one of the tasks
		
		assertTrue(task2.getState()==MATSTaskState.COMPLETED);
		assertFalse(task2.getState()==MATSTaskState.ONGOING);		
		
		task2.changeState(MATSTaskState.DELETED, Brian); //Can't Change one of the states to deleted for one of the tasks		
		assertFalse(task2.getState()==MATSTaskState.DELETED);	
		
		task2.changeIfOverdue();	//the tasks should not be set to overdue because it is not
		assertFalse(task2.getState()==MATSTaskState.OVERDUE);	
		
		
		assertTrue(task3.getState()==MATSTaskState.ONGOING);	//the task3 was intiialized as ongoing
		assertFalse(task3.getState()==MATSTaskState.OVERDUE);
		task3.changeIfOverdue();	//the task3 is actually overdue and so it should have its state changed accordingly
		assertTrue(task3.getState()==MATSTaskState.OVERDUE);
		tearDown();
		
	}

}
