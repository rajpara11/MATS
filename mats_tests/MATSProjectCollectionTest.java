//Raj
package mats_tests;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import mats_model.*;

import junit.framework.TestCase;

public class MATSProjectCollectionTest extends TestCase {

	MATSProjectCollection collection1;
	//MATSProjectCollection collection2;
	
	MATSUser Raj;
	MATSUser Apurva;
	MATSUser Brian;
	MATSUser Eug;
	
	MATSTask task1;
	MATSTask task2;
	MATSTask task3;
	MATSTask task4;
	MATSTask task5;
	
	MATSSubproject subproject1;
	MATSSubproject subproject2;
	MATSSubproject subproject3;
	
	List<MATSTask> tasksList;
	
	protected void setUp() throws Exception {
		
		super.setUp();

		collection1 = new MATSProjectCollection();
		
		Raj = new MATSUser ("Raj", "Password");
		Apurva = new MATSUser ("Apurva", "Password");
		Brian = new MATSUser ("Brian", "Password");
		Eug = new MATSUser ("Eug", "Password");
		 
		task1 = new MATSTask(Raj, Apurva, new Date ("03/01/08"), "task1", "Do task1");
		task2 = new MATSTask(Brian, Eug, new Date ("03/20/08"), "task2", "Do task2");	
		task3 = new MATSTask(Brian, Eug, new Date ("02/01/08"), "task3", "Do task3");
		task4 = new MATSTask(Apurva, Raj, new Date ("03/22/08"), "task4", "Do task4");
		task5 = new MATSTask(Eug, Apurva, new Date ("03/23/08"), "task5", "Do task5");
		
		task1.changeState(1, Raj);
		task3.changeState(1, Brian);
		 
		
		subproject1 = new MATSSubproject("subproject1", "description1");
		subproject2 = new MATSSubproject("subproject2", "description2");
	
		collection1.addProject("name", "description");
		collection1.addProject("name2", "description2");
		
		collection1.getChildren().get(0).addComponent(task1);
		collection1.getChildren().get(0).addComponent(task2);
		subproject1.addComponent(subproject2);
		subproject1.addComponent(task4);
		subproject2.addComponent(task3);
		collection1.getChildren().get(0).addComponent(subproject1);
		collection1.getChildren().get(1).addComponent(task5);
		
		tasksList = new ArrayList<MATSTask>();
		tasksList.add(task1);
		tasksList.add(task2);
		tasksList.add(task3);
		tasksList.add(task4);
		tasksList.add(task5);
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMATSProjectCollection() {
		
		MATSProjectCollection pCollection = null;
		
		assertNull(pCollection);
		pCollection = new MATSProjectCollection();
		assertTrue(pCollection.getChildren().size() == 0);
		assertNotNull(pCollection);
	}

	/**
	 * Created Mar 3- Brian
	 *
	 */
	public void testAddProject()throws Exception {
		
		setUp();
		collection1.addProject("Attach", "Attach files");
				
		assertTrue(collection1.getChildren().get(2).getName().equals("Attach"));
		assertFalse(collection1.getChildren().get(2).getDescription().equals("Attach"));
		
		collection1.addProject("Share", "Share files");
		assertFalse(collection1.getChildren().get(3).getName().equals("Share files"));
		assertTrue(collection1.getChildren().get(3).getDescription().equals("Project description: \n" + "Share files"));
		
		//fail("Not yet implemented");
		tearDown();
	}

	public void testFindTasksOwned() throws Exception {
		
		//fail("Not yet implemented");
		setUp();
		
		List<MATSTask> list1 = collection1.findTasksOwned(Eug);
		
		assertTrue(list1.get(0) == task2);
		assertTrue(list1.get(1) == task3);
		assertFalse(list1.size() == 3);
		
		List<MATSTask> list2 = collection1.findTasksOwned(Apurva);

		assertTrue(list2.size() == 2);
		assertTrue(list2.get(0) == task1);
		assertTrue(list2.get(1) == task5);
		assertFalse(list2.get(0) == task4);
		
		List<MATSTask> list3 = collection1.findTasksOwned(Raj);

		assertTrue(list3.size() == 1);
		assertFalse(list3.get(0) == task5);
		assertTrue(list3.get(0) == task4);		
		
		tearDown();
	}

	/**
	 * Also tests findTasksOwned method from MATSSubproject
	 * @throws Exception
	 */
	public void testFindTasksCreated()throws Exception {
		//fail("Not yet implemented");
		setUp();
		
		List<MATSTask> list4 = collection1.findTasksCreated(Brian);
		
		assertTrue(list4.get(0) == task2);
		assertTrue(list4.get(1) == task3);
		assertFalse(list4.size() == 3);
		
		List<MATSTask> list5 = collection1.findTasksCreated(Eug);

		assertTrue(list5.size() == 1);
		assertTrue(list5.get(0) == task5);
		assertFalse(list5.get(0) == task4);
		
		List<MATSTask> list6 = collection1.findTasksCreated(Raj);

		assertTrue(list6.size() == 1);
		assertFalse(list6.get(0) == task5);
		assertTrue(list6.get(0) == task1);	
		tearDown();	
	}
		
	
	/**
	 * 
	 *
	 */
	public void testFindComponent()throws Exception {
		
		setUp();
		
		MATSComponent componentFound1 = collection1.findComponent("task1");
		MATSComponent componentFound2 = collection1.findComponent("task4");
		
		assertTrue(componentFound1 == task1);
		assertTrue(componentFound2 == task4); //has to go into multiple subproject folders to find task4 
		

		assertFalse(componentFound2 == task3);
		assertFalse(componentFound1 == task4);
		
		tearDown();
		
	}

	public void testContainsSubproject () throws Exception {
		setUp();

		assertTrue(collection1.containsSubproject("name")); //was added into the collection
		assertTrue(collection1.containsSubproject("name2"));
		
		subproject2.addComponent(subproject3); //test with a subprojectthere and one that is
		assertTrue(collection1.containsSubproject("subproject2")); //at the bottom level
		
		
		
		tearDown();
		
	}
	public void testFindTasksOfState() throws Exception {
			
		setUp();
		
		tasksList.remove(task1);
		tasksList.remove(task3);
		
		List<MATSTask> tasksOfState = collection1.findTasksOfState(0); //finds all the tasks that are ongoing
		
		assertTrue( tasksOfState.equals(tasksList));
		
		task2.changeState(2, Brian);
		task4.changeState(2, Apurva);
		task5.changeState(2, Eug);
		
		tasksOfState = collection1.findTasksOfState(0);
		assertFalse( tasksOfState.equals(tasksList));
		
		
		tasksOfState = collection1.findTasksOfState(2); //finds tasks that are in completed state
		
		assertTrue( tasksOfState.equals(tasksList));
		
		task2.changeState(3, Brian);
		task4.changeState(3, Apurva);
		task5.changeState(3, Eug);
		
		tasksOfState = collection1.findTasksOfState(3); //finds tasks that are in deleted state
		assertFalse( tasksOfState.equals(tasksList));
		
		tasksOfState = collection1.findTasksOfState(1); //finds overdue tasks 
		tasksList.remove(task2);
		tasksList.remove(task4);
		tasksList.remove(task5);
		
		tasksList.add(task1); 
		tasksList.add(task3);
		
		assertTrue( tasksOfState.equals(tasksList));
		
		tearDown();
	}

	
	public void testGetChildren() throws Exception {
		
		setUp();
		
		MATSProjectCollection collection3 = new MATSProjectCollection();		
		
		collection3.addProject("name", "description");
		collection3.addProject("name2", "description2");
		
		List<MATSSubproject> children = collection3.getChildren();
		
		assertTrue(children.get(0).getName().equals("name"));
		assertTrue(children.get(1).getName().equals("name2"));
		
		assertFalse(children.get(0).getName().equals("name2"));
		
		tearDown();		
	}

}
