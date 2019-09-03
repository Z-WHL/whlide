package com.windhoverlabs.e4.rcp.wizards;

import java.net.URI;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

import com.windhoverlabs.e4.rcp.projects.CFSProjectSupport;

public class CFSProjectNewWizard extends Wizard implements INewWizard {
	private static final String CFS_NEW_WIZARD_NAME = "New CFS Project"; //$NON-NLS-1$
	private static final String CFS_NEW_PAGE_NAME_ONE = "CFS Project Wizard"; //$NON-NLS-1$
	private WizardNewProjectCreationPage _pageOne;
	private ISelection selection;
	
	/**
	 * Constructor for CustomProjectNewWizard.
	 */
	public CFSProjectNewWizard() {
		setWindowTitle(CFS_NEW_WIZARD_NAME);
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void addPages() {
		super.addPages();
		_pageOne = new WizardNewProjectCreationPage(CFS_NEW_PAGE_NAME_ONE);
		_pageOne.setTitle(NewWizardMessages.CFSProjectNewWizard_New_CFS_Project);
		_pageOne.setDescription(NewWizardMessages.CFSProjectNewWizard_Description);
		
		addPage(_pageOne);
	}
	
	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean performFinish() {
		String name = _pageOne.getProjectName();
		URI location = null;
		if (!_pageOne.useDefaults()) {
			location = _pageOne.getLocationURI();
		}
		
		CFSProjectSupport.createProject(name, location);
		
		return true;
	}

}
