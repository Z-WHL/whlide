package com.windhoverlabs.cfside.model;

import org.eclipse.jdt.core.IJavaProject;


/**
 * The ICFSProject Interface serves as an interface to any project that instantiates the CFS Model.
 * @author vagrant
 *
 */
public interface ICFSProject extends IJavaProject {
	/**
	 * Retrieves the properties of the CFS Project
	 * @return the CFS properties
	 */
	public CFSProperties getProperties();
}
