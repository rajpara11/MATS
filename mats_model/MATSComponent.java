package mats_model;

import java.io.Serializable;

/**
 * @author boconno3, elee, rparames, asaini 
 * This is our Abstract Class that can be either a Subproject or Task
 */
public abstract class MATSComponent implements Serializable {
	
	/**
	 * The name of the component.
	 */
	protected String name;
	
	/**
	 * The description of the component.
	 */
	protected String description;
	
	/**
	 * 
	 * @param name -the name of the component
	 * @param description -the description of the component
	 * 
	 * This constructor will take in a name and description string parameter
	 * and create a component using this
	 */
	public MATSComponent(String name, String description) {
		
		this.name = name;
		this.description = description;
	}
	
	/**
	 * @author rparames
	 * @return the name of the component
	 * 
	 * This method will return the name of the component
	 */
	public String getName() {
		
		return name;
	}
	
	/**
	 * @author elee3
	 * @param user - the user trying to set the description
	 * @param description - the description of the component
	 * @return true if the description was changes successfully, false if not
	 * 
	 * This method will set the description of the component
	 */
	public boolean setDescription(MATSUser user, String description) {
		
		this.description = description;
		return true;
	}
	
	/**
	 * @author asaini
	 * @return the description of the component
	 * 
	 * This method will return the description of the component
	 */
	public String getDescription() {
		
		return description;
	}
}