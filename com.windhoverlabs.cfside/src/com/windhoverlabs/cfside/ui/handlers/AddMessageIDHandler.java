package com.windhoverlabs.cfside.ui.handlers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import com.windhoverlabs.cfside.utils.ProjectUtils;
import com.windhoverlabs.cfside.core.projects.CFSProjectSupport;
import com.windhoverlabs.cfside.model.Message;

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
		createConfigFile();
		createExampleMessageConfigFile();
				
		return null;
	}
	
	private void createConfigFile() {
		IFile file = ProjectUtils.getProjectSelection().getFile("/parent/exampleMessageConfig.xml");
		if (!file.exists()) {
			byte[] bytes = "".getBytes();
		    InputStream source = new ByteArrayInputStream(bytes);
		    try {
		    	file.create(source, IResource.NONE, null);
		    } catch (CoreException e) {
		    	e.printStackTrace();
		    }
		}
	}
	
	private void createExampleMessageConfigFile() {
		IProject project = ProjectUtils.getProjectSelection();
		
		IFile f = project.getFile("/parent/exampleMessageConfig.xml");
		int numberOfExamples = 5;
		
		for (int i = 0; i < 5; i++) {
			String temp = Integer.toString(i);		
			String testIdentifier = "TestIdentifier".concat(temp);
			String testName = "TestName".concat(temp);
			String testType = i+1 % 2 == 1 ? "CMD" : "TLM";
			String testDescription = "TestDescription".concat(temp);
			Message messageTemp = new Message(i, testIdentifier, testName, testType, testDescription);
			
			String mess = messageTemp.toString().concat("\r\n");
			byte[] bytes = mess.getBytes();
			InputStream source = new ByteArrayInputStream(bytes);
			try {
				f.appendContents(source, IResource.NONE, null);
				source.close();
			} catch (CoreException | IOException e) {
				e.printStackTrace();
			}
		}
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
			if (!f.exists()) {
				createMessageIDFile();
			}
			byte[] bytes = filename.getBytes();
		    InputStream source = new ByteArrayInputStream(bytes);
		    try {
		    	f.appendContents(source, IResource.NONE, null);
		    } catch (CoreException e) {
		    	e.printStackTrace();
		    }
		}
	}
	
	private void createMessageIDFile() {
		IFile file = ProjectUtils.getProjectSelection().getFile("messageids.txt");

		byte[] bytes = "".getBytes();
	    InputStream source = new ByteArrayInputStream(bytes);
	    try {
	    	file.create(source, IResource.NONE, null);
	    } catch (CoreException e) {
	    	e.printStackTrace();
	    }
	}
}
