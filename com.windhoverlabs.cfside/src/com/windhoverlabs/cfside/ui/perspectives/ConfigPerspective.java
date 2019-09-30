package com.windhoverlabs.cfside.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ConfigPerspective implements IPerspectiveFactory {

	private IPageLayout factory;
	
	public ConfigPerspective() {
		super();
	}
	
	@Override
	public void createInitialLayout(IPageLayout factory) {		
		this.factory = factory;
		factory.setEditorAreaVisible(true);
		
		addViews();
		addActionSets();
		addViewShortcuts();
		
	}
	/**
	 * Create Initial views of the UI upon perspective change.
	 */
	private void addViews() {
		IFolderLayout topLeft = 
				factory.createFolder(
						"topLeft",
						IPageLayout.LEFT,
						0.15f,
						factory.getEditorArea());
		
		topLeft.addView("com.windhoverlabs.cfside.ui.views.projectTreeViewer"); 
		
		IFolderLayout bottomLeft =
				factory.createFolder(
						"bottomLeft",
						IPageLayout.BOTTOM,
						0.75f,
						"topLeft");
		
		bottomLeft.addView("com.windhoverlabs.cfside.ui.views.fileMergeView");
		IFolderLayout bottom =
				factory.createFolder(
						"bottom",
						IPageLayout.BOTTOM,
						0.75f,
						factory.getEditorArea());
				
		bottom.addView("com.windhoverlabs.cfside.ui.views.errorViewer");
		
		

	}
	
	private void addActionSets() {
		factory.addActionSet("org.eclipse.debug.ui.launchActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.debug.ui.debugActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.debug.ui.profileActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.jdt.debug.ui.JDTDebugActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.jdt.junit.JUnitActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.team.ui.actionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.team.cvs.ui.CVSActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.ant.ui.actionSet.presentation"); //NON-NLS-1
		factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); //NON-NLS-1
	}
	

	private void addViewShortcuts() {
		factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ccvs.ui.AnnotateView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.pde.ui.DependenciesView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.jdt.junit.ResultView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
		factory.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
	}


}
