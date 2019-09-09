package com.windhoverlabs.cfside.ui.handlers;

import java.io.ByteArrayInputStream;
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

import com.windhoverlabs.cfside.ui.dialogs.AddMessageIDDialog;
import com.windhoverlabs.cfside.ui.dialogs.CFSDialog;
import com.windhoverlabs.cfside.core.projects.CFSProjectSupport;

public class AddMessageIDHandler extends AbstractHandler{
	private IWorkbenchWindow window;
	private IWorkbenchPage activePage;
	
	private IProject theProject;
	private IResource theResource;
	private IFile theFile;
	
	private String workspaceName;
	private String projectName;
	private String fileName;
	
	public AddMessageIDHandler() {
		
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		Shell shell = window.getShell();
		
		AddMessageIDDialog dia = new AddMessageIDDialog(shell);
		String filename = null;
		if(dia.open() == Window.OK) {
			filename = dia.getcfsXML();
		}
		
		
		writeToCFS(window, filename);
		
				
		return null;
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
			
			IFile f = project.getFile("messageids.txt");
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
