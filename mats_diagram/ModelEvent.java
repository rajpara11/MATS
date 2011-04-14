package mats_model;

import java.util.EventObject;

/**
 * 
 * @author elee 
 * Allows messages to be sent from Model to View 
 */
public class ModelEvent extends EventObject {

	String message = null;
	
	/**
	 * @author elee
	 * @param source
	 * 
	 * Extends the EventObject Class 4
	 */
	public ModelEvent(Object source) {
		super(source);
	}
	
	/**
	 * 
	 * @param source
	 * @param message
	 * 
	 * Wrap message in EventObject for View  
	 */
	public ModelEvent(Object source, String message ) {
		super(source);
		this.message = message;
	}
	
	/**
	 * 
	 * @returnm The event message 
	 */
	public String getMessage() {
		return message;
	}
}
