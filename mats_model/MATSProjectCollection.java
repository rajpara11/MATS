package mats_model;

import java.util.*;
import java.io.Serializable;

/**
 * 
 * @author boconno3, elee, rparames, asaini 
 *
 * Defines project collection object that contains a list of all subproject 
 * objects that car not contained within another subproject. 
 */
public class MATSProjectCollection implements Serializable {

	private List<MATSSubproject> projectCollection;
	
	
	/**
	 * @author rparames
	 * @author asaini
	 * @author elee3
	 * @author boconno3
	 * 
	 * Constructor and Instantiates list of projects 
	 */
	public MATSProjectCollection() {
		projectCollection = new ArrayList<MATSSubproject>();
	}
	


	/**
	 * 
	 * @author rparames
	 * @author asaini
	 * @author elee3
	 * @author boconno3
	 * @param name - The name of the project to add to the project collection
	 * @param description - a description describing the project to add to the project collection
	 * 
	 * Adds a projects to the project collection
	 */
	public void addProject(String name, String description) {
		MATSSubproject newProject = new MATSSubproject(name, description);
		projectCollection.add(newProject);
	}

	
	/**
	 * @author elee3
	 * @param String - The name of the subproject we want to find
	 * @return If there exists a subproject with the name of the string passed in, then return true
	 * 
	 * This method will only check one layer downwards for the instance of the subproject-to-find
	 */
	public boolean containsSubproject(String subprojectToFind) {

		MATSComponent component = null;

		for (MATSSubproject subproject: projectCollection) {
			if ( subprojectToFind.equalsIgnoreCase(subproject.getName()) ) {
				return true;
			}
			component = subproject.findComponent(subprojectToFind);

			if ( (component != null) && (component instanceof MATSSubproject) ) {
				return true;
			}
		}
		return false;
	}


	/**
	 * @author rparames
	 * @author asaini
	 * @author elee3
	 * @author boconno3
	 * @param user - the instance of a user to find all tasks owned by
	 * @return A collection list of all the tasks found from this search method
	 * 
	 * This method will recursively search for all instances of tasks owned by this instance user by
	 * traversing the project collection, checking out all the subprojects and create a list of all the
	 * tasks that the user has been assinged.  
	 */
	public List<MATSTask> findTasksOwned(MATSUser user) {

		List<MATSTask> tasksOwned = new ArrayList<MATSTask>();
		
		for (MATSSubproject s: projectCollection){
			s.findTasksOwned(tasksOwned, s, user);
		}
		
		return tasksOwned;
	}


	/**
	 * @author rparames
	 * @author asaini
	 * @author elee3
	 * @author boconno3
	 * @param user - the instance of a user to find all tasks created by
	 * @return A collection list of all the tasks found from this search method
	 * 
	 * This method will recursively search for all instances of tasks created by this instance user by
	 * traversing the project collection, checking out all the subprojects and create a list of all the
	 * tasks that the user shows as the creator.  
	 */
	public List<MATSTask> findTasksCreated(MATSUser user) {

		List<MATSTask> tasksCreated = new ArrayList<MATSTask>();

		for (MATSSubproject s: projectCollection ){
			s.findTasksCreated(tasksCreated, s, user);
		}
		
		return tasksCreated;
	}

	
	/**
	 * @author rparames
	 * @author asaini
	 * @param name - The name of the task or project to find
	 * @return this method will return the reference to the task or project if found, otherwise, returns null.
	 * 
	 *  This method will look for the task or project based on the given name by
	 *  cycling through the collection of projects
	 */
	public MATSComponent findComponent(String name) {

		MATSComponent component = null;
		
		for (MATSSubproject subproject: projectCollection) {
			component = subproject.findComponent(name);
			if (component!= null) {
				return component;
			}
		}

		return component;
	}

	
	/**
	 * @author rparames
	 * @author asaini
	 * @author elee3
	 * @author boconno3
	 * @param state - search parameter to find all tasks in common with
	 * @return list of tasks that are in the specified state.
	 * 
	 * This method will recursively search for all instances of tasks that have the given state by
	 * traversing the project collection
	 */
	public List<MATSTask> findTasksOfState(int state) {

		List<MATSTask> allTasks = new ArrayList<MATSTask>(); 
		List<MATSTask> tasksOfState = new ArrayList<MATSTask>();

		for (MATSSubproject subproject: projectCollection) {
			allTasks.addAll(subproject.collectTasks());
		}

		for (MATSTask task : allTasks){
			if (task.getState() == state) {
				tasksOfState.add(task);
			}
		}

		return tasksOfState;
	}

	
	/**
	 * @author rparames
	 * @author asaini
	 * @return list of the subprojects in this project collection class 
	 * 
	 * This method returns the 1st layer of children.  Only subprojects are returned.
	 */
	public List<MATSSubproject> getChildren() {
		return projectCollection;
	}
	
	/**
	 * @author asaini, boconnor
	 * @param The name of the subproject from which we want to get all the children.
	 * @return The list of names of the children (1st layer only) of the specified subproject.
	 */
	public List<String> getSubprojectChildrenNames(String name) {
		
		List<String> childrenNames = new ArrayList<String>();
		
		for (MATSSubproject subproject : projectCollection) {
			
			childrenNames = subproject.getChildrenOf(name);
			if (childrenNames.size()!=0 ) {
				break;
			}
		}
		
		//System.out.println(childrenNames.size());
		return childrenNames;
	}
	
	/**
	 * @return The list of names of the 1st layer of children in the project collection.
	 */
	public ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (MATSSubproject subproject : projectCollection ) {
			names.add( subproject.getName() );
		}
		return names;
	}
	
	

	
	/**
	 * @author elee3
	 * @param subproject - the instance of subproject to remove from the project collection
	 * @return this method will return true if remove was successful, otherwise false
	 * 
	 * This method will remove the given instance of subproject from the project collection.
	 * This method is used primarily by the move project method.
	 */
	public boolean remove( MATSSubproject subproject ) {
		return projectCollection.remove(subproject);
	}
	
	/**
	 * 
	 */
	public boolean add(MATSSubproject subproject) {
		return projectCollection.add(subproject);
	}
	
	public String toXML() {
		
		String message = "\r\n<MATSProjectCollection>";
		message += "\r\n<projectCollection>";
		
		for (MATSSubproject subproject : projectCollection) {
			
			message += subproject.toXML();
		}
		message += "\r\n</projectCollection>";
		message += "\r\n</MATSProjectCollection>";
		return message;
	}

}
