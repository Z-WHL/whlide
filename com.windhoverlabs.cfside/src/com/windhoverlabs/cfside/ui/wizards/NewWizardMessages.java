package com.windhoverlabs.cfside.ui.wizards;

import org.eclipse.osgi.util.NLS;

public class NewWizardMessages extends NLS {
	private static final String BUNDLE_NAME = "com.windhoverlabs.cfside.ui.wizards.messages"; //$NON-NLS-1$
	public static String CFSProjectNewWizard_Description;
	public static String CFSProjectNewWizard_New_CFS_Project;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, NewWizardMessages.class);
	}

	private NewWizardMessages() {
	}
}
