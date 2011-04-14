package mats_controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


import mats_model.MATSModel;
import mats_view.MATSView;
import mats_view.TreeNode;

/**
 * 
 * @author boconno3, elee, rparames, asaini 
 *
 */
public class MATSController implements ActionListener, TreeSelectionListener {

	/**
	 * The model interacting with controller 
	 */
	private MATSModel model;

	/**
	 * The view interacting with controller 
	 */
	private MATSView view;


	/**
	 * The user wil create a new instance of the model 
	 */
	public MATSController() {
		view = new MATSView(this);
	}


	/** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {

		TreeNode node;
		try {
			node = (TreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
		} catch (Exception exception) {

			return;
		}

		view.displayComponentInfo( (String) node.getUserObject()  );
	}


	/**
	 *@author boconno3, rparames, asaini, elee3
	 * 
	 * Opens the application and presents the user
	 * with option of starting a new application model, importing a saved model or 
	 * connecting an external model through the internet
	 */
	public void startMATSApplication() {	
		view = new MATSView(this);
	}

	/**
	 * This method sets the model variable in oru class
	 * 
	 * @param model - the model you want to set this model variable as
	 */
	private void setModel( MATSModel model ) {
		this.model = model;
	}

	/**
	 * 
	 * @param event When an event is triggered, the MATSController will do the appropriate
	 *              action based on the action command
	 */
	public void actionPerformed(ActionEvent event) {

		if ( event.getActionCommand().equals("new mats system") ) { //when user creates a new MATS system
			createNewSystem();
		}
		else if ( event.getActionCommand().equals("Load") ) { //When user chooses to load existing system
			load();       											//TO BE IMPLEMENTED
		}
		else if ( event.getActionCommand().equals("Save") ) { //When user chooses to save current system
			exportModel();									//TO BE IMPLEMENTED
		}
		else if (event.getActionCommand().equals("Save_XML")) {
			exportModelXML();
		}
		else if (event.getActionCommand().equals("Load_XML")) {
			loadXML();
		}
		else if (event.getActionCommand().equals("Close")) {
			close();
		}
		else if (event.getActionCommand().equals("Exit")) {
			exit();
		}
		else if (event.getActionCommand().equals("move project")) {
			moveProject();	
		}
		else if ( event.getActionCommand().equals("log out") ) { //logs out the user
			logout();
			
		}
		else if ( event.getActionCommand().equals("display_tasks_owned")) { //displays associated tasks of user
			displayTasksOwned(); 
		}
		else if ( event.getActionCommand().equals("display_tasks_created")) { //displays associated tasks for user
			displayTasksCreated();
		}
		else if ( event.getActionCommand().equals("new project") ) { //if a new project is created
			addNewProject();	
		}
		else if ( event.getActionCommand().equals("new task") ) {
			addNewTask();
		}
		else if (event.getActionCommand().equals("new user")) {
			addNewUser();
		}
		else if (event.getActionCommand().equals("log in")) { 
			login();
		}
		else if (event.getActionCommand().equals("change_task_state")) {
			changeTaskState();
		}
		else if (event.getActionCommand().equals("change_task_deadline")) {
			changeTaskDeadline();
		}
		else if (event.getActionCommand().equals("change_task_owner")) {
			changeTaskOwner();
		}
		else if (event.getActionCommand().equals("refresh")) {
			refresh();
		}
	}


	/**
	 * @author boconno3, elee3
	 * 
	 * Exports the applicaition model to store on a file on computer 
	 * for future use. 
	 */
	private void exportModel() {
		String pathOutputFile = view.showInputDialog("Where would you like to save the MATS model?");
		try {
			model.exportMATSModel( pathOutputFile );
		} catch ( Exception exception ) {
			view.showMessageDialog("Exporting file failed - exception: " + exception );
		}

		view.appendToProjectTextArea("Export Project Successfuly");
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method exports a model which has been saved in xml format and will load it into the current
	 * system
	 *
	 */
	private void exportModelXML() {
		String outputFilePath = view.showInputDialog("Where do you want to save this XML file?");
		try {
			model.exportToXmlFile(outputFilePath);
		}
		catch (Exception e) {
			
			view.displayError("Export was unsuccessful!");
			return;
		}
		view.writeToProjectTextArea("Export was successful!");
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 *
	 * This method will move a subproject into another subproject or the project collection given that
	 * it is a valid move
	 */
	private void moveProject() {
		TreeNode node = null;
		String lastSelected = "";
		String newProject = "";
		if ( view.getTree().getLastSelectedPathComponent() instanceof TreeNode) {
			node = (TreeNode) view.getTree().getLastSelectedPathComponent();
			lastSelected = (String) node.getUserObject();
			
			if (model.isSubproject(lastSelected)) {
				newProject = view.showInputDialog("What is the name of the subproject you want to add it under? \n\n The subproject must be in the database");
				
			
				try{
					if (model.isSubproject(newProject) ||  (newProject.equalsIgnoreCase("Project Collection"))) {
						model.moveProject(lastSelected, newProject);
					
					}
					else {
						view.displayError("The project could not be moved!");
					}
				}
				catch (Exception e) {
					return;
				}
			}
			else {
				view.displayError("The project could not be moved!");
			}
		}
		
			try {
				lastSelected = (String) view.getTree().getLastSelectedPathComponent().toString();
			} catch (Exception exception) {
				return;
			}
	}
	
	/**
	 * @author boconno3, elee3, rparames, asaini
	 * 
	 * Description:
	 * Hardcoded main method that sends commands to the model controller
	 * until GUI is implemented. 
	 */
	public static void main(String[] args) {
		new MATSController();
	}
	
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will add a new user into the database by asking the user of the system for the characteristics
	 * of the user
	 */
	private void addNewUser() {
		String name = view.showInputDialog("What is the name of the new user?");
		if ( name == null ) {
			return;
		}

		String password = view.showInputDialog("What is the password of the new user?");
		if ( password == null ) {
			return;
		}
		if (model.addUser(name, password)) {
			model.login(name, password);
			refresh();
		}
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will add a new project into the project collection if the user has the project
	 * collection selected when they try to create a new subproject
	 */
	private void addNewProject() {
		
		TreeNode node = null;
		String lastSelected = "";
		if ( view.getTree().getLastSelectedPathComponent() instanceof TreeNode) {
			node = (TreeNode) view.getTree().getLastSelectedPathComponent();
		}
		
			try {
				lastSelected = (String) view.getTree().getLastSelectedPathComponent().toString();
			} catch (Exception exception) {
				return;
			}
		

		if (lastSelected.equals ("Project Collection")) {

			String name = view.showInputDialog("What is the name of the new subproject? (Do not use a component name that already exists)");
			if ( name == null ) {
				return;
			}

			String description = view.showInputDialog("What is the description?");
			if ( description == null ) {
				return;
			}


			model.addNewProject(name, description);
		}

		else {

			if ( node != null ) {
				String name = view.showInputDialog("What is the name of the new subproject? (Do not use a component name that already exists)");
				if ( name == null ) {
					return;
				}

				String parent = (String) node.getUserObject();
				if ( parent == null ) {
					return;
				}

				String description = view.showInputDialog("What is the description?");
				if ( description == null ) {
					return;
				}

				model.addComponent( name, "Subproject", parent, null, null, description  );
			}

			else {
				view.displayError("Please select a subproject to add a subproject under");
			}
		}
	}
		
		/**
		 * @author boconno3, elee3, asaini, rparames
		 * 
		 * This method will ask the user for all of the characteristics of a task that they want to add
		 * and will then, depending on where in the tree they have selected, insert the task in the correct
		 * place if it is a valid move
		 */
		private void addNewTask(){
			TreeNode node = null;
			String lastSelected = "";

			if ( view.getTree().getLastSelectedPathComponent() instanceof TreeNode) {
				node = (TreeNode) view.getTree().getLastSelectedPathComponent();
			}
			else {
				try {
					lastSelected = (String) view.getTree().getLastSelectedPathComponent().toString();
				} catch (Exception exception) {
					return;
				}
			}
			



			if (lastSelected.equals ("Project Collection")) {

				view.displayError("You cannot add a task here!");
			}
			else {


				if ( node != null ) {

					Date date;
					
					String name = view.showInputDialog("What is the name of the new task? (Do not use a component name that already exists)");
					if ( name == null ) {
						return;
					}

					String parent = (String) node.getUserObject();
					if ( parent == null ) {
						return;
					}

					String owner = view.showInputDialog("Who is the owner of the task? (Please enter a user that exists in the current database)");
					if ( owner == null ) {
						return;
					}
					else if (!(model.containsUser(owner))) {
						view.displayError("That owner is not in the database");
						return;
					}


					String deadline= view.showInputDialog("Please enter the date for the deadline of the task? (Use the format yyyy/mm/dd ");
					try {
						date = new Date(deadline);
					}
					catch (Exception exception) {
						view.displayError("An invalid date was entered");
						return;		
					}
					if ( deadline == null ) {
						return;
					}


					String description = view.showInputDialog("Please enter the description for the task: ");
					if ( description == null ) {
						return;
					}

					if (parent.equalsIgnoreCase("Project Collection")) {
						view.displayError("You cannot add a task here!");
					}
					else {
						model.addComponent( name, "Task", parent, owner, date, description );
					}
				}
				else {
					view.displayError("Please select a subproject to add a task under");
				}
			}
	}
	
	/**
	* @author boconno3, elee3, asaini, rparames
	* 
	* This method is a method which essentially is a log on menu which asks the user for their information
	* and logs them in if the given info mathes information in the user database
	*/
	private void login() {
		
		String name = view.showInputDialog("What is the name of the user?");
		if ( name == null ) {
			return;
		}

		String password = view.showInputDialog("What is the password of the user?");
		if ( password == null ) {
			return;
		}

		if (model.login(name, password)) {
			refresh();
			view.updateTree();
		}
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will, if the user has selected a task in the tree that they are a creator for, allow the user
	 * to change the state of the task as long as the destination state is a viable one
	 *
	 */
	private void changeTaskState() {
		TreeNode node = null;
		
		if ( view.getTree().getLastSelectedPathComponent() instanceof TreeNode) {
			node = (TreeNode) view.getTree().getLastSelectedPathComponent();
		}
		else {
			
			return;
		}
		if (node!= null) {
			String state =  view.showInputDialog("What is the new state of the task? \n\n Please input either ONGOING, DELETED, or COMPLETED");

			model.changeTaskState((String) node.getUserObject(), state);

		}
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will, if the user has selected a task in the tree for which they are a creator,
	 * allow the user to change the deadline of the task if the new deadline is a viable one
	 *
	 */
	private void changeTaskDeadline() {
		TreeNode node = null;
		if ( view.getTree().getLastSelectedPathComponent() instanceof TreeNode) {
			node = (TreeNode) view.getTree().getLastSelectedPathComponent();
		}
		else {
			
			return;
		}
		if (node!= null) {
			Date date;

			String deadline= view.showInputDialog("Please enter the date for the new deadline of the task? (Use the format yyyy/mm/dd) ");
			try {
				date = new Date(deadline);
			}
			catch (Exception exception) {
				view.displayError("An invalid date was entered");
				return;		
			}

			model.changeDeadline((String) node.getUserObject(),date);
		}

	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will, if the user has selected a task in the tree for which they are a creator, allow them
	 * to change the owner of the task as long as the owner exists in the database
	 */
	private void changeTaskOwner() {
		
		TreeNode node = null;
		if ( view.getTree().getLastSelectedPathComponent() instanceof TreeNode) {
			node = (TreeNode) view.getTree().getLastSelectedPathComponent();
		}
		else {

			return;

		}
		if (node!= null) {
			String owner = view.showInputDialog("Who is the new owner of the task? (User must exist in the current database)");
			if ( owner == null ) {
				return;
			}
			else if (!(model.containsUser(owner))) {
				view.displayError("That owner is not in the database");
				return;
			}

			model.changeTaskOwner((String) node.getUserObject(), owner);
		}
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will create a new system and initialize all the values for this inital system for a blank
	 * project collection with no users
	 *
	 */
	private void createNewSystem() {
		setModel( new MATSModel() );
		view.setModel( model );
		view.enableNewSystemMenuOptions();
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will load a serialized file that has been saved ( a MATS system ) as the current project
	 * collection (will also load its users)
	 *
	 */
	private void load() {
		
		
		String pathInputFile = view.showInputDialog("Where would you like to load the MATS model from?");
		try {
			setModel(MATSModel.importMATSModel(pathInputFile));
			view.setModel( model );
			view.enableImportedSystemMenuOptions();			
		} catch ( Exception exception ) {
			view.showMessageDialog("Importing file failed - exception: " + exception );
		}
		
		view.enableImportedSystemMenuOptions();
		view.appendToProjectTextArea("Imported Project Successfuly");
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will load a XML file and set it as the current project
	 *
	 */
	private void loadXML() {
		
		String pathInputFile = view.showInputDialog("Where would you like to load the MATS model from?");
		try {
			setModel(MATSModel.importFromXmlFile(pathInputFile));
			view.setModel( model );
			view.enableImportedSystemMenuOptions();		
		} catch ( Exception exception ) {
			view.displayError("Importing file failed - exception: " + exception );
			return;
		}

		view.appendToProjectTextArea("Imported Project Successfuly");
		
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will actually close the current project that is open in the view without asking
	 * the user to save their project
	 *
	 */
	private void close() {
		setModel(null);
		view.closeModelWindow();
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will log the current user that is logged into the MATS system out of the system
	 *
	 */
	private void logout() {
		model.logout();
		view.enableLoggedOutMenuOptions();
	}
	
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will, when a user has been selected in the list, display all of the tasks thtat the user owns
	 *
	 */
	private void displayTasksOwned() {
		view.getTasksOwned();
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will, when a user has been selected in the list, display all of the tasks that the user has 
	 * created
	 *
	 */
	private void displayTasksCreated() {
		view.getTasksCreated();
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will actually recursively go through all of the tasks in the project collection and check
	 * if any are overdue. If they are, it will change their state to reflect this.
	 *
	 */
	private void refresh() {
		model.refresh();
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * This method will exit the program
	 */
	private void exit() {
		System.exit(0);
	}
}