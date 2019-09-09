package com.windhoverlabs.cfside.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.windhoverlabs.cfside.utils.ProjectUtils;

/**
 * Helper class to help with files
 * 
 */
public class FileUtils {

	/**
	 * Writes a File to the root folder of the current project.
	 * @param fileName
	 * @param content
	 * @return
	 */
	public static void writeToRoot(String fileName, String content) {
		IProject project = ProjectUtils.getProjectSelection();
		IFile file = project.getFile(fileName);
		
		byte[] data = content.getBytes();
		InputStream source = new ByteArrayInputStream(data);
		if (file.exists()) {
			try {
		    	file.appendContents(source, IResource.NONE, null);
		    } catch (CoreException e) {
		    	e.printStackTrace();
			}
		} else {
			try {
				file.create(source, IResource.NONE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
