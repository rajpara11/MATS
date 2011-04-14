package mats_view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import mats_model.MATSModel;
import mats_model.ModelEvent;
import mats_controller.MATSController;
import mats_model.ModelListener;

/**
 * 
 * @author boconno3, elee, rparames, asaini
 *
 */
public class MATSView extends JFrame implements ModelListener  {
	
	/*
	 * GUI CONSTANTS
	 */
	private final int ROWS = 20;
	private final int COLUMNS = 55;
	
	/*
	 * The reference of the model for which this view is an observer
	 */
	private MATSModel model;
	private MATSController controller;
	
	
	/*
	 * GUI Variables
	 */
	private JTextArea projectTextArea;
	private JTextArea taskInfoTextArea;
	private JMenu fileMenu, editMenu, projectMenu, usersMenu;
	private JMenuItem fileMenuCreate, fileMenuSave, fileMenuLoad, fileMenuClose, fileMenuExit, fileMenuSaveAsXML, fileMenuLoadAsXML;
	private JMenuItem editMenuRefresh;
	private JMenuItem projectMenuCreateSubproject, projectMenuCreateTask, projectMenuMoveSubproject, projectMenuDisplayTasksOwned, projectMenuDisplayTasksCreated,projectMenuChangeTaskState, projectMenuChangeTaskDeadline, projectMenuChangeTaskOwner;
	private JMenuItem usersMenuAddNewUser, usersMenuLogin, usersMenuLogout, usersMenuEdit;
	private JList usersList;
    private JTree tree;
    private JSplitPane splitPane;
	
	/**
	 * @author elee3
	 * @param controller - The controller reference for which to direct actions to
	 * 
	 */
	public MATSView(MATSController controller ) {
		super( "Multi-user Action Tracking System Application" );
		this.controller = controller;
		
		createGUI();
	}
	
	/**
	 * @author elee3, asaini, rparames
	 * 
	 * Displays a physical representation of the sub-projects and tasks in a tree format
	 */
	public void updateTree() {
		
		// temporary node used to populate the tree
		TreeNode node = null;
		
		// root node for which all nodes fall under
        TreeNode rootNode = new TreeNode("Project Collection");

        // get the names of all subproject at the highest level in the model
        ArrayList<String> names = model.getNames();
        
        // Create the child nodes
        for ( int index=0; index < names.size() ; index++ ) {
        	node = new TreeNode( names.get(index) , true, false );
        	rootNode.add(node);
            populateTree(node, names.get(index));
        } 
        
        // Create a tree that allows one selection at a time.
        tree = new JTree(rootNode);

        // set tree selection to only allow 1 selection at a time
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Add selection listener for tree
        tree.addTreeSelectionListener(controller);
        
        // place the tree into a scroll pane
        JScrollPane treePane = new JScrollPane(tree,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // add the scroll pane into the GUI
        splitPane.setLeftComponent(treePane);
	}
	
	/**
	 * @author boconno3, asaini
	 * @param root - The root of the subproject tree.
	 * @param name - The name of the root/subproject.
	 * 
	 * Recursive method meant to populate the tree of sub-projects and tasks with nodes.
	 */
	public void populateTree(TreeNode root, String name) {
		
		// get the names of tasks and sub-projects from the model
		List<String> childrenNames = model.getSubprojectDirectoryList(name);
		TreeNode offspring = null;

		
		for (String child : childrenNames) {
			boolean isSubproject = model.isSubproject(child);
			boolean isLeaf = !(isSubproject);
			
			// set tasks as a leaf and empty sub-projects as non-leaf
			offspring = new TreeNode(child, isSubproject, isLeaf );
			
			// add the children nodes to the parent node
			root.add(offspring);
			
			// continue populating tree for all children that are sub-projects
			if ( isSubproject ) {
				populateTree(offspring, child);				
			}
		}
	}
	
	
	/**
	 * @author elee3
	 * @param controller The controller is the class listening to action events
	 */
	private void createGUI() {
		
        // Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Project Collection");
        //createNodes(top);

        
        // Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        
        // Listen for when the selection changes.
        tree.addTreeSelectionListener(controller);
        
        
        // Create the scroll pane and add the tree to it. 
        JScrollPane treePane = new JScrollPane(tree,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        Box infoBox = Box.createVerticalBox();
        
		usersList = new JList();
		usersList.setVisibleRowCount(12);
		JScrollPane listPane = new JScrollPane(usersList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
        
        //Add the scroll panes to a split pane.
        JSplitPane splitPaneRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneRight.setLeftComponent(infoBox);
        splitPaneRight.setRightComponent( listPane );
        add(splitPaneRight);
        
        
        splitPaneRight.setDividerLocation(600); 

        //Add the scroll panes to a split pane.
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treePane);
        splitPane.setRightComponent(splitPaneRight);
        add(splitPane);
		
        splitPane.setDividerLocation(200); 
		
		projectTextArea = new JTextArea( ROWS, COLUMNS );
		projectTextArea.setEditable(false);
		JScrollPane pane1 = new JScrollPane(projectTextArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane1.setBorder(BorderFactory.createTitledBorder("Project Information Dialog Box"));
		infoBox.add(pane1);
		
		
		taskInfoTextArea = new JTextArea( ROWS, COLUMNS );
		taskInfoTextArea.setEditable(false);
		JScrollPane pane2 = new JScrollPane(taskInfoTextArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane2.setBorder(BorderFactory.createTitledBorder("Task Information Dialog Box"));
		infoBox.add(pane2);
		
		addMenuBar();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
					
				}
				
				
		);

		pack();
		setVisible(true);
	}
	
	/**
	 * @author elee3
	 * @return Accessor for the MATSController to find the last selected tree selection
	 */
	public JTree getTree() {
		return tree;
	}
	
	/**
	 * @author rparames
	 * @param The name of the component whose description we want to display.
	 */
	public void displayComponentInfo( String name ) {
		
		if ( model.isSubproject(name) ) {
			writeToProjectTextArea(model.getComponentDescription(name) );
		}
		else if (model.isTask(name)) {
			
			writeToProjectTextArea("Task Description: "+ model.getComponentDescription(name) );
			appendToProjectTextArea("Task Deadline: " + model.getTaskDeadline(name));
			appendToProjectTextArea("Task State: " +model.getTaskState(name));
			appendToProjectTextArea("Task Created By: "+ model.getTaskCreator(name));
			appendToProjectTextArea("Tasks Owned By: "+ model.getTaskOwner(name));
			
			writeToTaskInfoTextArea(model.getTaskHistory(name));
		}
		update();
		
	}
	
	/**
	 * @author rparames
	 * 
	 * Sets the menubar of the frame and adds the menus onto the menubar.
	 */
	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		addFileMenuBar(menuBar);
		addEditMenuBar(menuBar);
		addProjectMenuBar(menuBar);
		addUserMenuBar(menuBar);
	}
	
	/**
	 * @author asaini, elee3
	 * @param menuBar - The menubar which we want to add menus to.
	 * 
	 * Adds the file menu onto the menu bar, and adds menu items onto the file menu.
	 */
	private void addFileMenuBar(JMenuBar menuBar) {
		
		fileMenu = new JMenu ("File");
		menuBar.add(fileMenu);
		
		//Creates a new MATS application
		fileMenuCreate = new JMenuItem("Create New MATS System");
		fileMenuCreate.setActionCommand("new mats system");
		fileMenuCreate.addActionListener(controller);
		fileMenu.add(fileMenuCreate);
		fileMenuCreate.setEnabled(true);
		
		//Saves the current state of the MATS system as a serialized document
		fileMenuSave = new JMenuItem("Save Current MATS System");
		fileMenuSave.setActionCommand("Save");
		fileMenuSave.addActionListener(controller);
		fileMenu.add(fileMenuSave);
		fileMenuSave.setEnabled(false);
		
		//Saves the current state of the MATS system as an XML document
		fileMenuSaveAsXML = new JMenuItem("Save Current MATS System As XML Document");
		fileMenuSaveAsXML.setActionCommand("Save_XML");
		fileMenuSaveAsXML.addActionListener(controller);
		fileMenu.add(fileMenuSaveAsXML);
		fileMenuSaveAsXML.setEnabled(false);
		
		// Load a MATS system from a saved serialized document
		fileMenuLoad = new JMenuItem("Load MATS System");
		fileMenuLoad.setActionCommand("Load");
		fileMenuLoad.addActionListener(controller);
		fileMenu.add(fileMenuLoad);
		fileMenuLoad.setEnabled(true);
		
		//Load a MATS system from a saved XML document
		fileMenuLoadAsXML = new JMenuItem("Load MATS System from XML document");
		fileMenuLoadAsXML.setActionCommand("Load_XML");
		fileMenuLoadAsXML.addActionListener(controller);
		fileMenu.add(fileMenuLoadAsXML);
		fileMenuLoadAsXML.setEnabled(true);
		
		//Close the current state of the MATS system
		fileMenuClose = new JMenuItem("Close MATS System");
		fileMenuClose.setActionCommand("Close");
		fileMenuClose.addActionListener(controller);
		fileMenu.add(fileMenuClose);
		fileMenuClose.setEnabled(false);
		
		//Exit the MATS application
		fileMenuExit = new JMenuItem("Exit");
		fileMenuExit.setActionCommand("Exit");
		fileMenuExit.addActionListener(controller);
		fileMenu.add(fileMenuExit);
		fileMenuExit.setEnabled(true);
		
	}
	
	/**
	 * @author asaini, elee3
	 * @param menuBar - The menubar which we want to add menus to.
	 * 
	 * Adds the edit menu onto the menu bar, and adds menu items onto the edit menu.
	 */
	private void addEditMenuBar(JMenuBar menuBar) {
		
		editMenu = new JMenu ("Edit");
		menuBar.add(editMenu);
		
		// Refresh the state of all tasks, to check if any ongoing tasks have gone past the deadline.
		editMenuRefresh = new JMenuItem("Refresh All Tasks");
		editMenuRefresh.setActionCommand("refresh");
		editMenuRefresh.addActionListener(controller);
		editMenu.add(editMenuRefresh);
		editMenuRefresh.setEnabled(false);
	}

	/**
	 * @author asaini, elee3
	 * @param menuBar - The menubar which we want to add menus to.
	 * 
	 * Adds the project menu onto the menu bar, and adds menu items onto the project menu.
	 */
	private void addProjectMenuBar(JMenuBar menuBar) {

		projectMenu = new JMenu ("Project");
		menuBar.add(projectMenu);

		// Create a new project
		projectMenuCreateSubproject = new JMenuItem("Create New Project");
		projectMenuCreateSubproject.setActionCommand("new project");
		projectMenuCreateSubproject.addActionListener(controller);
		projectMenu.add(projectMenuCreateSubproject);
		projectMenuCreateSubproject.setEnabled(false);
		
		// Create a new Task
		projectMenuCreateTask = new JMenuItem("Create New Task");
		projectMenuCreateTask.setActionCommand("new task");
		projectMenuCreateTask.addActionListener(controller);
		projectMenu.add(projectMenuCreateTask);
		projectMenuCreateTask.setEnabled(false);
		
		// Move a subproject into another directory along with everything inside the subproject
		projectMenuMoveSubproject = new JMenuItem("Move Project");
		projectMenuMoveSubproject.setActionCommand("move project");
		projectMenuMoveSubproject.addActionListener(controller);
		projectMenu.add(projectMenuMoveSubproject);
		projectMenuMoveSubproject.setEnabled(false);
		
		// Display all the tasks that the specified user currently owns.
		projectMenuDisplayTasksOwned = new JMenuItem("Display Tasks Owned");
		projectMenuDisplayTasksOwned.setActionCommand("display_tasks_owned");
		projectMenuDisplayTasksOwned.addActionListener(controller);
		projectMenu.add(projectMenuDisplayTasksOwned);
		projectMenuDisplayTasksOwned.setEnabled(false);
		
		// Display all the tasks that the specified user has created.
		projectMenuDisplayTasksCreated = new JMenuItem("Display Tasks Created");
		projectMenuDisplayTasksCreated.setActionCommand("display_tasks_created");
		projectMenuDisplayTasksCreated.addActionListener(controller);
		projectMenu.add(projectMenuDisplayTasksCreated);
		projectMenuDisplayTasksCreated.setEnabled(false);
		
		// Attempt to change the current state of a task
		projectMenuChangeTaskState = new JMenuItem("Change Task State");
		projectMenuChangeTaskState.setActionCommand("change_task_state");
		projectMenuChangeTaskState.addActionListener(controller);
		projectMenu.add(projectMenuChangeTaskState);
		projectMenuChangeTaskState.setEnabled(false);
		
		// Change the specified task's deadline date
		projectMenuChangeTaskDeadline = new JMenuItem("Change Task Deadline");
		projectMenuChangeTaskDeadline.setActionCommand("change_task_deadline");
		projectMenuChangeTaskDeadline.addActionListener(controller);
		projectMenu.add(projectMenuChangeTaskDeadline);
		projectMenuChangeTaskDeadline.setEnabled(false);
		
		// Change the owner of a specified task
		projectMenuChangeTaskOwner= new JMenuItem("Change Task Owner");
		projectMenuChangeTaskOwner.setActionCommand("change_task_owner");
		projectMenuChangeTaskOwner.addActionListener(controller);
		projectMenu.add(projectMenuChangeTaskOwner);
		projectMenuChangeTaskOwner.setEnabled(false);
	}

	/**
	 * @author asaini, elee3
	 * @param menuBar - The menubar which we want to add menus to.
	 * 
	 * Adds the user menu onto the menu bar, and adds menu items onto the user menu.
	 */
	private void addUserMenuBar(JMenuBar menuBar) {
		
		usersMenu = new JMenu ("User");
		menuBar.add(usersMenu);
		
		// A new user registers into the database and logs in
		usersMenuAddNewUser = new JMenuItem("Create New User and Login");
		usersMenuAddNewUser.setActionCommand("new user");
		usersMenuAddNewUser.addActionListener(controller);
		usersMenu.add(usersMenuAddNewUser);
		usersMenuAddNewUser.setEnabled(false);
		
		// Log in as a user already in the database
		usersMenuLogin = new JMenuItem("Login as existing user");
		usersMenuLogin.setActionCommand("log in");
		usersMenuLogin.addActionListener(controller);
		usersMenu.add(usersMenuLogin);
		usersMenuLogin.setEnabled(false);
		
		// Log out of the MATS model
		usersMenuLogout = new JMenuItem("Log out");
		usersMenuLogout.setActionCommand("log out");
		usersMenuLogout.addActionListener(controller);
		usersMenu.add(usersMenuLogout);
		usersMenuLogout.setEnabled(false);
		
		// Changes the password of the currently logged in user
		usersMenuEdit = new JMenuItem("Change password");
		usersMenuEdit.setActionCommand("Change password");
		usersMenuEdit.addActionListener(controller);
		usersMenu.add(usersMenuEdit);
		usersMenuEdit.setEnabled(false);
	}
	
	/**
	 * @author asaini, elee3
	 * 
	 * Closes the current MATS model window.
	 */
	public void closeModelWindow() {
		unsubscribeFromModel();
		writeToProjectTextArea("");
		writeToTaskInfoTextArea("");

		usersList.setModel(new DefaultListModel());
		usersList.setFixedCellWidth(10);
	
		// root node for which all nodes fall under
        TreeNode rootNode = new TreeNode("Project Collection");
        
        // Create a tree that allows one selection at a time.
        tree = new JTree(rootNode);
		
        // place the tree into a scroll pane
        JScrollPane treePane = new JScrollPane(tree,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // add the scroll pane into the GUI
        splitPane.setLeftComponent(treePane);
        
        enableClosedSystemMenuOptions();
	}
	
	
	/**
	 * @author elee3
	 * @param model - The reference to the MATSModel object we want to subscribe to.
	 * 
	 * Subscribes to the specified model.
	 */
	public void setModel(MATSModel model ) {
		
		usersList.setModel(model.getListModel());
		unsubscribeFromModel();
		this.model = model;
		subscribeToModel();
	}
	
	/**
	 * @author elee3
	 * @return returns true if the model is already subscribed to a model, otherwise returns false
	 * 
	 * Unsubscribes from the model which is currently being subscribed to.
	 */
	private boolean unsubscribeFromModel() {
		if ( model != null ) {
			return model.removeModelListener(this);
		}
		return false;
	}
	
	/**
	 * @author boconno3, asaini, rparames
	 * 
	 * Sets the menu options when a new MATS system has been created.
	 */
	public void enableNewSystemMenuOptions() {
		toggleFileMenuCreate(false);
		toggleFileMenuSave(true);
		toggleFileMenuSaveAsXML(true);
		toggleFileMenuLoad(false);
		toggleFileMenuLoadAsXML(false);
		toggleFileMenuClose(true);
		
		toggleEditMenuRefresh(false);
		
		toggleProjectMenuCreateSubproject(false);
		toggleProjectMenuCreateTask(false);
		toggleProjectMenuDisplayTasksOwned(false);
		toggleProjectMenuChangeTaskState(false);
		toggleProjectMenuDisplayTasksCreated(false);
		toggleProjectMenuChangeTaskDeadline(false);
		
		toggleProjectMenuChangeTaskOwner(false);
		toggleProjectMenuMoveSubproject(false);
		
		toggleEditMenuRefresh(false);
		
		toggleUsersMenuAddNewUser(true);
		toggleUsersMenuLogin(false);
		toggleUsersMenuLogout(false);
	}
	
	/**
	 * @author boconno3, asaini, rparames
	 * 
	 * Sets the menu options when a new MATS system has been created.
	 */
	public void enableImportedSystemMenuOptions() {
		enableNewSystemMenuOptions();
		
		toggleUsersMenuLogin(true);
	}
	
	/**
	 * @author boconno3, asaini, rparames
	 * 
	 * Sets the correct menu options when a MATS system has been closed.
	 */
	public void enableClosedSystemMenuOptions() {
		toggleFileMenuCreate(true);
		toggleFileMenuSave(false);
		toggleFileMenuSaveAsXML(false);
		toggleFileMenuLoad(true);
		toggleFileMenuLoadAsXML(true);
		toggleFileMenuClose(false);
		
		toggleEditMenuRefresh(false);
		
		toggleProjectMenuCreateSubproject(false);
		toggleProjectMenuCreateTask(false);
		toggleProjectMenuDisplayTasksOwned(false);
		toggleProjectMenuChangeTaskState(false);
		toggleProjectMenuDisplayTasksCreated(false);
		toggleProjectMenuChangeTaskDeadline(false);
		
		toggleProjectMenuChangeTaskOwner(false);
		toggleProjectMenuMoveSubproject(false);
		
		toggleEditMenuRefresh(false);
		
		toggleUsersMenuAddNewUser(false);
		toggleUsersMenuLogin(false);
		toggleUsersMenuLogout(false);
	}
	
	/**
	 * @author boconno3, asaini, rparames
	 * 
	 * Sets the correct menu options when a new MATS user has logged into the application.
	 */
	public void enableLoggedInMenuOptions() {
		toggleUsersMenuAddNewUser(false);
		toggleUsersMenuLogin(false);
		toggleUsersMenuLogout(true);
		toggleFileMenuSave(true);
		toggleFileMenuSaveAsXML(true);
		toggleFileMenuLoad(false);
		toggleFileMenuLoadAsXML(false);
		
		toggleProjectMenuCreateSubproject(true);
		toggleProjectMenuCreateTask(true);
		toggleProjectMenuDisplayTasksOwned(true);
		toggleProjectMenuDisplayTasksCreated(true);
		toggleProjectMenuChangeTaskState(true);
		toggleProjectMenuChangeTaskDeadline(true);
		toggleProjectMenuChangeTaskOwner(true);
		toggleProjectMenuMoveSubproject(true);

		
		toggleEditMenuRefresh(true);
	}
	
	/**
	 * @author boconno3, asaini, rparames
	 * 
	 * Sets the correct menu options when a MATS user has logged out of the application.
	 */
	public void enableLoggedOutMenuOptions() {
		toggleUsersMenuAddNewUser(true);
		toggleUsersMenuLogin(true);
		toggleUsersMenuLogout(false);
		toggleFileMenuSave(false);
		toggleFileMenuSaveAsXML(false);
		toggleProjectMenuCreateSubproject(false);
		toggleProjectMenuCreateTask(false);
		toggleProjectMenuDisplayTasksOwned(false);
		toggleProjectMenuDisplayTasksCreated(false);
		toggleProjectMenuChangeTaskState(false);
		toggleProjectMenuChangeTaskDeadline(false);
		toggleProjectMenuChangeTaskOwner(false);
		toggleProjectMenuMoveSubproject(false);
		
		toggleEditMenuRefresh (false);
	}
	

	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleProjectMenuChangeTaskOwner (boolean toggle ) {
		projectMenuChangeTaskOwner.setEnabled(toggle);
	}

	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleProjectMenuChangeTaskDeadline (boolean toggle) {
		projectMenuChangeTaskDeadline.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleProjectMenuChangeTaskState(boolean toggle) {
		projectMenuChangeTaskState.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleFileMenuCreate(boolean toggle) {
		fileMenuCreate.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleProjectMenuDisplayTasksOwned (boolean toggle) {
		projectMenuDisplayTasksOwned.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleProjectMenuDisplayTasksCreated (boolean toggle) {
		projectMenuDisplayTasksCreated.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleFileMenuSave(boolean toggle) {
		fileMenuSave.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleFileMenuLoad(boolean toggle) {
		fileMenuLoad.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleFileMenuClose(boolean toggle) {
		fileMenuClose.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleEditMenuRefresh(boolean toggle) {
		editMenuRefresh.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleProjectMenuCreateSubproject(boolean toggle) {
		projectMenuCreateSubproject.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleProjectMenuCreateTask(boolean toggle) {
		projectMenuCreateTask.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleProjectMenuMoveSubproject(boolean toggle) {
		projectMenuMoveSubproject.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	public void toggleUsersMenuAddNewUser(boolean toggle) {
		usersMenuAddNewUser.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleUsersMenuLogin(boolean toggle) {
		usersMenuLogin.setEnabled(toggle);
	}

	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleUsersMenuLogout(boolean toggle) {
		usersMenuLogout.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleFileMenuSaveAsXML(boolean toggle) {
		fileMenuSaveAsXML.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, asaini, rparames, boconno3
	 * @param toggle - enables/disables a menu option
	 * 
	 * Enables or disables a menu option depending on the boolean value of toggle.
	 */
	private void toggleFileMenuLoadAsXML (boolean toggle) {
		fileMenuLoadAsXML.setEnabled(toggle);
	}
	
	/**
	 * @author elee3, rparames
	 *
	 * Updates the content pane to its actual current state.
	 */
	public void update() {
		getContentPane().invalidate();
		getContentPane().validate();
		projectTextArea.repaint();
		projectTextArea.setVisible(true);
	}
	
	
	
	/**
	 * @author boconno3
	 * 
	 *This method will add this view to the list of observers for the model for which
	 *it has a reference to (can be updated now)
	 */
	private void subscribeToModel() {
		model.addModelListener(this);
	}
	
	
	
	/**
	 * @author boconno3
	 * @param errorMessage - the error message that will be displayed to the user
	 * 
	 * This method will display an error message to the user
	 */
	public void displayError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "ERROR" ,JOptionPane.ERROR_MESSAGE );
	}
	
	/**
	 * @author elee3, rparames
	 * @param message - the message that will be displayed onto the project description text area
	 * 
	 * Writes the project description specified to the appropriate text area.
	 */
	public void writeToProjectTextArea(String message) {
		projectTextArea.setText(message + "\n\n");
	}
	
	/**
	 * @author elee3, rparames
	 * @param message - the message that will be displayed onto the task history text area
	 * 
	 * Writes the complete history of the task specified to the appropriate text area.
	 */
	public void writeToTaskInfoTextArea(String message) {
		taskInfoTextArea.setText(message + "\n\n");
	}
	
	/**
	 * @author elee3, rparames
	 * @param message - the message that will be displayed onto the project description text area
	 * 
	 * Appends the message to the project description text area without removing its previous contents.
	 */
	public void appendToProjectTextArea(String message) {
		
		projectTextArea.append(message + "\n\n");
	}
	
	/**
	 * @author elee3, rparames
	 * @param message - the message that will be displayed onto the task history text area
	 * 
	 * Appends the message to the task history text area without removing its previous contents.
	 */
	public void appendToTaskInfoTextArea(String message) {
		taskInfoTextArea.append(message + "\n\n");
	}
	
	/**
	 * @author elee3, rparames
	 * @param question - The message displayed to the user on the pop-up pane.
	 * @return The response received from the user.
	 * 
	 * Communicates with the user expecting a response.
	 */
	public String showInputDialog(String question) {
		return JOptionPane.showInputDialog(this, question);
	}
	
	/**
	 * @author asaini, boconno3
	 * @param message - The message displayed to the user on the pop-up pane.
	 * 
	 * Sends a message to the user.
	 */
	public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
		
	}
	
	/**
	 * @author asaini, boconno3
	 * @return returns the list of users in the JList.
	 */
	public JList getUsersList() {
		return usersList;
	}
	
	// required by interface ModelListener
	public void newSystemCreated(ModelEvent e) {
		enableNewSystemMenuOptions();
	}
	
	// required by interface ModelListener
	public void importedSystem(ModelEvent e) {
		enableImportedSystemMenuOptions();
	}

	// required by interface ModelListener
	public void loggedIn(ModelEvent e) {
		enableLoggedInMenuOptions();
	}
	
	// required by interface ModelListener
	public void loggedOut(ModelEvent e) {
		enableLoggedOutMenuOptions();
	}
	
	// required by interface ModelListener
	public void newSubprojectCreated(ModelEvent e) {
		updateTree();
	}
	
	// required by interface ModelListener
	public void newTaskCreated(ModelEvent e) {
		updateTree();
	}
	
	// required by interface ModelListener
	public void newUserAdded(ModelEvent e) {
		showMessageDialog(e.getMessage());
	}
	
	// required by interface ModelListener
	public void taskStateEdited(ModelEvent e) {
		updateTree();
		update();
	}
	
	// required by interface ModelListener
	public void error(ModelEvent e) {
		displayError(e.getMessage());
	}

	// required by interface ModelListener
	public void componentDescriptionChanged(ModelEvent e) {
		showMessageDialog(e.getMessage());
		update();
	}

	// required by interface ModelListener
	public void refresh(ModelEvent e) {
		showMessageDialog(e.getMessage());
		update();
	}

	// required by interface ModelListener
	public void taskAssigned(ModelEvent e) {
		showMessageDialog(e.getMessage());
		update();
		
	}

	// required by interface ModelListener
	public void taskOwnerChanged(ModelEvent e) {
		showMessageDialog(e.getMessage());
		update();
	}

	// required by interface ModelListener
	public void taskStateChanged(ModelEvent e) {
		showMessageDialog(e.getMessage());
		update();
	}
	
	public void taskDeadlineChanged(ModelEvent e) {
		showMessageDialog(e.getMessage());
		update();
	}
	
	// required by interface ModelListener
	public void projectMoved(ModelEvent e) {
		showMessageDialog(e.getMessage());
		updateTree();
	}

	/**
	 * @author boconno3
	 * 
	 * Gets the tasks owned by the current user 
	 */
	public void getTasksOwned() {
		String user;
		
		try {
			user = (String)getUsersList().getSelectedValue();
		}
		catch (Exception e) {
			displayError("Nothing has been selected");
			return;
		}
		
		List<String> list = model.getTasksOwned(user);
		
		if (list.size()!= 0) {
			writeToProjectTextArea("The tasks owned by this user are: ");
			appendToProjectTextArea(list.get(0));
		
			for (int i = 1; i< list.size(); i++) {
				appendToProjectTextArea(list.get(i));
			}
		}
		else {
			writeToProjectTextArea("No tasks are currently owned by this user");
		}
		update();
	}
	
	/**
	 * @author boconno3
	 * 
	 * Gets a message to display all the tasks created by the current user 
	 */
	public void getTasksCreated() {
		String user;
		try {
			user = (String)getUsersList().getSelectedValue();
		}
		catch (Exception e) {
			displayError("Nothing has been selected");
			return;
		}
		
		List<String> list = model.getTasksCreated(user);
		if (list.size()!= 0) {
			writeToProjectTextArea("The tasks created by this user are: ");
			appendToProjectTextArea(list.get(0));
		
			for (int i = 1; i< list.size(); i++) {
				appendToProjectTextArea(list.get(i));
			}
		}
		else {
			writeToProjectTextArea("No tasks are currently owned by this user");
		}
		update();
	}
}
