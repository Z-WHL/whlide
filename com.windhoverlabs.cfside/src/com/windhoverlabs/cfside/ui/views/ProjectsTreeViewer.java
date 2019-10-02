package com.windhoverlabs.cfside.ui.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.ViewPart;

import com.windhoverlabs.cfside.ui.composites.StackedTreeViewers;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;

public class ProjectsTreeViewer extends ViewPart {
	
	public static final String ID = "com.windhoverlabs.cfside.ui.views.projectTreeViewer";

	private List<String> projectList = new ArrayList<String>();
	private List<Composite> openedProjects = new ArrayList<Composite>();
	private Map<String, SingleProjectTreeViewer> projectCompositeNames = new HashMap<String, SingleProjectTreeViewer>();
	private Map<Composite, GridData> gridData = new HashMap<Composite, GridData>();
	private SingleProjectTreeViewer[] projectComposites;
	
	private String[] projectArray;
	private String currentProjectSelected;
	private StackedTreeViewers stackofTrees;
	
	SingleProjectTreeViewer currentProject;
		
	public ProjectsTreeViewer() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		setProjectList();		
		setProjectSelectDropDown(parent);
	}

	private void setProjectSelectDropDown(Composite parent) {
		
		int countOfProjects = projectList.size();
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
					System.out.println("No currentProject");
				} else {
					currentProject.setNewProject(combo.getText());
					System.out.println(parent.toString());
					System.out.println(currentProject.toString());
					currentProjectSelected = combo.getText();
					//createNewTreeViewer(currentProjectSelected, parent, false);
					System.out.println("currentProject");
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

			
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			currentProject.setLayoutData(gd);
			parent.pack();
		    System.out.println("Tree Was Created)");
	}
	
	private void changeProjectTree(String project, Composite composite) {
		
		SingleProjectTreeViewer temp = projectCompositeNames.get(project);
		StackLayout layout = (StackLayout) composite.getLayout();
		layout.topControl = temp;
		composite.layout();
		/**
		for (int i = 0; i < projectList.size(); ++i) {
			SingleProjectTreeViewer temp = projectCompositeNames.get(project);
			System.out.println(temp.toString());
			if (temp.projectName.equals(project)) {
				System.out.println(temp.projectName);
				StackLayout layout = (StackLayout) composite.getLayout();
				layout.topControl = projectComposites[i];
				composite.layout();
			}
		}
		**/
	}
	
	private void hideProjectTree(String project, Composite parent) {
		if (projectCompositeNames.containsKey(project)) {
			Composite composite = (Composite) projectCompositeNames.get(project);
			GridData gd = (GridData) gridData.get(composite);
			gd.exclude = !gd.exclude;
			composite.setVisible(!gd.exclude);			
			parent.pack();
			parent.layout(true);

		}
	}
	
	private void revealProject(String project, Composite parent, StackLayout stack) {
		if (projectCompositeNames.containsKey(project)) {
			Composite composite = (Composite) projectCompositeNames.get(project);
			GridData gd = (GridData) gridData.get(composite);
	
			parent.pack();
			parent.layout(true);
		}
	}

	private void setProjectList() {
		IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {	
			projectList.add(project.getName());
		}
		projectArray = projectList.parallelStream().toArray(String[]::new);
		System.out.println(projectList.toString());
		System.out.println(projectArray);

	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
