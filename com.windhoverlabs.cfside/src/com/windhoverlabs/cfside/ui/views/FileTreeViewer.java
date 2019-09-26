package com.windhoverlabs.cfside.ui.views;

import org.eclipse.swt.widgets.Composite;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;

public class FileTreeViewer extends ViewPart {
	public FileTreeViewer() {
	}
	public static final String ID = "com.windhoverlabs.cfside.ui.views.fileTreeViewer";

		private List<String> projectList = new ArrayList<String>();
		private List<Composite> openedProjects = new ArrayList<Composite>();
		private Map<String, Composite> projectCompositeNames = new HashMap<String, Composite>();
		private Map<Composite, GridData> gridData = new HashMap<Composite, GridData>();
		
		
		private String[] projectArray;
		private String currentProjectSelected;
		private StackedTreeViewers stackofTrees;
	@Override
	public void createPartControl(Composite parent) {
		
		setProjectList();
		setProjectSelectDropDown(parent);

	}

	private void setProjectSelectDropDown(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Combo projectCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gd_projectCombo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_projectCombo.widthHint = 583;
		projectCombo.setLayoutData(gd_projectCombo);
		
		
		SelectionListener selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Combo combo = ((Combo) event.widget);
				System.out.println(combo.getText());
				if (currentProjectSelected != null) {
					if (currentProjectSelected != combo.getText()) {
						//hideProjectTree(currentProjectSelected, stackOfTrees);
						//currentProjectSelected = combo.getText();
						//changeProjectTree(currentProjectSelected, stackOfTrees, stack);
						stackofTrees.changeToStack(combo.getText());;
					}
				} else {
					currentProjectSelected = combo.getText();
					//createNewTreeViewer(currentProjectSelected, stackOfTrees, stack);
				
				}
			}
		};
		
		projectCombo.addSelectionListener(selectionListener);
		projectCombo.setItems(projectArray);
		
		stackofTrees = new StackedTreeViewers(parent, SWT.NONE);
		stackofTrees.setLayoutData(gd_projectCombo);
	}
	
	
	private void changeProjectTree(String project, Composite composite, StackLayout stack) {
		if (projectCompositeNames.containsKey(project)) {
			revealProject(project, composite, stack);
		} else {
			createNewTreeViewer(project, composite, stack);
		}
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
	
	private void createNewTreeViewer(String project, Composite parent, StackLayout stack) {
		Composite newTreeViewer = new ListFileForPackageTree(parent, SWT.NONE, project);
		GridData tempGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		newTreeViewer.setLayoutData(tempGridData);
		
		projectCompositeNames.put(project, newTreeViewer);
		gridData.put(newTreeViewer, tempGridData);
		currentProjectSelected = project;
		parent.pack();
		parent.layout(true);
		
		System.out.println("Tree Was Created)");
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
