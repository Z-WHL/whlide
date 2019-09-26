package com.windhoverlabs.cfside.ui.views;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;

public class ListFileForPackageTree extends Composite {

	private TreeViewer treeViewer;
	private Tree tree;
	private TreeColumn column0;
	private TreeColumn column1;
	public String projectName;
	
	public ListFileForPackageTree(Composite parent, int style, String name) {	
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
	}
	
	public void refreshViewer() {
		treeViewer.refresh();
	}
	


}
