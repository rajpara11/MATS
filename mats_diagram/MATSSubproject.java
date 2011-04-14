package mats_model;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author boconno3, elee3, rparames, asaini
 * Defines MATSSubproject objects to contain both MATSSubproject and MATSTASK objects 
 */
public class MATSSubproject extends MATSComponent {

	/**
	 * List of all the contents of subproject
	 * These can include either MATSTasks or MATSSubprojects that inherit from 
	 * abstract class MATSComponent 
	 */
	private List<MATSComponent> subprojectFolder;
	
	/**
	 * @author boconno3, elee3, rparames, asaini
	 * @param Name of subproject 
	 * @param Description of subproject 
	 */
	public MATSSubproject(String name, String description) {
		super(name, description);
		subprojectFolder = new ArrayList<MATSComponent>();
	}
	
	/**
	 * @author boconno3, elee3, rparames, asaini
	 * @return A description of the subproject 
	 */
	public String getDescription() {
		return "Project description: \n" + super.getDescription();
	}
	
	/**
	 * @author rparames, asaini
	 * @param component: Adds this component to the subproject 
	 */
	public void addComponent(MATSComponent component) {
		
		subprojectFolder.add(component);	
	}
	
	/**
	 * @author elee3
	 * @param subproject: The name of the subproject folder, we want to find the parent project folder of
	 * @return This method will return a reference of itself if the given name of the subproject is one of its children
	 */
	public MATSSubproject findParent( String subproject ) {
		for ( MATSComponent component : subprojectFolder ) {
			
			if ( (component instanceof MATSSubproject) ) {
				
				MATSSubproject project = (MATSSubproject) component; 
				
				if ( subproject.equals(project.getName()) ) {
					return this;
				}
				else {
					MATSSubproject result = project.findParent( subproject );
					if ( result != null ) {
						return result;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * @author elee3
	 * @param subproject - the name of the subproject you want to look for
	 * @return if there exists a subproject with the name of the string passed in, then return true.
	 */
	public boolean containsSubproject( String subproject ) {
		for ( MATSComponent component : subprojectFolder ) {
			if ( subproject.equals(component.getName()) && (component instanceof MATSSubproject) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @author rparames, asain
	 * @param componentName: Name of component that is being searched for 
	 * @return The task or subproject which holds the specified name.
	 * 
	 * Iterates through the subproject folder to find the desired component.
	 */
	public MATSComponent findComponent(String componentName) {
		
		MATSComponent component = searchSubproject(this, componentName);
		return component;
	}
	
	/**
	 * @author rparames, asaini
	 * @return A list of components within the subproject.
	 */
	public List<MATSComponent> getSubprojectFolder() {
		return subprojectFolder;
	}
	
	/**
	 * @author rparames, asaini
	 * @param current: The current component in the search   
	 * @param nameToFind: The name the method is searching for 
	 * @return The task or subproject which holds the specified name.
	 * 
	 * Iterates through a subproject folder recursively until the component with the specified name is found
	 */
	private MATSComponent searchSubproject(MATSComponent current, String nameToFind) {
		
		if (nameToFind.equalsIgnoreCase(current.getName())) {
			return current;
		}
		
		if (current instanceof MATSTask) {
			
			return null;
		}
		
		if (current instanceof MATSSubproject) {
			
			MATSSubproject subproject = (MATSSubproject)current;
			
			for (MATSComponent c : subproject.subprojectFolder) {
				
				current = searchSubproject(c, nameToFind);
				if (current != null) {
					return current;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @author boconno3, asaini
	 * @param tasksOwned: a reference to arraylist collection of tasks owned by specified user 
	 * @param component: component to be examined
	 * @param user: Checks whether specified user owns tasks
	 * 
	 * Recursive method that goes into each subproject and collects the number of tasks
	 * owned by the specified user 
	 */
	public void findTasksOwned(List<MATSTask> tasksOwned, MATSComponent component, MATSUser user){
		
		
		if (component instanceof MATSSubproject){
			MATSSubproject subproject = (MATSSubproject)component;
			for (MATSComponent c: subproject.subprojectFolder){
				findTasksOwned(tasksOwned, c, user);
				
			}
		}
		if (component instanceof MATSTask) {
			MATSTask task = (MATSTask)component;
			if (task.getOwner().equals(user.toString())){
				tasksOwned.add(task);
	
			}
		}
		
		
	}
	
	/**
	 * 
	 * @author boconno3, asaini
	 * @param tasksCreated: a reference to arraylist collection of tasks created by specified user
	 * @param component: Component to be examined
	 * @param user:Checks whether specified user created tasks
	 * 
	 * Recursive method that goes into each subproject and collects the number of tasks
	 * owned by the specified user 
	 */
	public void findTasksCreated(List<MATSTask> tasksCreated, MATSComponent component, MATSUser user){
		
		if (component instanceof MATSSubproject){
			MATSSubproject subproject = (MATSSubproject)component;
			for (MATSComponent c: subproject.subprojectFolder){
				findTasksCreated(tasksCreated, c, user);
			}
		}
		if (component instanceof MATSTask) {
			MATSTask task = (MATSTask)component;
			if (task.getCreator().equals(user.toString())){
				tasksCreated.add(task);
			}
		}
	}
	
	/**
	 * @author boconno3
	 * @return A list of all tasks within subproject
	 * 
	 * Creates and returns a list of tasks within the folder 
	 */
	public List<MATSTask> collectTasks(){
		List<MATSTask> allTasks = new ArrayList<MATSTask>(); 
		collectAllTasks(this, allTasks);
		return allTasks;
	}
	
	/**
	 * @author boconno3
	 * @param component: The component from which tasks are collected
	 * @param allTasks:  A list of all the tasks in the main subproject folder
	 * 
	 * Recursive method that adds tasks to the reference of collection of tasks passed in as parameter 
	 */
	private void collectAllTasks(MATSComponent component, List<MATSTask> allTasks) {
		
		if (component instanceof MATSSubproject){
			MATSSubproject subproject = (MATSSubproject) component;
			for (MATSComponent c: subproject.subprojectFolder){
				collectAllTasks(c, allTasks);
			}
		}
		if (component instanceof MATSTask) {
			MATSTask task = ( MATSTask ) component; 
			allTasks.add(task);
		}
	}
	
	/**
	 * @author elee3
	 * @param subproject - remove from the collection this subproject (used for the move function particularly)
	 * @return true if the remove was successful
	 */
	public boolean remove( MATSSubproject subproject ) {
		return subprojectFolder.remove(subproject);
	}
	
	public List<MATSComponent> getChildren() {
		
		List<MATSComponent> components = new ArrayList<MATSComponent>();
		
		for (MATSComponent comp : subprojectFolder) {
			
			components.add(comp);
		}
		
		return components;
	}
	
	/**
	 * @author rparames, asaini, boconnor
	 * @param The name of the subproject from which we need all the children.
	 * @return A list of components within ONLY the subproject specified by the parameter.
	 */
	public List<String> getChildrenOf(String name) {
		
		List<String> childrenNames = new ArrayList<String>();
		MATSComponent parent = findComponent(name);
		
		if (parent instanceof MATSSubproject) {
			MATSSubproject root = (MATSSubproject) parent;
			
			childrenNames = searchChildren(root);
		}
		
		return childrenNames;
	}
	
	/**
	 * @author asaini, boconnor
	 * @param The parent of all the children we want to receive
	 * @return A list of children of the specified subproject.
	 */
	public List<String> searchChildren(MATSSubproject root) {
		
		List<String> childrenNames = new ArrayList<String>();		
		for (MATSComponent child : root.getSubprojectFolder()) {
			childrenNames.add(child.getName());
		}
		
		return childrenNames;
	}
	
	/**
	 * 
	 * @return A string representation of the MATSSubproject object.
	 */
	public String toXML() {
		
		String messageXML = "\r\n<MATSSubproject>";
		messageXML += "\r\n<name>" + name + "</name>";
		messageXML += "\r\n<description>" + description + "</description>";
		messageXML += "\r\n<subprojectFolder>";
		
		for (MATSComponent component : subprojectFolder) {
			
			if (component instanceof MATSSubproject) {
				
				MATSSubproject scomponent = (MATSSubproject)component;
				messageXML += scomponent.toXML();
			}
			else if (component instanceof MATSTask) {
				
				MATSTask tcomponent = (MATSTask)component;
				messageXML += tcomponent.toXML();
			}
		}
		messageXML += "\r\n</subprojectFolder>";
		messageXML += "\r\n</MATSSubproject>";
		
		return messageXML;
	}

    /**
     * @link aggregation 
     */
    private MATSComponent lnkMATSComponent;
}