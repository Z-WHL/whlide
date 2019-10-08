package com.windhoverlabs.cfside.ui.handlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.windhoverlabs.cfside.ui.dialogs.CFSDialog;
import com.windhoverlabs.cfside.utils.JsonObjectsUtil;
import com.windhoverlabs.cfside.core.projects.CFSProjectSupport;

public class MergeCFSFiles extends AbstractHandler{
	private IWorkbenchWindow window;
	private IWorkbenchPage activePage;
	
	private IProject theProject;
	private IResource theResource;
	private IFile theFile;
	
	private String workspaceName;
	private String projectName;
	private String fileName;
	
	public MergeCFSFiles() {
		
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		Shell shell = window.getShell();
		
		CFSDialog dia = new CFSDialog(shell);
		String pathA = null;
		String pathB = null;
		String pathSaved = null;

		if(dia.open() == Window.OK) {
			pathA = dia.pathOne();
			//pathB = dia.pathTwo();
			//pathSaved = dia.pathSaved();
		} else if (dia.open() == Window.CANCEL) {
			return null;
		}
		
		/**
		if (JsonObjectsUtil.deepMerge(pathA, pathB, pathSaved)) {
			MessageDialog.openInformation(
					window.getShell(),
					"Success",
					"Your file has been merged");
		} else {
			MessageDialog.openInformation(
					window.getShell(),
					"Failure",
					"An error occured. Your files were not merged");
		}
		**/
			
		JsonObjectsUtil.goMerge(new File(pathA));
		return null;
	}
	
	private boolean checkValidity(String str) {
		boolean flag = true;
		
		if ( str.length() <= 4 ) { flag = false; }
		if ( !str.substring(str.length() - 4).equals(".xml")) { flag = false; }
		if ( str.contains(" ")) { flag = false; }
		
		return flag;
	}
	
	private void writeToCFS(IWorkbenchWindow window, String filename) {

		IWorkbenchPage activePage = window.getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();
		if(activeEditor != null) {
			IEditorInput input = activeEditor.getEditorInput();
			IProject project = input.getAdapter(IProject.class);
			if(project == null) {
				IResource resource = input.getAdapter(IResource.class);
				if(resource != null) {
					project = resource.getProject();
				}
			}
			
			IFile f = project.getFile("cfsXML.txt");
			filename = filename.concat("\r\n");
			
			byte[] bytes = filename.getBytes();
		    InputStream source = new ByteArrayInputStream(bytes);
		    try {
		    	f.appendContents(source, IResource.NONE, null);
		    } catch (CoreException e) {
		    	e.printStackTrace();
		    }
		}
	}
}
