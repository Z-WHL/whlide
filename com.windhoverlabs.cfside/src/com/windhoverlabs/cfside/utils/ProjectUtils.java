package com.windhoverlabs.cfside.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/** 
 * 
 * Helper class for projects
 *
 */

public class ProjectUtils {

	/**
	 * Finds and returns the given project for a selected portion
	 * 
	 */
	public static IProject getProjectSelection() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if ( window != null ) {
			if ( isStructuredSelection(window) ) {
				return getProjectFromSelection(window);
			} else {
				return getProjectFromEditor(window);
			}
		}
		return null;
	}
	
	/**
	 * Checks if the current selection is a structured selection
	 * @param window
	 * @return
	 */
	private static boolean isStructuredSelection(IWorkbenchWindow window) {
		ISelection iselection = window.getSelectionService().getSelection();
		if ( iselection.getClass() != IStructuredSelection.class ) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get the project based off of the current active selection.
	 * @param window
	 * @return
	 */
	
	private static IProject getProjectFromSelection(IWorkbenchWindow window) {
		IProject project = null;
		ISelection iselection = window.getSelectionService().getSelection();
		IStructuredSelection selection = (IStructuredSelection) iselection;
		if ( selection == null ) {
			return project;
		} else {
			Object firstElement = selection.getFirstElement();
			if ( firstElement instanceof IResource ) {
				project = ((IResource) firstElement).getProject();
			} else {
				return project;
			}
		}
		return project;
	}
	
	/**
	 * Get the project based off of the current file that is opened by the editor.
	 * @param window
	 * @return
	 */
	private static IProject getProjectFromEditor(IWorkbenchWindow window) {
		IProject project = null;
		IWorkbenchPage activePage = window.getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();
		if ( activeEditor != null ) {
			IEditorInput input = activeEditor.getEditorInput();
			project = input.getAdapter(IProject.class);
			if ( project == null ) {
				IResource resource = input.getAdapter(IResource.class);
				if ( resource != null ) {
					project = resource.getProject();
				}
			}
		}
		return project;
	}
}
