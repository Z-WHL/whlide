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

public class ProjectsTreeViewer extends ViewPart {
	public ProjectsTreeViewer() {
	}
	public static final String ID = "com.windhoverlabs.cfside.ui.views.fileTreeViewer";

		private List<String> projectList = new ArrayList<String>();
		private List<Composite> openedProjects = new ArrayList<Composite>();
		private Map<String, SingleProjectTreeViewer> projectCompositeNames = new HashMap<String, SingleProjectTreeViewer>();
		private Map<Composite, GridData> gridData = new HashMap<Composite, GridData>();
		private SingleProjectTreeViewer[] projectComposites;
		
		private String[] projectArray;
		private String currentProjectSelected;
		private StackedTreeViewers stackofTrees;
	@Override
	public void createPartControl(Composite parent) {
		
		setProjectList();
		Composite comp = new Composite(parent, SWT.FILL);
		
		setProjectSelectDropDown(comp);

	}

	private void setProjectSelectDropDown(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		int countOfProjects = projectList.size();
		//Drop-Down Composite - Creates
		Combo projectCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gd_projectCombo = new GridData();
		gd_projectCombo.widthHint = 361;
		projectCombo.setLayoutData(gd_projectCombo);
		
		Button btnNewButton = new Button(parent, SWT.PUSH);
		
		btnNewButton.setText("New Button");
		//Composite holder = new Composite(parent, SWT.NONE);
		final SingleProjectTreeViewer sptv1 = new SingleProjectTreeViewer(parent, SWT.NONE, projectArray[0]);

		Composite allProjectComposite = new AllProjectTreeComposite(parent, SWT.NONE);

		btnNewButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out.println(currentProjectSelected + " was clicked and opened");
				AllProjectTreeComposite comp = (AllProjectTreeComposite) allProjectComposite;
				comp.changeControl(currentProjectSelected);
				comp.layout();
			}
		});

		//Array of Project Tree Viewers are created.
		
		projectCombo.setItems(projectArray);
		//Listener for Project Drop Down
		
		SelectionListener selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Combo combo = ((Combo) event.widget);
				System.out.println(combo.getText() + "was selected");
				currentProjectSelected = combo.getText();
				
			}
		};
		//Listener and Items are added.
		projectCombo.addSelectionListener(selectionListener);
	}
	
	
	private void createNewTreeViewer(String project, Composite parent, StackLayout stack) {
		Composite newTreeViewer = new SingleProjectTreeViewer(parent, SWT.NONE, project);
		GridData tempGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		newTreeViewer.setLayoutData(tempGridData);
		
		//projectCompositeNames.put(project, newTreeViewer);
		gridData.put(newTreeViewer, tempGridData);
		currentProjectSelected = project;
		parent.pack();
		parent.layout(true);
		
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
		/**
		for (int i = 0; i < projectList.size(); i++) {
			String temp = projectList.get(i).toString();
			projectArray[i] = temp;
		}
		**/
		System.out.println(projectList.toString());
		System.out.println(projectArray);

	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}
