package mats_model;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.PrintWriter;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Iterator;
import java.util.Date;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;



/**
 * @author Eugene, Apurva , Brian, Raj  
 * 
 * Documentation: Brian
 * The model object is independant of the controller and the view, is observable by view 
 * and preserves database content. 
 */
public class MATSModel extends DefaultHandler implements ListModel {

	/**
	 * MatsModel constants 
	 */
	private static enum Action {ERROR,
		                        NEW_SYSTEM_CREATED,
		                        IMPORTED_SYSTEM,
		                        LOGGED_IN,
		                        LOGGED_OUT,
		                        NEW_USER_ADDED,
								NEW_TASK_CREATED,
								NEW_SUBPROJECT_CREATED,
								TASK_ASSIGNED,
								REFRESH,
								TASK_STATE_CHANGED,
								TASK_OWNER_CHANGED,
								TASK_DEADLINE_CHANGED,
								COMPONENT_DESCRIPTION_CHANGED,
								PROJECT_MOVED}
	


	/**
	 * List of all the projects in the model.
	 */
	private MATSProjectCollection projectFolder;

	/**
	 * List of all the views 
	 */
	
	private List<ModelListener> modelListeners;

	/**
	 * List of all the users in the model.
	 */
	private MATSUserDatabase userDatabase;

	/**
	 * The current user that is logged on to the application model
	 */
	private MATSUser whoAmI;

	/**
	 * The listmodel of all of the users
	 */
	private DefaultListModel listModel;
	
	
	//==========================================================
	// XML Instance Variables for IMPORTING (ALL STATIC)
	//==========================================================
	
	private static MATSModel importingNewModel;
	
	/**
	 * The temporary variable used as the value of many hashmaps.
	 */
	private static String charsBetweenTags;
	
	/**
	 * Preserves and Identifies the most recent set of tags for a Class (i.e. MATSUser, MATSTask)
	 */
	private static Stack<String> classStack;
	
	/**
	 * Temporarily preserve all information found about a MATSUser
	 */
	private static HashMap<String, String> tempMatsUserHashMap;
	
	
	/**
	 * Create the populated project collection then add it to the model
	 */
	private static MATSProjectCollection tempMatsProjectCollection;
	
	/**
	 * The list of MATS components used to keep track of.
	 */
	private static Stack<MATSComponent> componentStack;

	/**
	 * The list of MATS components used to keep track of.
	 */
	private static Stack<HashMap<String,String>> subprojectInfoStack;
	
	private static HashMap<String,String> tempMatsTaskHashMap;
	
	private static List<String> tempMatsTaskHistory;

	private static Date tempMatsTaskDate;
	
	private static FileInputStream fileInput;

	
	/**
	 * @author rparames, boconno3
	 */
	public MATSModel(MATSUserDatabase userDatabase, MATSProjectCollection projectFolder) {
		
		this.userDatabase = userDatabase;
		this.projectFolder = projectFolder;
		
		whoAmI = null;
		listModel = new DefaultListModel();
		modelListeners = new ArrayList<ModelListener>();
	}
	
	
	/**
	 * @author elee3
	 *
	 * Constructor
	 */
	public MATSModel() {
		
		this( new MATSUserDatabase() , new MATSProjectCollection() );
		
	}

	/**
	 * Method that ListModel forces us to implement 
	 */
	public void addListDataListener(ListDataListener l) {
	}

	/**
	 * Method that ListMOdel forces us to implement  
	 */
	public void removeListDataListener(ListDataListener l) {
	}

	/**
	 * @param Index to locate usr in user database
	 * @return User from user database  
	 */
	public Object getElementAt(int index) {
		return userDatabase.get(index);
	}

	/**
	 * @return size of userdatabase  
	 */
	public int getSize() {
		return userDatabase.size();
	}
	
	private MATSUser findUser(String user) {
		return userDatabase.findUser(user);
	}

	/**
	 * 
	 * @param projectFolder The ProjectCollection object to be set in the model 
	 */
	private void setProjectFolder(MATSProjectCollection projectFolder ) {
		this.projectFolder = projectFolder;
	}

	/**
	 * 
	 * @param userDatabase: The userdatabase object to be set in the model 
	 */
	private void setUserDatabase(MATSUserDatabase userDatabase ) {
		this.userDatabase = userDatabase;
	}

	/**
	 * 
	 * @return An array list containing the names of each immediate project in the main project collection 
	 */
	public ArrayList<String> getNames() {
		return projectFolder.getNames();
	}

	/**
	 * 
	 * @param inputFilePath
	 * @return a MATSModel object to be imported, loading an existing MATS System when starting the application
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static MATSModel importMATSModel(String inputFilePath) throws IOException, ClassNotFoundException {

		MATSModel importModel = new MATSModel();

		FileInputStream fileIn = new FileInputStream( inputFilePath );
		ObjectInputStream objectFileIn = new ObjectInputStream( fileIn );

		Object readObject;

		while ( true ) {

			try {
				readObject =  objectFileIn.readObject();
			} catch (Exception e) {
				objectFileIn.close();
				fileIn.close();
				return importModel;
			}

			if ( readObject instanceof MATSUserDatabase ) {
				importModel.setUserDatabase( (MATSUserDatabase) readObject );
				for ( String userName : importModel.getAllUsers() ) {
					importModel.getListModel().addElement(userName);
				}
			}
			else if ( readObject instanceof MATSProjectCollection ) {
				importModel.setProjectFolder( (MATSProjectCollection) readObject );
			}
		}

	}

	/**
	 * 
	 * @param outputFilePath
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * 
	 * Exports current model System to external file 
	 */
	public void exportMATSModel(String outputFilePath) throws IOException, ClassNotFoundException {

		FileOutputStream fileOut = new FileOutputStream(outputFilePath);

		ObjectOutputStream objectFileOut = new ObjectOutputStream(fileOut);
		
		objectFileOut.writeObject( userDatabase );
		objectFileOut.writeObject( projectFolder );

		objectFileOut.close();
		fileOut.close();
	}


	/**
	 * @author rparames
	 * @param user - the user who will be logged in
	 * @param password - the password of the user that will be logged in
	 * @return true if the user was successfully logged in, false if not
	 * 
	 * This method will take the name of a user and their password and log that user
	 * in by verifying that that user exists in the database and has the correct
	 * information
	 */
	public boolean login(String user, String password) {

		whoAmI = userDatabase.verifyUser(user, password);
		

		
		if ( whoAmI != null ) {
			notifyViews("Logged In", Action.LOGGED_IN);
			return true;
		}
		else {
			
			notifyViews("Could not log in", Action.ERROR);
		}

		return false;
	}

	/**
	 * @author elee3
	 * @param user - the user who is to be logged in
	 * @return true if the string is equivalent to the name of the user that is currently logged in
	 * 
	 * This method will return whether or not a given user (passed in ) is logged in right now or not
	 */
	public boolean loggedIn(String user) {
		if(whoAmI!=null){
			if (user.equals(whoAmI.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author elee3
	 * This method will logout the current user that was logged in
	 *
	 */
	public void logout() {
		if ( whoAmI != null ) {
			notifyViews(whoAmI.toString() + " successfully logged out", Action.LOGGED_OUT);
			whoAmI = null;
		}
	}

	/**
	 * @author boconno3
	 * @param username -the user name of the user that will be added
	 * @param password - the password of the user that will be added
	 * 
	 * This method will take a username and a password and create a user using
	 * these by adding them to the user database
	 */
	public boolean addUser(String username, String password) {
		if ((username.length()==0) || (password.length()==0)){
			notifyViews("Invalid username or password!" , Action.ERROR);
			return false;
		}
		if (userDatabase.addUser(username, password)) {
			listModel.addElement( username );
			notifyViews( username + " was added to the database", Action.NEW_USER_ADDED);
			return true;
		}
		else {
			notifyViews("User already exists with that name!", Action.ERROR);
			return false;
		}
	}

	/**
	 * @autor elee3
	 * @author rparames
	 * @author boconno3
	 * @author asaini
	 * @param name - the name of the component we'd like to add
	 * @param component - the type of component we'd like to add
	 * @param subproject - the subproject underneath which this project should be added
	 * @param owner - if a task, the owner of the task
	 * @param deadline - if a task, the deadline of the task
	 * @param description - if a task, the description of the task
	 * 
	 * This method will take parameters and create either a subproject or a task and put
	 * this component in the correct location depending on the parameters that are given to it
	 */
	public void addComponent(String name, String component, String subproject, String owner, Date deadline, String description) {

		MATSComponent componentObj = projectFolder.findComponent(subproject);

		if (name.length()==0) {
			notifyViews("Error Occured- Invalid component name!", Action.ERROR);
			return;
		}
		
		if (getComponent(name)!= null){
			notifyViews("Error Occurred - A component with that name already exists!", Action.ERROR);
			return;
		}
		
		if ((componentObj instanceof MATSTask) || (componentObj == null)) {
			notifyViews("Error Occurred - Invalid Subproject Name!", Action.ERROR);
		}

		else {

			MATSSubproject subprojectObj = (MATSSubproject)componentObj;


			if (component.equals("Task")) {
				MATSUser ownerObj = userDatabase.findUser(owner);

				subprojectObj.addComponent(new MATSTask(whoAmI, ownerObj, deadline, name, description));
				notifyViews("New task with the name " + name +" was added to the subproject "+ subproject, Action.NEW_TASK_CREATED );
			}

			else if (component.equals("Subproject")) {
				subprojectObj.addComponent(new MATSSubproject(name, description));
				notifyViews("New subproject with the name " + subproject + " was added", Action.NEW_SUBPROJECT_CREATED);
			}
		}
	}


	/**
	 * @author elee3
	 * @param task - the task which we'd like to re-assign
	 * @param newOwner - the new owner we'd like to assign this task to
	 * 
	 * This method will take a string representation of a task, find a task, and
	 * try to re-assign this task to a given user if it is acceptable
	 */
	public void assignTask(String task, String newOwner) {

		MATSComponent componentObj = projectFolder.findComponent(task);

		if ((componentObj instanceof MATSSubproject) || (componentObj == null)) {
			notifyViews("Error Occurred Assigning a Task", Action.ERROR);
		}

		MATSTask taskObj = (MATSTask)componentObj;
		MATSUser newOwnerObj = userDatabase.findUser(newOwner);

		if (newOwnerObj != null) {
			taskObj.changeOwner(newOwnerObj, whoAmI);
			notifyViews("The task with name " + task + " was assigned to " + newOwner, Action.TASK_ASSIGNED);
		}

		notifyViews("Unable to assign task to that user because of invalid user name", Action.ERROR);
	}

	/**
	 * @author rparames
	 * @param name - the name of the project to add to the collection
	 * @param description - the description of the project we'd like to add 
	 * 
	 * This method will add a new project to the project collection
	 */
	public void addNewProject(String name, String description) {
		
		if (name.length()==0) {
			notifyViews("Invalid project name!", Action.ERROR);
			return;
		}
		
		if (getComponent(name)!= null){
			notifyViews("Error Occurred - A component with that name already exists!", Action.ERROR);
			return;
		}
		
		if (!projectFolder.containsSubproject(name)) {
			projectFolder.addProject(name, description);
			notifyViews("New project with the name" + name +" was added", Action.NEW_SUBPROJECT_CREATED);
		}
		else {
			notifyViews("You can't add subprojects with the same name!", Action.ERROR);
			return;
		}
	}

	/**
	 * @author elee3
	 * @param username- the name of the user for whom we'd like to find a list of tasks owned
	 * 
	 * This method will take a string of a user and actually go through all of the folders
	 * and find all of the tasks that this owner owns, then set the value of returnString
	 * to a list of these tasks
	 */
	public List<String> getTasksOwned (String username) {

		MATSUser user = userDatabase.findUser(username);

		if (user == null) {
			notifyViews("Invalid user name to find tasks owned for", Action.ERROR);
		}

		List<MATSTask> tasksOwned = projectFolder.findTasksOwned(user);
		List<String> tasksOwnedNames = new LinkedList<String>();

		for (MATSTask ownedTask : tasksOwned) {
			tasksOwnedNames.add(ownedTask.getName());
		}

		return tasksOwnedNames;
	}
	/**
	 * @author boconno3
	 * @param username - the name of the user for whom we'd like to find the list of tasks created
	 * 
	 * This method will take a string of a user's name and find all the tasks that 
	 * this owner created, then return a string representative list of these by setting
	 * the value returnString with this value
	 */	
	public List<String> getTasksCreated(String username) {

		MATSUser user = userDatabase.findUser(username);

		if (user == null) {
			notifyViews("Invalid user name to get tasks created for", Action.ERROR);
		}		

		List<MATSTask> tasksCreated = projectFolder.findTasksCreated(user);
		List<String> tasksCreatedNames = new LinkedList<String>();

		for (MATSTask createdTask : tasksCreated) {
			tasksCreatedNames.add(createdTask.getName());
		}

		return tasksCreatedNames;
	}


	/**
	 * @author elee3
	 * @param name - the name of the component that we'd like to search for
	 * @return the component if it was found, null if it was not
	 * 
	 * This method will take a string of a component and search the entire
	 * project folder for this component
	 */
	public MATSComponent getComponent(String name) {
		return projectFolder.findComponent(name);
	}

	/**
	 * @author asaini
	 * @return the current user of the model 
	 * 
	 * This method will return the current user that is logged into the model
	 */
	public MATSUser getWhoAmI() {
		return whoAmI;
	}

	/**
	 * @author boconno3
	 * @param name - the name of the user we'd like to search for
	 * @return a boolean which is true if user is in database, false if not
	 * 
	 * This method searches a user database for a user with the specified username
	 */
	public boolean containsUser(String name) {
		if (userDatabase.findUser(name) == null ) {
			return false;
		}
		return true;
	}

	/**
	 * @author  asaini
	 * Prints all of the users in the database to the returnString value
	 */
	public List<String> getAllUsers() {
		return userDatabase.getAllUsers();
	}

	public String toString() {
		return userDatabase.findAll();
	}

	/**
	 * @author asaini
	 * @param state - The state (overdue, ongoing, completed or deleted) of the tasks for which to search
	 * Searches through entire collection of subprojects and tasks, creating a list of tasks that 
	 * are either overdue, ongoing, completed or deleted
	 */
	public List<String> findTasksOfState(String state) {

		List<MATSTask> tasks;
		List<String> tasksOfState = new ArrayList<String>();

		if ( state.compareToIgnoreCase("OVERDUE") == 0 ) {
			tasks = projectFolder.findTasksOfState( MATSTaskState.COMPLETED );
			for (int i= 0 ; i< tasks.size(); i++) {
				tasksOfState.add(tasks.get(i).getName());
			}
		}
		else if ( state.compareToIgnoreCase("ONGOING") == 0 ) {
			tasks = projectFolder.findTasksOfState( MATSTaskState.ONGOING );
			for (int i= 0 ; i< tasks.size(); i++) {
				tasksOfState.add(tasks.get(i).getName());
			}
		}
		else if ( state.compareToIgnoreCase("COMPLETED") == 0 ) {
			tasks = projectFolder.findTasksOfState( MATSTaskState.COMPLETED );
			for (int i= 0 ; i< tasks.size(); i++) {
				tasksOfState.add(tasks.get(i).getName());
			}
		}
		else if ( state.compareToIgnoreCase("DELETED") == 0 ) {
			tasks = projectFolder.findTasksOfState( MATSTaskState.COMPLETED );
			for (int i= 0 ; i< tasks.size(); i++) {
				tasksOfState.add(tasks.get(i).getName());
			}
		}

		return tasksOfState;
	}

	/**
	 * @author rparames
	 * @param task - the task which we want to change the deadline for
	 * @param deadline - the new deadline we want to change the task for
	 * 
	 * This method will take in a string of a task, find that task, and try
	 * to change the deadline for this task (if it is acceptable) to a given date
	 */
	public void changeDeadline(String task, Date deadline) {

		MATSComponent t = projectFolder.findComponent(task);

		if ((t instanceof MATSSubproject) || (t == null)) {
			notifyViews("Invalid task name", Action.ERROR);
		}
		else{
			MATSTask cTask= (MATSTask)t;

			if (whoAmI!=null) {

				if (!cTask.changeDeadline(whoAmI, deadline)) {
					notifyViews("You either do not have the privelege to change this description or you specified a date that has already passed!", Action.ERROR);
				}

				else {
					notifyViews("The task with the name "+ task + " had its deadline changed to " + deadline.toString(), Action.TASK_DEADLINE_CHANGED);
				}
			}
		}
	}

	/**
	 * @author rparames
	 *
	 *This method will go through all of the tasks in all of the subprojects and update
	 *their states if they are overdue
	 */
	public void refresh() {

		List<MATSTask> tasks = projectFolder.findTasksOfState(MATSTaskState.ONGOING);

		for (MATSTask task: tasks)  {
			task.changeIfOverdue();
		}

		notifyViews("All of the tasks were refreshed", Action.REFRESH);
	}
	

	/**
	 * @author rparames
	 * @param task - the task for which we'd like to change the state for
	 * @param state - the new state we'd like to take the described task to
	 * 
	 * This method will take in a string of a task, find that task, and then 
	 * try to change the state of this task to the given one's if it is allowable
	 */
	public void changeTaskState(String task, String state) {

		MATSComponent t = projectFolder.findComponent(task);

		if ((t instanceof MATSSubproject) || (t == null)) {
			notifyViews("Invalid task name to change task state on", Action.ERROR);
		}
		else{
			MATSTask cTask= (MATSTask)t;

			int destination= -1;
			//smelly code allowed at this point
			if ( state.compareToIgnoreCase("OVERDUE") == 0 ) {
				destination = MATSTaskState.OVERDUE;
			}
			else if ( state.compareToIgnoreCase("ONGOING") == 0 ) {
				destination = MATSTaskState.ONGOING;
			}
			else if ( state.compareToIgnoreCase("COMPLETED") == 0 ) {
				destination = MATSTaskState.COMPLETED;
			}
			else if ( state.compareToIgnoreCase("DELETED") == 0 ) {
				destination = MATSTaskState.DELETED;
			}

			if (!cTask.changeState(destination, whoAmI)) {
				notifyViews("You either do not have the privelege to change the state of this task or the specified state is impossible to reach", Action.ERROR);
			}
			else {
				notifyViews("The task with the name "+ task + " had its state changed to " + state, Action.TASK_STATE_CHANGED);
			}
		}

	}

	/**
	 * @author asaini
	 * @param task -the task for which we'd like to chagne the owner
	 * @param username - the new owner of the task
	 * 
	 * This method will take a string of a task, find that task, and then change
	 * the owner of that task to an owner which is also given ot the method in 
	 * string form
	 */
	public void changeTaskOwner(String task, String username) {

		MATSComponent t = projectFolder.findComponent(task);

		if ((t instanceof MATSSubproject) || (t == null)) {
			notifyViews("Invalid task name to change owner for", Action.ERROR);
		}
		else{
			MATSTask cTask= (MATSTask)t;
			MATSUser user = userDatabase.findUser(username);

			if (user!=null) {
				if (!cTask.changeOwner(user, whoAmI)) {
					notifyViews("The owner of this task could not be changed!", Action.ERROR);
				}
				else {
					notifyViews("The task with the name "+ task + " had its owner changed to " + username, Action.TASK_OWNER_CHANGED);
				}
			}
		}
	}

	/**
	 * @author rparames
	 * @param component - the component you'd like to change description for
	 * @param description - the new description of the component
	 * 
	 * This method will take in a component in string format, find that component
	 * and change its description to a given value (in parameter)
	 */
	public void changeComponentDescription(String component, String description) {
		MATSComponent c = projectFolder.findComponent(component);

		if (!c.setDescription(whoAmI, description)) {
			notifyViews("You do not have the privelege to change this description", Action.ERROR);
		}
		else {
			notifyViews("The component description successful", Action.COMPONENT_DESCRIPTION_CHANGED);
		}
	}

	/**
	 * @author rparames
	 * @param task - the task for which the history should be presented
	 * 
	 * This method will return the history of the task by setting a returnString
	 * to this value
	 */
	public String getTaskHistory(String task) {
		MATSComponent t = projectFolder.findComponent(task);
		String taskHistory = "";
		if ((t instanceof MATSSubproject) || (t == null)) {
			notifyViews("Invalid task name to print history for", Action.ERROR);
		}
		else{

			taskHistory= "The history of this task is: \n";
			MATSTask cTask= (MATSTask)t;
			List<String> history = cTask.getHistory();

			for (int i = 0; i<history.size(); i++) {
				taskHistory+= history.get(i)+ "\n";
			}
		}

		return taskHistory;
	}

	/**
	 * @author rparames
	 * @param task- the task for which to display the state
	 * 
	 * This method takes a string representation of a state and sets the returnString 
	 * to the state of this task
	 */
	public String getTaskState(String task) {
		MATSComponent t = projectFolder.findComponent(task);

		if ((t instanceof MATSSubproject) || (t == null)) {
			notifyViews("Invalid task name to print task state for", Action.ERROR);
		}
		else{
			MATSTask cTask= (MATSTask)t;
			int state = cTask.getState();
			String currentState = "";

			if (state ==MATSTaskState.OVERDUE) {
				currentState = "OVERDUE";
			}
			else if (state==MATSTaskState.COMPLETED) {
				currentState = "COMPLETED";
			}
			else if (state == MATSTaskState.ONGOING) {
				currentState = "ONGOING";
			}
			else if (state == MATSTaskState.DELETED) {
				currentState = "DELETED";
			}

			return currentState;
		}
		
		return "";
	}

	/**
	 * @author rparames
	 * @param component - the component which you want to print a description of
	 * 
	 * This method will take in a String parameter of a component, find that component
	 * and set the value of the returnString which goes to the view class to the description
	 * of this component
	 */
	public String getComponentDescription(String component) {
		MATSComponent c = projectFolder.findComponent(component);

		String descrip = c.getDescription();
		
		return descrip;
	}

	/**
	 * @author rparames
	 * @param task -the task for which the deadline should be displayed
	 * 
	 * This method will take a String argument, find a task associated with that
	 * string and then set the returnString to the deadline of the task (in string 
	 * form)
	 */
	public String getTaskDeadline(String task) {
		MATSComponent t = projectFolder.findComponent(task);

		if ((t instanceof MATSSubproject) || (t == null)) {
			notifyViews("Invalid task name to get task deadline for", Action.ERROR);
		}
		else{
			MATSTask cTask= (MATSTask)t;
			String deadline = cTask.getDeadline();

			return deadline;
		}
		
		return "";
	}
	
	/**
	 * @author rparames, asaini
	 * @param taskName: The task's name represented as a String 
	 * @return The owner of the paarticular task's name 
	 */
	public String getTaskOwner(String taskName) {
		MATSComponent t = projectFolder.findComponent(taskName);

		if ((t instanceof MATSSubproject) || (t == null)) {
			notifyViews("Invalid task name to get task owner for", Action.ERROR);
		}
		else{
			MATSTask cTask= (MATSTask)t;
			String owner = cTask.getOwner();

			return owner;
		}
		
		return "";
	}
	
	/**
	 * @author boconno3
	 * @param taskName: The task's name represented as a String 
	 * @return The username of the user that created the owner
	 */
	public String getTaskCreator(String taskName) {
		MATSComponent t = projectFolder.findComponent(taskName);

		if ((t instanceof MATSSubproject) || (t == null)) {
			notifyViews("Invalid task name to get task owner for", Action.ERROR);
		}
		else{
			MATSTask cTask= (MATSTask)t;
			String creator = cTask.getCreator();

			return creator;
		}
		
		return "";
	}





	/**
	 * @author rparames
	 * @param change -the string that will be passed to the view 
	 * 
	 * This method will set the setChanged() method and notify its observers of 
	 * a change by passing it a string which says the change
	 */
	public void notifyViews(String message, Action action) {

		ModelEvent event = new ModelEvent(this, message);

		Iterator <ModelListener> iter = modelListeners.iterator();

		switch (action) {
			case ERROR : {  
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).error(event);
				}
				break;
			}
			case NEW_SYSTEM_CREATED : {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).newSystemCreated(event);
				}
				break;
			}
			case IMPORTED_SYSTEM : {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).importedSystem(event);
				}
				break;
				
			}		
			case LOGGED_IN: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).loggedIn(event);
				}
				break;
			}
			case LOGGED_OUT: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).loggedOut(event);
				}
				break;
				
			}
			case NEW_USER_ADDED: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).newUserAdded(event);
				}
				break;
			}
			case NEW_TASK_CREATED: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).newTaskCreated(event);
				}
				break;
			}
			case NEW_SUBPROJECT_CREATED: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).newSubprojectCreated(event);
				}
				break;
			}
			case TASK_ASSIGNED: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).taskAssigned(event);
				}
				break;
			}
			case REFRESH: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).refresh(event);
				}
				break;
			}
			case TASK_STATE_CHANGED: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).taskStateChanged(event);
				}
				break;
			}
			case TASK_OWNER_CHANGED: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).taskOwnerChanged(event);
				}
				break;
			}
			case COMPONENT_DESCRIPTION_CHANGED: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).componentDescriptionChanged(event);
				}
				break;
			}
			case TASK_DEADLINE_CHANGED: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).taskDeadlineChanged(event);
				}
				break;
			}
			case PROJECT_MOVED: {
				while ( iter.hasNext() ) {
					( (ModelListener) iter.next()).projectMoved(event);
				}
				break;
			}
			
		}
	}

	/**
	 * @authoer rparames, asaini, boconnor
	 * @param subproject - the subproject we want to display the contents of
	 * @return A list of all tasks and subprojects directly below a subproject 
	 */
	public List<String> getSubprojectDirectoryList(String subproject) {

		return projectFolder.getSubprojectChildrenNames(subproject);

	}

	/**
	 * @author elee3
	 * @param nameOfProjectToMove - the name of the project we're trying to move
	 * @param nameOfNewParentProject - the name of the destination location to move the project under
	 * @return this method will return true if moved successfully, otherwise false
	 * 
	 * This method will move a project from one location to another.
	 */
	public boolean moveProject( String nameOfProjectToMove, String nameOfNewParentProject ) {

		boolean addToFirstLevel = false;
		
		if (nameOfProjectToMove.equals(nameOfNewParentProject)) {
			notifyViews("The project could not be moved!", Action.ERROR);
			
			return false;
		}
		
		MATSSubproject projectToMove = null;
		MATSSubproject newParentProject = null;
		MATSSubproject oldParentProject = null;

		// traverse the projects and check if the name of the subproject to move exists
		if ( projectFolder.containsSubproject(nameOfProjectToMove) ) {
			projectToMove = (MATSSubproject) projectFolder.findComponent(nameOfProjectToMove);
		}
		else {
			notifyViews("The project could not be moved!", Action.ERROR);
			
			return false;
		}

		// traverse the projects and check if the name of the subproject to move exists
		if ( projectFolder.containsSubproject(nameOfNewParentProject) ) {
			newParentProject = (MATSSubproject) projectFolder.findComponent(nameOfNewParentProject);
			
			if (projectToMove.containsSubproject(newParentProject.getName())) {
				
				notifyViews("This move is illegal!", Action.ERROR);
				return false;
			}
		}
		else if (nameOfNewParentProject.equalsIgnoreCase("Project Collection")) {
			addToFirstLevel = true;
			
			
		}
		else {
			
			notifyViews("The project could not be moved!", Action.ERROR);
			return false;
		}
		
		
		
		


			// check first layer for subproject
			for ( MATSSubproject subproject : projectFolder.getChildren() ) {
				if ( subproject.getName().equals(nameOfProjectToMove) ) {
					projectFolder.remove( projectToMove );
					
					if (!addToFirstLevel ) {
						newParentProject.addComponent( projectToMove );
					}
					else {
						projectFolder.add(projectToMove);
					}
					notifyViews("The project "+nameOfProjectToMove+ " was moved underneath "+ nameOfNewParentProject, Action.PROJECT_MOVED);				
					return true;
				}
			}
			

			// check lower layers for parent of subproject
			if ( oldParentProject == null ) {
				for ( MATSSubproject subproject : projectFolder.getChildren() ) {
					oldParentProject = subproject.findParent(nameOfProjectToMove);
					if ( oldParentProject != null ) {
						
						 oldParentProject.remove( projectToMove );
							
						if (!addToFirstLevel ) {
							newParentProject.addComponent( projectToMove );
						}
						else {
							projectFolder.add(projectToMove);
						}
						notifyViews("The project "+nameOfProjectToMove+ " was moved underneath "+ nameOfNewParentProject, Action.PROJECT_MOVED);
						return true;
					}
				}
			}
		
			
			notifyViews("The project could not be moved!", Action.ERROR);
			return false;
		
	}

	/**
	 * @return The listmodel displaying all of the users in the list
	 */
	public DefaultListModel getListModel() {
		return listModel;
	}

	/**
	 * @param name of component 
	 * @return true if component with the given name is a subproject 
	 */
	public boolean isSubproject(String name) {
		if (projectFolder.findComponent(name) instanceof MATSSubproject) {
			return true;
		}
		return false;
	}

	/**
	 * @param name of component 
	 * @return true if component with the given name is a task 
	 */
	public boolean isTask(String name) {
		if (projectFolder.findComponent(name) instanceof MATSTask) {
			return true;
		}
		return false;
	}


	/**
	 * 
	 * @param listener
	 */
	public synchronized void addModelListener (ModelListener listener){
		modelListeners.add(listener);
	}

	/**
	 * 
	 * @param listener
	 */
	public synchronized boolean removeModelListener (ModelListener listener) {
		return (modelListeners.remove(listener));
	}
	
	//==========================================================
	// XML Methods 
	//==========================================================
	
	/**
	 * 
	 */
	public String toXML() {
		
		String messageXML = "";
		messageXML += "<MATSModel>";
		
		messageXML += "\r\n"+ "<userDatabase>";
		messageXML += userDatabase.toXML();
		messageXML += "\r\n"+ "</userDatabase>";
		
		messageXML += "\r\n<projectFolder>";
		messageXML += projectFolder.toXML();
		messageXML += "\r\n</projectFolder>";
		
		messageXML += "\r\n</MATSModel>";
		
		return messageXML;
	}
	

	
    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================
	
	
	// import org.xml.sax.*; SAX = (Simple API for XML)
	public void exportToXmlFile(String outputFilePath) {//throws Exception {

		try{
			FileOutputStream fos = new FileOutputStream(outputFilePath);
			PrintWriter writer = new PrintWriter(fos);
			writer.println(this.toXML());
			writer.close();
			//out.close();
			fos.close();
		}
		catch(IOException ex){
			return;
		}
	}

	// import org.xml.sax.*; SAX = (Simple API for XML)
	public static MATSModel importFromXmlFile(String inputFilePath) throws Exception {
				
		/**
		 * The temporary variable used as the value of many hashmaps.
		 */
		charsBetweenTags = "";
		
		/**
		 * The list of strings used to keep track of the class name tags
		 */
		classStack = new Stack<String>();
		
		/**
		 * The list of counters used to keep track of to count the number of components in each subproject.
		 */
		//userStack = new Stack<MATSUser>();
		//tempMatsUsers = new ArrayList<MATSUser>();
		
		/**
		 * The list of MATS components used to keep track of.
		 */
		componentStack = new Stack<MATSComponent>();

		/**
		 * The list of MATS components used to keep track of.
		 */
		subprojectInfoStack = new Stack<HashMap<String,String>>();
		
		/**
		 * A list used to map the nametags with the contents of the MATSUser class.
		 */
		tempMatsUserHashMap = new HashMap<String, String>();
		
		
		importingNewModel = new MATSModel();
		
		fileInput = new FileInputStream(inputFilePath);
		
		
		// process XML Document HERE
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			
		
			SAXParser parser = factory.newSAXParser();
			parser.parse(fileInput, importingNewModel);
			
		}
		catch (Throwable t) {
			
			return null;			
		}
		
		for ( String userName : importingNewModel.getAllUsers() ) {
			importingNewModel.getListModel().addElement(userName);
		}

		return importingNewModel;
	}

	
	/**
     * Receive notification of the beginning of the document.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the beginning
     * of a document (such as allocating the root node of a tree or
     * creating an output file).</p>
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startDocument
     */
    public void startDocument () throws SAXException {
        //emit ("<?xml version='1.0' encoding='UTF-8'?>");
        //nl();
    }

    


    
    /**
     * Receive notification of the start of an element.
     *
     * take specific actions at the start of each element
     * 
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement(String namespaceURI,
    						 String localName,
    						 String qualifiedName,
    						 Attributes atts) throws SAXException {
    	
    	//System.out.println("localName: " + localName + " namespaceURI: " + namespaceURI + " qualifiedName: " + qualifiedName);

    	if ( qualifiedName.equalsIgnoreCase("MATSModel") ) {
    		classStack.push(localName);
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("userDatabase") && classStack.peek().equalsIgnoreCase("MATSModel") ) {
    		
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("MATSUserDatabase") ) {
    		classStack.push(localName);
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("userDatabase") && classStack.peek().equalsIgnoreCase("MATSUserDatabase") ) {
    		
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("MATSUser") ) {
    		classStack.push(localName);
    		tempMatsUserHashMap.put("name","");
    		tempMatsUserHashMap.put("password","");
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("name") && classStack.peek().equalsIgnoreCase("MATSUser") ) {
    		
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("password") && classStack.peek().equalsIgnoreCase("MATSUser") ) {
    		
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("projectFolder") && classStack.peek().equalsIgnoreCase("MATSModel") ) {
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("MATSProjectCollection") ) {
    		classStack.push(localName);
    		componentStack.push(null);
    		
    		tempMatsProjectCollection = new MATSProjectCollection();
    		
    		
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("projectCollection") && classStack.peek().equalsIgnoreCase("MATSProjectCollection") ) {

    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("MATSSubproject") ) {
    		classStack.push(localName);
    		componentStack.push(null); 
    		
    		HashMap<String,String> tempMatsSubprojectInfo = new HashMap<String,String>();
    		
    		tempMatsSubprojectInfo.put("name", "");
    		tempMatsSubprojectInfo.put("description", "");
    		
    		subprojectInfoStack.push(tempMatsSubprojectInfo);
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("name") && classStack.peek().equalsIgnoreCase("MATSSubproject") ) {
    		
    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("description") && classStack.peek().equalsIgnoreCase("MATSSubproject") ) {

    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("subprojectFolder") && classStack.peek().equalsIgnoreCase("MATSSubproject") ) {

    	}
    	// finished
    	else if ( qualifiedName.equalsIgnoreCase("MATSTask") ) {
    		classStack.push(localName);
    		
    		tempMatsTaskHistory = new ArrayList<String>();
    		tempMatsTaskDate = new Date();
    		
    		tempMatsTaskHashMap = new HashMap<String,String>();
    		
    		tempMatsTaskHashMap.put("name", "");
    		tempMatsTaskHashMap.put("description", "");
    		tempMatsTaskHashMap.put("creator", "");
    		tempMatsTaskHashMap.put("owner", "");
    		tempMatsTaskHashMap.put("currentState", "");
    	}
    	else if ( qualifiedName.equalsIgnoreCase("name") && classStack.peek().equalsIgnoreCase("MATSTask") ) {

    	}
    	else if ( qualifiedName.equalsIgnoreCase("description") && classStack.peek().equalsIgnoreCase("MATSTask") ) {

    	}
    	else if ( qualifiedName.equalsIgnoreCase("creator") && classStack.peek().equalsIgnoreCase("MATSTask") ) {

    	}
    	else if ( qualifiedName.equalsIgnoreCase("owner") && classStack.peek().equalsIgnoreCase("MATSTask") ) {

    	}
    	else if ( qualifiedName.equalsIgnoreCase("currentState") && classStack.peek().equalsIgnoreCase("MATSTask") ) {
    		
    	}
    	else if ( qualifiedName.equalsIgnoreCase("MATSTaskState") ) {
    		classStack.push(localName);
    	}
    	else if ( qualifiedName.equalsIgnoreCase("history") && classStack.peek().equalsIgnoreCase("MATSTaskState") ) {

    	}
    	else if ( qualifiedName.equalsIgnoreCase("deadline") && classStack.peek().equalsIgnoreCase("MATSTaskState") ) {

    	}
    }
    
    /**
     * Receive notification of character data inside an element.
     *
     * take specific actions for each chunk of character data
     *
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the
     *               character array.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#characters
     */
    public void characters (char ch [], int start, int length) throws SAXException {

    	//String tempCharacter = new String(ch, start, length);
    	
    	String tempCharacter = "";
        for (int i = start; i < start+length; i++) {
        	if ( ch[i] == '\n' || ch[i] == '\r' ) {
        		//System.out.println("Encountered a slash n or slash r");
        		continue;
        	}
        	tempCharacter += ch[i];
        }
        
        if ( ! tempCharacter.equals("") ) {
            System.out.println("characters(): " + tempCharacter);
            charsBetweenTags = tempCharacter;
        }

    }
    
    

    
    /**
     * Receive notification of the end of an element.
     *
     * take specific actions at the end of
     * each element (ie finalising adding a MATSUser to a userDatabase).</p>
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement (String localName) throws SAXException {
    	
    	System.out.println("EndElement()->localName: " + localName);
    	
    	if ( localName.equalsIgnoreCase("MATSModel") ) {
    		classStack.pop();
    	}
    	else if ( localName.equalsIgnoreCase("userDatabase") && classStack.peek().equalsIgnoreCase("MATSModel") ) {
    		// save the temp MATSUserDatbase to the userDatabase field in MATSModel
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("MATSUserDatabase") ) {
    		classStack.pop();
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("userDatabase") && classStack.peek().equalsIgnoreCase("MATSUserDatabase") ) {

    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("MATSUser") ) {
    		classStack.pop();
    		
    		addUser( tempMatsUserHashMap.get("name") ,tempMatsUserHashMap.get("password") );
    		
    		importingNewModel.addUser( tempMatsUserHashMap.get("name") ,tempMatsUserHashMap.get("password") );
 
    		tempMatsUserHashMap.remove("name");		    	// Clean tempMatsUserHashMap key="name"
    		tempMatsUserHashMap.remove("password");    		// Clean tempMatsUserHashMap key="password"
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("name") && classStack.peek().equalsIgnoreCase("MATSUser") ) {
    		tempMatsUserHashMap.put("name",charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("password") && classStack.peek().equalsIgnoreCase("MATSUser") ) {
    		tempMatsUserHashMap.put("password",charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("projectFolder") && classStack.peek().equalsIgnoreCase("MATSModel") ) {
    		
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("MATSProjectCollection") ) {
    		classStack.pop();
    		importingNewModel.setProjectFolder(tempMatsProjectCollection);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("projectCollection") && classStack.peek().equalsIgnoreCase("MATSProjectCollection") ) {
    		
    		while ( !componentStack.empty() ) {
    			MATSComponent tempMatsComponent = componentStack.pop();
    			
    			if ( tempMatsComponent!= null ) {
    				break;
    			}
    			else if (  tempMatsComponent instanceof MATSTask ) {
    				// CAUSE ERROR HERE (Should not see MATSTask here)
    				break;
    			}
    			else if ( tempMatsComponent instanceof MATSSubproject ) {
    				tempMatsProjectCollection.add( (MATSSubproject) tempMatsComponent );
    			}
    		}
 
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("MATSSubproject") ) {
    		classStack.pop();
    		
    		HashMap<String,String> tempMatsSubprojectInfo = subprojectInfoStack.pop();
    		
    		MATSSubproject tempMatsSubproject = new MATSSubproject( tempMatsSubprojectInfo.get("name") , tempMatsSubprojectInfo.get("description") );
    		
      		while ( !componentStack.empty() ) {
    			MATSComponent tempMatsComponent = componentStack.pop();
    			
    			if ( tempMatsComponent!= null ) {
    				break;
    			}
    			else  {
    				tempMatsSubproject.addComponent(tempMatsComponent);    				
    			}
    		}
      		
      		componentStack.push(tempMatsSubproject);
    		
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("name") && classStack.peek().equalsIgnoreCase("MATSSubproject") ) {
    		subprojectInfoStack.peek().put("name", charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("description") && classStack.peek().equalsIgnoreCase("MATSSubproject") ) {
    		subprojectInfoStack.peek().put("description", charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("subprojectFolder") && classStack.peek().equalsIgnoreCase("MATSSubproject") ) {
    		
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("MATSTask") ) {
    		classStack.pop();

    		
    		MATSTask tempMatsTask = new MATSTask( importingNewModel.findUser( tempMatsTaskHashMap.get("creator") ),
    				                              importingNewModel.findUser( tempMatsTaskHashMap.get("owner") ),
    				                              tempMatsTaskDate,
    				                              tempMatsTaskHashMap.get("name"),
    				                              tempMatsTaskHashMap.get("description") );
    		
    		for ( String message : tempMatsTaskHistory ) {
    			tempMatsTask.writeToHistory(message);
    		}
    		
    		componentStack.push(tempMatsTask);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("name") && classStack.peek().equalsIgnoreCase("MATSTask") ) {
    		tempMatsTaskHashMap.put("name", charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("description") && classStack.peek().equalsIgnoreCase("MATSTask") ) {
    		tempMatsTaskHashMap.put("description", charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("creator") && classStack.peek().equalsIgnoreCase("MATSTask") ) {
    		tempMatsTaskHashMap.put("creator", charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("owner") && classStack.peek().equalsIgnoreCase("MATSTask") ) {
    		tempMatsTaskHashMap.put("owner", charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("currentState") && classStack.peek().equalsIgnoreCase("MATSTask") ) {
    		tempMatsTaskHashMap.put("currentState", charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("MATSTaskState") ) {
    		classStack.pop();
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("history") && classStack.peek().equalsIgnoreCase("MATSTask") ) {
    		tempMatsTaskHistory.add(charsBetweenTags);
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("deadline") && classStack.peek().equalsIgnoreCase("MATSTaskState") ) {
    		
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("year") && classStack.peek().equalsIgnoreCase("MATSTaskState") ) {
    		tempMatsTaskDate.setYear( Integer.parseInt(charsBetweenTags) );
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("month") && classStack.peek().equalsIgnoreCase("MATSTaskState") ) {
    		tempMatsTaskDate.setMonth( Integer.parseInt(charsBetweenTags) );
    	}
    	// finished
    	else if ( localName.equalsIgnoreCase("day") && classStack.peek().equalsIgnoreCase("MATSTaskState") ) {
    		//tempMatsTaskDate.setDay( Integer.parseInt(charsBetweenTags) );
    		tempMatsTaskDate.setDate( Integer.parseInt(charsBetweenTags) );
    	}
    }

    

    /**
     * Receive notification of the end of the document.
     *
     * take specific actions at the end
     * of a document (ie finalising a MATSModel)
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#endDocument
     */
    public void endDocument () throws SAXException {
    	
        try {
        	
            fileInput.close();
        } catch (IOException e) {
            throw new SAXException ("I/O error", e);
        }
        
    }
	

}