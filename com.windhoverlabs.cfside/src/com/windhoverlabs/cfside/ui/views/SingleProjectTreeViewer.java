package com.windhoverlabs.cfside.ui.views;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.swt.SWT;
import java.nio.file.Paths;
import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;

public class SingleProjectTreeViewer extends Composite {

	private TreeViewer treeViewer;
	private Tree tree;
	private TreeColumn column0;
	private TreeColumn column1;
	public String projectName;
	IProject currentProj;
	
	private IPageLayout factory;

	
	public SingleProjectTreeViewer(Composite parent, int style, String name) {	
		super(parent, style);
			
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		currentProj = ws.getRoot().getProject(name);
				
		String pa = currentProj.getLocation().toString();
		System.out.println(pa);
		projectName = name;
		tree = new Tree(parent, SWT.MULTI);
		tree.setHeaderVisible(true);
		
		column0 = new TreeColumn(tree, SWT.NONE);
		column0.setText("Project : " + projectName);
		column0.setWidth(200);
		
		treeViewer = new TreeViewer(tree);
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setLabelProvider(new TreeLabelProvider());
		treeViewer.setInput(new FolderNode(new File(pa)));
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
				
				Object selectedNode = thisSelection.getFirstElement();
				
				if (selectedNode instanceof TreeNode) {
					TreeNode tn = (TreeNode) selectedNode;
					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

					MessageDialog.openInformation(
							window.getShell(),
							"File",
							"File " + tn.getName() + " has been double-clicked on. Full Path : " + tn.getFullName() + " relative name : " + tn.getRelativeName());
			
					String pathStr = tn.getRelativeName();
					System.out.println(pathStr);
					IPath iPath = new Path(pathStr);
					IFile fileToOpen = currentProj.getFile(iPath);
					/**
					IWorkbench wb = PlatformUI.getWorkbench();
					IWorkbenchWindow ww = wb.getActiveWorkbenchWindow();
					IWorkbenchPage wp = ww.getActivePage();
					
					HashMap map = new HashMap();
					map.put(IMarker.LINE_NUMBER, 1);
					map.put(IWorkbenchPage.EDITOR_ID_ATTR, "com.windhoverlabs.cfside.editors.CFSMultiPageEditorContributor");
					IMarker marker;
					try {
						marker = fileToOpen.createMarker(IMarker.TEXT);
						marker.setAttributes(map);
						IDE.openEditor(wp, marker);

					} catch (CoreException e) {
						e.printStackTrace();
					}
**/
					IWorkbenchPage[] pages = window.getPages();
				
					try {
						IDE.openEditor(pages[0], new FileEditorInput(fileToOpen), "com.windhoverlabs.cfside.editors.CFSMultiPageEditor", true);
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
	}
	
	public void setNewProject(String input) {
		IProject currentProj = null;
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		currentProj = ws.getRoot().getProject(input);
		String pa = currentProj.getLocation().toString();
		this.treeViewer.setInput(new FolderNode(new File(pa)));
		projectName = input;
		this.column0.setText("Project : " + projectName);
		refreshViewer();
	}
	
	public void refreshViewer() {
		treeViewer.refresh();
	}
	
	@Override
	public String toString() {
		return projectName;
	}

}
