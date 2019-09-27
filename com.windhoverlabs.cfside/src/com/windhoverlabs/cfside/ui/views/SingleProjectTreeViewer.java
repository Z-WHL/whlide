package com.windhoverlabs.cfside.ui.views;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ViewPart;

public class SingleProjectTreeViewer extends Composite {

	private TreeViewer treeViewer;
	private Tree tree;
	private TreeColumn column0;
	private TreeColumn column1;
	public String projectName;
	
	public SingleProjectTreeViewer(Composite parent, int style, String name) {	
		super(parent, style);
		
		IProject currentProj = null;
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		currentProj = ws.getRoot().getProject(name);
		
		
		String pa = currentProj.getLocation().toString();
		System.out.println(pa);
		projectName = name;
		tree = new Tree(parent, SWT.MULTI);
		tree.setHeaderVisible(true);
		
		column0 = new TreeColumn(tree, SWT.NONE);
		column0.setText("Label");
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
							"File " + tn.getName() + " has been double-clicked on");
				}
			}
			
		});
	}
	
	public void refreshViewer() {
		treeViewer.refresh();
	}
	
	@Override
	public String toString() {
		return projectName;
	}

}
