package com.windhoverlabs.cfside.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ErrorViewer extends ViewPart {

	public static final String ID = "com.windhoverlabs.cfside.ui.views.errorViewer";

	
	public ErrorViewer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {

		Button button = new Button(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
