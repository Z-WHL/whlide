package com.windhoverlabs.cfside.ui.editors;


import java.io.File;
import java.util.HashMap;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.windhoverlabs.cfside.ui.composites.ConfigTableComposite;
import com.windhoverlabs.cfside.ui.trees.ConfigComposite;
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

	/** The text widget used in page 2. */
	String pathName;
	
	ISelectionListener selectionListener;
	
	/**
	 * Creates a multi-page editor example.
	 */
	public CFSMultiPageEditor() {
		super();
		
	}
	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	private void createPage0() {
		try {
			editor = new TextEditor();
			int index = addPage(editor, getEditorInput());
			
			IEditorInput editorInput = getEditorInput();
			IPathEditorInput path = (IPathEditorInput) editorInput;
			File f = new File(path.getPath().toOSString());
			pathName = f.getAbsolutePath();
			
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
		final Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout fl = new FillLayout(SWT.HORIZONTAL);
		composite.setLayout(fl);
	
		final ConfigComposite body = new ConfigComposite(composite, SWT.NONE, pathName);
		body.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		int index = addPage(composite);
		setPageText(index, "Table1");
	}

	/**
	 * Creates page 2 of the multi-page editor,
	 * which shows the sorted text.
	 */
	private void createPage2() {
		Composite composite = new Composite(getContainer(), SWT.FILL);
		ConfigTableComposite table = new ConfigTableComposite(composite, SWT.FILL);
		FillLayout fl = new FillLayout();
		composite.setLayout(fl);
		
		int index = addPage(composite);
		setPageText(index, "Table2");
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
}