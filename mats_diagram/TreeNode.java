package mats_view;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 
 * @author boconno3, elee, rparames, asaini
 * 
 * Extends clas DefaultMutableTreeNode 
 *
 */
public class TreeNode extends DefaultMutableTreeNode {

	boolean isTask;
	
	/**
	 * Constructor 
	 *
	 */
	public TreeNode() {
		super();
	}
	
	/**
	 * 
	 * @param userObject
	 */
	public TreeNode(Object userObject) {
		super(userObject);
	}
	
	/**
	 * 
	 * @param userObject
	 * @param allowsChildren
	 */
	public TreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}
	
	/**
	 * 
	 * @param userObject
	 * @param allowsChildren
	 * @param isTask
	 */
	public TreeNode(Object userObject, boolean allowsChildren, boolean isTask) {
		super(userObject, allowsChildren);
		this.isTask = isTask;
	}
	
	/**
	 * @return true: if node has no childre
	 */
	public boolean isLeaf() {
		return isTask;
	}
}
