package com.windhoverlabs.cfside.ui.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.swt.layout.GridData;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;

public class ProjectsTreeViewer extends ViewPart {
	
	public static final String ID = "com.windhoverlabs.cfside.ui.views.projectTreeViewer";

	private List<String> projectList = new ArrayList<String>();
	private String[] projectArray;
	private String currentProjectSelected;
	
	SingleProjectTreeViewer currentProject;
		
	public ProjectsTreeViewer() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		setProjectList();		
		setProjectSelectDropDown(parent);
	}

	private void setProjectSelectDropDown(Composite parent) {
		
		//Drop-Down Composite - Creates
		Combo projectCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridLayout gridLayout = new GridLayout();
		
		parent.setLayout(gridLayout);
		GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		gd.heightHint = 20;
		projectCombo.setLayoutData(gd);

		//Array of Project Tree Viewers are created.
		
		projectCombo.setItems(projectArray);
		//Listener for Project Drop Down
		
		SelectionListener selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Combo combo = ((Combo) event.widget);
				System.out.println(combo.getText() + "was selected");
				if (currentProjectSelected == null) {
					currentProjectSelected = combo.getText();
					createNewTreeViewer(currentProjectSelected, parent);
					//System.out.println("No currentProject");
				} else {
					currentProject.setNewProject(combo.getText());
					//System.out.println(parent.toString());
					//System.out.println(currentProject.toString());
					currentProjectSelected = combo.getText();
					//createNewTreeViewer(currentProjectSelected, parent, false);
					//System.out.println("currentProject");
				}				
			}
		};		
		//Listener and Items are added.
		projectCombo.addSelectionListener(selectionListener);	
	}
		
	private void createNewTreeViewer(String project, Composite parent) {
		
			SingleProjectTreeViewer newTreeViewer = new SingleProjectTreeViewer(parent, SWT.FILL, project);
			this.currentProject = newTreeViewer;
			getSite().setSelectionProvider(this.currentProject.getTreeViewer());
			
			currentProject.setLayout(new FillLayout());
			parent.pack();
		    //System.out.println("Tree Was Created)");
	}
	
	private void setProjectList() {
		IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {	
			projectList.add(project.getName());
		}
		projectArray = projectList.parallelStream().toArray(String[]::new);
		//System.out.println(projectList.toString());
		//System.out.println(projectArray);

	}
	
	@Override
	public void setFocus() {
		
	}
}
