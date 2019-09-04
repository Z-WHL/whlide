package com.windhoverlabs.e4.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.windhoverlabs.e4.rcp.Activator;
import com.windhoverlabs.e4.rcp.projects.CFSProjectSupport;

import widgets.CFSDialog;

import static org.eclipse.swt.events.SelectionListener.*;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;


public class NewCFSHandler extends AbstractHandler {
	private IWorkbenchWindow window;
	private IWorkbenchPage activePage;
	
	private IProject theProject;
	private IResource theResource;
	private IFile theFile;
	
	private String workspaceName;
	private String projectName;
	private String fileName;
	
	public NewCFSHandler() {
		
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		Shell shell = window.getShell();
		
		CFSDialog dia = new CFSDialog(shell);
		String filename = null;
		if(dia.open() == Window.OK) {
			filename = dia.getcfsXML();
		}
		
		IWorkbenchPage activePage = window.getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();
		
		if(!filename.substring(filename.length() - 4).equals(".xml")) {
			MessageDialog.openError(this.window.getShell(), "Error", "File was not an XML");
		}
		else {
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
		return null;
	}
}
