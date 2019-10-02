package com.windhoverlabs.cfside.ui.views;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

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
		tree.setLayout(new FillLayout());
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
					
					String aa = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
					String path = tn.getFullName().substring(aa.length()+projectName.length()+2);
					IPath iPath = new Path(path);
					IFile fileToOpen = currentProj.getFile(iPath);
					
					BundleContext ctx = FrameworkUtil.getBundle(SingleProjectTreeViewer.class).getBundleContext();
					ServiceReference<EventAdmin> ref = ctx.getServiceReference(EventAdmin.class);
					EventAdmin eventAdmin = ctx.getService(ref);
					Map<String, String> properties = new HashMap<String, String>();
					properties.put("file", tn.getFullName());
					
					Event ev = new Event("viewcommunication/syncEvent", properties);
					eventAdmin.sendEvent(ev);
					
					ev = new Event("viewcommunication/asyncEvent", properties);
					eventAdmin.postEvent(ev);
					
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
	
	public TreeViewer getTreeViewer() {
		return this.treeViewer;
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
