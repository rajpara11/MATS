package mats_model;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author boconno3, elee, rparames, asaini 
 *
 */
public class MATSTaskState implements Serializable {

	/**
	 * The task's current state
	 */
	private int currentState;
	
	/**
	 * The task has not yet been completed and has yet to hit the deadline when in the "ONGOING" state.
	 */
	public static final int ONGOING = 0;
	
	/**
	 * The task has been completed by the owner when in this state.
	 */
	public static final int COMPLETED = 2;
	
	/**
	 * The task was deleted by its creator.
	 */
	public static final int DELETED = 3;
	
	/**
	 * The task has passed its deadline and is still not completed in this state.
	 */
	public static final int OVERDUE = 1;
	
	/**	
	 * @author rparames, asaini
	 * 
	 * Description:
	 * A state is created everytime a task is created.  A state cannot exist without a task,
	 * and a task cannot have multiple states.  The task's state is intialized to "ONGOING".
	 */
	public MATSTaskState() {
		
		currentState = ONGOING;
	}
	
	/**
	 * @author rparames, asaini
	 * @return Whether state was changed or not 
	 * Description:
	 * The task's state was changed by the creator.
	 */	
	public boolean changeState(int destination, Date dueDate) {
		
		if (currentState == DELETED) {
		}
		else if (currentState == ONGOING) {
			
			if (destination==OVERDUE){
				if ((dueDate.compareTo(getCurrentDate()) < 0)) {
					currentState = OVERDUE;
					return true;
				}
			}
			if (destination == DELETED) {
				currentState = DELETED;
				return true;
			}
			if (destination == COMPLETED) {
				currentState = COMPLETED;
				return true;
			}
			if (destination == ONGOING) {
				return false;
			}
			return false;
		}
		
		else if (currentState == OVERDUE) {
			if (destination == OVERDUE) {
				return false;
			}
			if (destination == COMPLETED) {
				currentState = COMPLETED;
				return true;
			}
			if (destination == DELETED) {
				currentState = DELETED;
				return true;
			}
			if (destination == ONGOING) {
				return false;
			}
		}
		
		else if (currentState == COMPLETED) {
			return false;
		}
		
		return false;			
	}

	/**
	 * @author rparames, asaini	
	 * 
	 * Description:
	 * @return The current date.
	 */
	public Date getCurrentDate() {
		
		Calendar c = Calendar.getInstance();
		Date cDate =  c.getTime();
		Date currentDate = new Date(cDate.getYear(), cDate.getMonth(), cDate.getDate());
		return currentDate;
		
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * Description:
	 * Returns the current state of the task.
	 */	
	public int getCurrentState() {
		
		return currentState;
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * 
	 * Description:
	 * @return The task's state as a String.
	 */
	public String toString() {
		
		if (currentState == 0) {
			
			return "ONGOING";
		}
		else if (currentState == 1) {
			
			return "OVERDUE";
		}
		else if (currentState == 2) {
			
			return "COMPLETED";
		}
		else if (currentState == 3) {
			
			return "DELETED";
		}
		
		return "";
	}
	
	/**
	 * @author asaini, boconno3
	 * @return The string to be sent to the XML document
	 */
	public String toXML() {
		
		String messageXML = "\r\n<MATSTaskState>";
		messageXML += "\r\n<currentState>" + Integer.toString(currentState) + "</currentState>";
		messageXML += "\r\n</MATSTaskState>";
		return messageXML;
	}
}
