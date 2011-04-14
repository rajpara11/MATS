package mats_model;

import java.io.Serializable;
import java.util.*;

/**
 * @author boconno3, elee, rparames, asaini  
 * 
 * Defines the task objects which describe a certain job for the owner and 
 * do not contain any tasks or subprojects within 
 */

public class MATSTask extends MATSComponent {
	
	/**
	 * The user who created this task.
	 */
	private MATSUser creator;
	
	/**
	 * The user who currently owns this task.
	 */
	private MATSUser owner;
	
	/**
	 * The current state of this task (Ongoing, Overdue, Completed, or Deleted).
	 */
	private MATSTaskState currentState;
	
	/**
	 * A chronological history of the task's lifetime.
	 */
	private List<String> history;
	
	/**
	 * The due date for the task's completion.
	 */
	private Date deadline;	

	
	/**
	 * @author rparames
	 * @author asaini
	 * @author elee3
	 * @author boconno3
	 * 
	 * Creates a task, intializing its owner and its state. The task is now ongoing.
	 */
	public MATSTask( MATSUser creator, MATSUser owner, Date deadline, String name, String description) {
		super(name, description);
		this.creator = creator;
		this.owner = owner;		
		this.deadline = deadline;
		currentState = new MATSTaskState();
		history = new ArrayList<String> ();
		writeToHistory("This task was created on this date by " + creator.toString());
		
	}

	
	/**
	 * @author rparames
	 * @author asaini
	 * @author elee3
	 * @author boconno3
	 * 
	 * @param newOwner - instance of the owner that the task should record as the new owner
	 * @param whoAmI - instance of the user trying to make the chance
	 * @return If the owner of the task was changed successfully then return true, otherwise false
	 * 
	 * The creator or the current owner can change the owner of this task, otherwise no change is
	 * made and the method returns false.
	 */
	public boolean changeOwner(MATSUser newOwner, MATSUser whoAmI) {
		
		if ( currentState.getCurrentState() != MATSTaskState.ONGOING ) {
			return false;
		}
		if ( (whoAmI != owner) && (whoAmI != creator) ) {
			return false;
		}
		
		this.owner = newOwner;
		writeToHistory("This task was reassigned on this date to " + newOwner.toString() );
		return true;
	}
	
	
	/**
	 * @author rparames
	 * @author asaini
	 * @author elee3
	 * @author boconno3
	 * @param user - instance of the user trying to make the change
	 * @param descrip - the new description to change to this task
	 * @return If the description was changed successfully then return true, otherwise false
	 * 
	 * The creator of this task is the only one that can change the description of this task.
	 * The method returns false if the user trying to make the change is not the creator.
	 */
	public boolean setDescription (MATSUser user, String descrip) {
		
		if (creator == user) {
			super.setDescription(user, descrip);
			writeToHistory("The description of the task was changed.");
			return true;
			
		}
		return false;
	}
	
	
	/**
	 * @author rparames
	 * @author asaini
	 * @author elee3
	 * @author boconno3
	 * @param user - instance of the user trying to make the change
	 * @param newDate - the new deadline date to change to this task
	 * @return If the deadline was changed successfully then return true, otherwise false
	 * 
	 * The creator of this task in the only one that can change the deadline due date for this task.
	 * The method returns false if the user trying to make the change is not the creator
	 */
	public boolean changeDeadline(MATSUser user, Date newDate) {
		
		if (creator == user) {
			if ((newDate.compareTo(currentState.getCurrentDate())) >0) {
				deadline = newDate;
				writeToHistory("The deadline of the task was postponed to " + deadline.toString());
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
	 * @param user - instance of the user trying to make the change
	 * @param destination - the new state of task to change to this task
	 * @return If the task state was changed successfully then return true, otherwise false
	 * 
	 * The creator of this task in the only one that can change the state of the task.
	 * The method returns false if the user trying to make the change is not the creator.
	 */
	public boolean changeState(int destination, MATSUser user) {
		
		if (creator == user) {
			if (currentState.changeState(destination, deadline)) {
				writeToHistory("The task's state was changed to " + currentState.toString());
				return true;
			}
		}
		return false;
	}
	

	/**
	 * @author rparames
	 * @author asaini
	 * 
	 * Checks if the current state being checked is overdue
	 */
	public void changeIfOverdue() {
		if (getState()==MATSTaskState.ONGOING) {
				if (currentState.changeState(MATSTaskState.OVERDUE, deadline)) {
					writeToHistory("The task's state was changed to " + currentState.toString());
				}
		}
	}
	

	/**
	 * @author rparames
	 * @author asaini
	 * @param message - append the history changes made to this task by this given string
	 * 
	 * This method will append all history changes given by the string called message.
	 */
	public void writeToHistory (String message) {
		history.add(currentState.getCurrentDate().toString() + ": " + message);
	}
	
	/**
	 * @author rparames, asaini
	 * @param The task's history to be set
	 * 
	 * This method sets the history of this task specified by the parameter
	 */
	public void setHistory(List<String> history) {
		this.history = history;
	}
	
	
	/**
	 * @author rparames
	 * @author asaini
	 * @return the name of the task owner in the form of a string type
	 * 
	 * This method will get the name of the owner of this task in the type string
	 */
	public String getOwner() {
		return owner.toString();
	}
	
	
	/**
	 * @author rparames
	 * @author asaini
	 * @return the name of the task creator in the form of a string type
	 * 
	 * This method will get the name of the creator of this task in the type string
	 */
	public String getCreator() {
		return creator.toString();
	}

	
	/**
	 * @author rparames
	 * @author asaini
	 * @return the task deadline in the form of a string type
	 * 
	 * This method will get the due date deadline of this task in the type string
	 */
	public String getDeadline() {
		return deadline.toString();
	}
	
	
	/**
	 * @author rparames
	 * @author asaini
	 * @return the list of history for this task
	 * 
	 * This method will get the history for this task and return as a list
	 */
	public List<String> getHistory() {
		return history;
	}
	
	
	/**
	 * @author rparames
	 * @author asaini
	 * @return the state of the task in the form of an integer
	 * 
	 * This method will get the current state of this task and return as an integer
	 */
	public int getState() {
		return currentState.getCurrentState();
	}
	
	/**
	 * @return A string representation of the MATSTask object.
	 */
	public String toXML() {
		
		String messageXML = "\r\n<MATSTask>";
		messageXML += "\r\n<name>" + name + "</name>";
		messageXML += "\r\n<description>" + description + "</description>";
		messageXML += "\r\n<creator>" + creator.getName() + "</creator>";
		messageXML += "\r\n<owner>" + owner.getName() + "</owner>";
		messageXML += "\r\n<currentState>";
		messageXML += currentState.toXML();
		messageXML += "\r\n</currentState>";
		
		for (String taskEvent : history) {
			
			messageXML += "\r\n<history>" + taskEvent + "</history>";
		}
		
		messageXML += "\r\n<deadline>";
		messageXML += "\r\n<year>" + deadline.getYear() + "</year>";
		messageXML += "\r\n<month>" + deadline.getMonth() + "</month>";
		messageXML += "\r\n<date>" + deadline.getDate() + "</date>";
		messageXML += "</deadline>";
		messageXML += "</MATSTask>";
		
		return messageXML;
	}
}

