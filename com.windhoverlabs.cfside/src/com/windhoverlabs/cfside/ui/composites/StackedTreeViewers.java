package com.windhoverlabs.cfside.ui.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import com.windhoverlabs.cfside.ui.views.SingleProjectTreeViewer;

public class StackedTreeViewers extends Composite {

	private String[] projectArray;
	private SingleProjectTreeViewer[] treeViewers;
	StackLayout layout;
	
	public StackedTreeViewers(Composite parent, int style) {
		super(parent, style);
		
		List<String> projectList = new ArrayList<String>();

		IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			
			projectList.add(project.getName());
		}
		
		projectArray = projectList.parallelStream().toArray(String[]::new);

		//System.out.println(projectList.toString());
		//System.out.println(projectArray);
		
		layout = new StackLayout();
		parent.setLayout(layout);
		
		treeViewers = new SingleProjectTreeViewer[projectList.size()];
		
		for (int i = 0; i < treeViewers.length; ++i) {
			treeViewers[i] = new SingleProjectTreeViewer(parent, SWT.NONE, projectArray[i]);
		}		
	}
	
	public void changeToStack(String project) {
		for (int i = 0; i < projectArray.length; i++) {
			if (treeViewers[i].projectName.equalsIgnoreCase(project)) {
				layout.topControl = treeViewers[i];
				break;
			}
		}
	}

}
