package mats_model;

/**
 * 
 * @author elee 
 * Interface for view 
 */
public interface ModelListener {

	/**
	 * 
	 * @param e - eventObject for all methods from model to view encapsulating a message
	 * 
	 * enforces the view to implement the following methods
	 */
	public void newSystemCreated(ModelEvent e);
	public void importedSystem(ModelEvent e);
	public void loggedIn(ModelEvent e);
	public void loggedOut(ModelEvent e);
	public void newSubprojectCreated(ModelEvent e);
	public void newTaskCreated(ModelEvent e);
	public void newUserAdded(ModelEvent e);
	public void taskStateEdited(ModelEvent e);
	public void error(ModelEvent e);
	public void taskAssigned(ModelEvent e);
	public void refresh(ModelEvent e);
	public void taskStateChanged(ModelEvent e);
	public void taskOwnerChanged(ModelEvent e);
	public void componentDescriptionChanged(ModelEvent e);
	public void projectMoved(ModelEvent e);
	public void taskDeadlineChanged(ModelEvent e);
	

	
}
