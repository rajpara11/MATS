//Apurva
package mats_tests;
import java.util.Date;

import mats_model.*;
import java.util.*;
import junit.framework.TestCase;

public class MATSSubprojectTest extends TestCase {
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
	
	protected void setUp() throws Exception {
		super.setUp();
		 Raj = new MATSUser ("Raj", "Password");
		 Apurva = new MATSUser ("Apurva", "Password");
		 Brian = new MATSUser ("Brian", "Password");
		 Eug = new MATSUser ("Eug", "Password");
		 
		 task1 = new MATSTask(Raj, Apurva, new Date ("03/05/08"), "task1", "Do task1");
		 task2 = new MATSTask(Brian, Eug, new Date ("03/10/08"), "task2", "Do task2");	
		 task3 = new MATSTask(Brian, Eug, new Date ("03/01/08"), "task3", "Do task2");
		 task4 = new MATSTask(Apurva, Raj, new Date ("03/05/08"), "task4", "Do task4");
		 task5 = new MATSTask(Eug, Apurva, new Date ("03/05/08"), "task5", "Do task5");
		 
		
		 subproject1 = new MATSSubproject("subproject1", "description1");
		 subproject2 = new MATSSubproject("subproject2", "description2");
		 subproject3 = new MATSSubproject("subproject3", "description3");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetDescription() throws Exception {
		setUp();
		
		String descrip = "Project description: \n";
		descrip += "description1";
		String descrip2 = "Project description: \n";
		descrip2+= "description2";
		
		assertTrue(subproject1.getDescription().equals(descrip));	//should be same description as subproject 1's
		assertFalse(subproject2.getDescription().equals(descrip));
		assertFalse(subproject3.getDescription().equals(descrip));
		
		assertTrue(subproject2.getDescription().equals(descrip2));	//should be same description as subproject 2's
		assertFalse(subproject1.getDescription().equals(descrip2));
		assertFalse(subproject3.getDescription().equals(descrip2));
		tearDown();
	}

	public void testMATSSubproject() {
		//don't need to implement the constructor for now
	}

	public void testAddComponent() throws Exception {
		setUp();
		assertFalse(subproject1.findComponent("task1")==task1); //task1 is not in subproject yet
		
		subproject1.addComponent(task1);	//add a task into the subproject
		assertTrue(subproject1.findComponent("task1")==task1); //task1 should be in subproject now
		
		assertFalse(subproject1.findComponent("subproject2")==subproject2); //subproject2 is not in subproject1 right now
		
		subproject1.addComponent(subproject2);
		assertTrue(subproject1.findComponent("subproject2")==subproject2); //subproject2 should be in the subproject1 right now
		assertFalse(subproject1.findComponent("subroject3")==subproject3);//not been added yet
		
		subproject2.addComponent(subproject3);
		assertTrue(subproject2.findComponent("subproject3")==subproject3);	//the subproject3 is inside subproject1 and subproject2
		assertTrue(subproject1.findComponent("subproject3")==subproject3);
		tearDown();
	}

	public void testGetChildren() throws Exception {
		setUp();
		List<MATSComponent> components = new ArrayList<MATSComponent>();
		components.add(subproject2);	//add subproject2 into the list
		
		assertFalse(subproject1.getChildren().equals(components)); //there is nothing in subproject1
		
		subproject1.addComponent(subproject2); //put subproject2 into subproject1
		assertTrue(subproject1.getChildren().equals(components));
		
		subproject2.addComponent(task2); //puts task2 into subproject2
		assertTrue(subproject1.getChildren().equals(components)); //Adding it to subproject2 doesn't change the children of subproject1
		
		subproject1.addComponent(task1);	//task1 is added to supbroject1
		assertFalse(subproject1.getChildren().equals(components)); //the children of this subproject has changed now
		
		components.add(task1);	//updates the list for comparison
		assertTrue(subproject1.getChildren().equals(components));
		tearDown();
	}
	
	
	public void testGetSubprojectFolder() throws Exception {
		//the exact same code as getChildren() but we are allowed smelly code at this point
		
		setUp();
		List<MATSComponent> components = new ArrayList<MATSComponent>();
		components.add(subproject2);	//add subproject2 into the list
		
		assertFalse(subproject1.getChildren().equals(components)); //there is nothing in subproject1
		
		subproject1.addComponent(subproject2); //put subproject2 into subproject1
		assertTrue(subproject1.getChildren().equals(components));
		
		subproject2.addComponent(task2); //puts task2 into subproject2
		assertTrue(subproject1.getChildren().equals(components)); //Adding it to subproject2 doesn't change the children of subproject1
		
		subproject1.addComponent(task1);	//task1 is added to supbroject1
		assertFalse(subproject1.getChildren().equals(components)); //the children of this subproject has changed now
		
		components.add(task1);	//updates the list for comparison
		assertTrue(subproject1.getChildren().equals(components));
		tearDown();
	}

	public void testFindComponent() throws Exception {
		//test finding task and subproject and taks and subproject two levels away
		
		setUp();
		assertFalse(subproject1.findComponent("subproject2")==subproject2); //nothing in subproject1 at this point
		
		subproject1.addComponent(subproject2); //added subproject2 into subproject1
		assertTrue(subproject1.findComponent("subproject2")==subproject2);
		
		assertFalse(subproject1.findComponent("task1")==task1); //task1 is not in subproject1 yet
		
		subproject1.addComponent(task1); //task1 is added to subproject1
		assertTrue(subproject1.findComponent("task1")==task1);
		
		assertFalse(subproject1.findComponent("task2")==task2); //task2 is not in supbroject1 yet
		
		subproject2.addComponent(task2); //task2 is added to supbproject2 which is in supbroject1
		assertTrue(subproject1.findComponent("task2")==task2);
		
		assertFalse(subproject1.findComponent("subproject3")==subproject3); //subproject3 is not in subproject1
		
		subproject2.addComponent(subproject3); //subproject3 is added to subproject2 which is in subproject1
		assertTrue(subproject1.findComponent("subproject3")==subproject3);
		
		
		assertFalse(subproject1.findComponent("task3")==task3); 
		
		subproject3.addComponent(task3); //task3 is added to subproject3, which is in subproject2, which is in subproject1
		assertTrue(subproject1.findComponent("task3")==task3); 
		tearDown();
	
	}	
	public void testFindParent() throws Exception {
		setUp();
		
		subproject1.addComponent(subproject2);	//test adding a supbroject to the first level
		
		assertTrue(subproject1.findParent("subproject2").getName().equals("subproject1"));
		
		subproject2.addComponent(subproject3); //test adding a subproject into the second level
		assertFalse(subproject1.findParent("subproject3").getName().equals("subproject1"));
		assertTrue(subproject1.findParent("subproject3").getName().equals("subproject2"));
		
		tearDown();
		
	}
	
	public void testContainsSubproject () throws Exception {
		setUp();
		subproject1.addComponent(subproject2); //test adding a subproject into the first level
		
		assertTrue(subproject1.containsSubproject("subproject2"));
		
		subproject2.addComponent(subproject3); //test adding a supbojrect into the second level
		assertFalse(subproject1.containsSubproject("subproject3"));
		assertTrue(subproject2.containsSubproject("subproject3"));
		
		
		tearDown();
		
	}
	
	public void testGetChildrenOf() throws Exception {
		setUp();
		subproject1.addComponent(subproject2); 
		subproject2.addComponent(task2); //add into the subproject at first levle
		subproject2.addComponent(subproject3);
		subproject3.addComponent(task1); //add into the second level
		subproject2.addComponent(task3);
		
		assertTrue(subproject1.getChildrenOf("subproject2").get(0).equals("task2"));
		assertTrue(subproject1.getChildrenOf("subproject2").get(1).equals("subproject3"));
		
		assertFalse(subproject1.getChildrenOf("subproject2").get(2).equals("task1"));
		
		assertTrue(subproject1.getChildrenOf("subproject2").get(2).equals("task3"));
		
		
		
		
		tearDown();
		
	}
	
	public void testFindTasksOwned() {
		//Don't need to test this method as it's a recursive helper for the method of same name
		//in the projectCollection class, so refer to the test methods for that class
	}
	
	public void testFindTasksCreated() {
		//Don't need to test this method as it's a recursive helper for the method of same name
		//in the projectCollection class, so refer to the test methods for that class
		
	}
	
	public void testCollectTasks()throws Exception {
		setUp();
		
		List <MATSTask> tasks = new ArrayList<MATSTask>();
		
		//nothing in subproject1 at this point
		
		subproject1.addComponent(subproject2); //added subproject2 into subproject1
		assertTrue(subproject1.findComponent("subproject2")==subproject2);
		
		assertTrue(subproject1.collectTasks().equals(tasks)); //task1 is not in subproject1 yet
		
		subproject1.addComponent(task1); //task1 is added to subproject1
		assertFalse(subproject1.collectTasks().equals(tasks));
		tasks.add(task1);		
		assertTrue(subproject1.collectTasks().equals(tasks));
		
		
		//task2 is not in supbroject1 yet
		tasks.remove(task1);
		tasks.add(task2);
		tasks.add(task1);
		assertFalse(subproject1.collectTasks().equals(tasks));
		subproject2.addComponent(task2); //task2 is added to supbproject2 which is in supbroject1
		assertTrue(subproject1.collectTasks().equals(tasks));//task2 is not in subproject 1's collectTasks() arrayList
		
		
		//subproject3 is not in subproject1
		
		subproject2.addComponent(subproject3); //subproject3 is added to subproject2 which is in subproject1
		
		
		
		subproject1.addComponent(task3); //task3 is added to subproject1
		assertFalse(subproject1.collectTasks().equals(tasks));
		
		tasks.add(task3);	//need to update the list for comparison
		assertTrue(subproject1.collectTasks().equals(tasks));
		
		tearDown();
		
	} 

}
