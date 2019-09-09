package com.windhoverlabs.cfside.ui.editors;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.Collator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;

import com.windhoverlabs.cfside.core.projects.CFSProjectSupport;
import com.windhoverlabs.cfside.utils.ProjectUtils;
import com.windhoverlabs.cfside.utils.FileUtils;

/**
 * An example showing how to create a multi-page editor.
 * This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class CFSMultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener{

	/** The text editor used in page 0. */
	private TextEditor editor;
	HashMap<String, Group> messageIDWidgets = new HashMap<String, Group>();

	/** The Fields Set in page 1. */
	private Text appNameInput;
	private String appNameInputString;
	private Text pathDirectoryInput;
	private String pathDirectoryInputString;
	
	/** The text widget used in page 2. */
	private StyledText text;
	/**
	 * Creates a multi-page editor example.
	 */
	public CFSMultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	private void createPage0() {
		try {
			editor = new TextEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}
	/**
	 * Creates page 1 of the multi-page editor,
	 * which allows you to change the font used in page 2.
	 */
	private void createPage1() {

		Composite composite = new Composite(getContainer(), SWT.FILL);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 12;

		Label id = new Label(composite, SWT.FILL);
		id.setText("Identifier");
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		id.setLayoutData(gridData);
		
		Label name = new Label(composite, SWT.FILL);
		name.setText("Name");
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		gridData.horizontalAlignment = GridData.FILL;

		name.setLayoutData(gridData);
		
		Label msgid = new Label(composite, SWT.FILL);
		msgid.setText("MsgID");
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = GridData.FILL;

		msgid.setLayoutData(gridData);
		
		Label types = new Label(composite, SWT.FILL);
		types.setText("Type");
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = GridData.FILL;

		types.setLayoutData(gridData);
		
		Label descrip = new Label(composite, SWT.FILL);
		descrip.setText("Description");
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		gridData.horizontalAlignment = GridData.FILL;

		descrip.setLayoutData(gridData);
		
		/*
		
		appNameInput.addModifyListener(e -> {
			Text src = (Text) e.getSource();
			String temp = src.getText();
			appNameInputString = temp;
		});
		
		Label pathDirectory = new Label(composite, SWT.NONE);
		pathDirectory.setText("Some Path");
	
		Text pathDirectoryInput = new Text(composite, SWT.BORDER);
		pathDirectoryInput.setLayoutData(gridData);
		
		pathDirectoryInput.addModifyListener(e -> {
			Text src = (Text) e.getSource();
			String temp = src.getText();
			pathDirectoryInputString = temp;
		});
		*/
		
		Button fontButton = new Button(composite, SWT.NONE);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 2;
		fontButton.setLayoutData(gd);
		fontButton.setText("Set");
		
		fontButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				saveInfo(appNameInputString, pathDirectoryInputString);
				/*
				String data = "";
				data.concat("Application Name : " + appNameInputString);
				data.concat("\r\n");
				data.concat("Path Name : " + pathDirectoryInput.getText());
				data.concat("\r\n");
				
				IContainer con = (IContainer) event;
				IProject project = con.getProject();
				if(project == null) {
				   IResource resource = con.getAdapter(IResource.class);
				   if(resource != null) {
					   project = resource.getProject();
				   }
				}
				
				IFile f = project.getFile("someinfo.txt");
				
				byte[] bytes = data.getBytes();
				InputStream source = new ByteArrayInputStream(bytes);
			    try {
			    	f.appendContents(source, IResource.NONE, null);
			    } catch (CoreException e) {
			    	e.printStackTrace();
			    }
			*/	
			}
		});

		int index = addPage(composite);
		setPageText(index, "Properties");
	}
	/**
	 * Creates page 2 of the multi-page editor,
	 * which shows the sorted text.
	 */
	private void createPage2() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);

		int index = addPage(composite);
		setPageText(index, "Preview");
	}
	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
	}
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 2) {
			saveInfo("hi", "ok");
		}
	}
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(() -> {
				IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
				for (int i = 0; i<pages.length; i++){
					if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
						IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
						pages[i].closeEditor(editorPart,true);
					}
				}
			});
		}
	}
	/**
	 * Saves the information.
	 */
	private void saveInfo(String s1, String s2) {
		StringBuilder data = new StringBuilder("");
		data.append("Application Name : " + s1);
		data.append("\r\n");
		data.append("Path Name : " + s2);
		data.append("\r\n");
		String str = data.toString();
		
		System.out.println(str);
		FileUtils.writeToRoot("someinfo.txt", str);
	}
	
	private Group createGroup(Composite parent, String identifier, String name, String msgId, int type, String desc) {
		Group message = new Group(parent, SWT.BORDER);
		Text id = new Text(message, SWT.BORDER);
		id.setText(identifier);
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 2;
		id.setLayoutData(gridData);
		
		Text nam = new Text(message, SWT.BORDER);
		nam.setText(name);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 4;
		nam.setLayoutData(gridData);
		
		Text msgID = new Text(message, SWT.BORDER);
		msgID.setText(msgId);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 1;
		msgID.setLayoutData(gridData);
		
		Text typ = new Text(message, SWT.BORDER);
		typ.setText(msgId);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 1;
		typ.setLayoutData(gridData);
		
		Text descr = new Text(message, SWT.BORDER);
		descr.setText(name);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalSpan = 4;
		descr.setLayoutData(gridData);
		
		return message;
		
	}
}
