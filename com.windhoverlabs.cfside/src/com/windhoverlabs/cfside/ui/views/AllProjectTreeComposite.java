package com.windhoverlabs.cfside.ui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class AllProjectTreeComposite extends Composite {
	private List<String> projectList = new ArrayList<String>();
	private String[] projectArray;
	private Map<String, Composite> projectCompositeNames = new HashMap<String, Composite>();
	private StackLayout stackLayout;
	
	public AllProjectTreeComposite(Composite parent, int style) {
		super(parent, style);
		
		IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			
			projectList.add(project.getName());
		}
		stackLayout = new StackLayout();

		projectArray = projectList.parallelStream().toArray(String[]::new);
		int countOfProjects = projectList.size();
		GridData tempGridData = new GridData(SWT.FILL, SWT.FILL, true, true);

		for (int i = 0; i < countOfProjects; ++i) {
			final Composite compoTree = new Composite(this, SWT.NONE);
			SingleProjectTreeViewer comp = new SingleProjectTreeViewer(compoTree, SWT.FILL, projectList.get(i));
			comp.setLayoutData(tempGridData);
			projectCompositeNames.put(projectList.get(i), compoTree);
			System.out.println(projectList.get(i) + " created.");
		}
		
		setLayout(stackLayout);
		stackLayout.topControl = projectCompositeNames.get(projectArray[0]);

	}
	
	public void changeControl(String project) {
		Composite temp = projectCompositeNames.get(project);
		stackLayout.topControl = temp;
		setVisible(true);
		this.layout();
		System.out.println(stackLayout.topControl+"control");
	}

}
