package com.windhoverlabs.cfside.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class CFSDialog extends Dialog {
	private Text pathOne;
	private Text pathTwo;
	private Text pathSaved;
		
	public String pathOneName;
	public String pathTwoName;
	public String pathSavedName;

	public CFSDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(2, false);
        layout.marginRight = 5;
        layout.marginLeft = 10;
        container.setLayout(layout);

        Label pathOneLabel = new Label(container, SWT.NONE);
        pathOneLabel.setText("Path for File 1:");
        
        pathOne = new Text(container, SWT.BORDER);
        pathOne.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        
        pathOne.addModifyListener(e -> {
        	Text textWidget = (Text) e.getSource();
        	String text = textWidget.getText();
        	pathOneName = text;
        });
        
        Label pathTwoLabel = new Label(container, SWT.NONE);
        pathTwoLabel.setText("Path for File 2:");
        
        pathTwo = new Text(container, SWT.BORDER);
        pathTwo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        
        pathTwo.addModifyListener(e -> {
        	Text textWidget = (Text) e.getSource();
        	String text = textWidget.getText();
        	pathTwoName = text;
        });
        
        Label pathSavedLabel = new Label(container, SWT.NONE);
        pathSavedLabel.setText("Path for Saved Merge:");
        
        pathSaved = new Text(container, SWT.BORDER);
        pathSaved.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        
        pathSaved.addModifyListener(e -> {
        	Text textWidget = (Text) e.getSource();
        	String text = textWidget.getText();
        	pathSavedName = text;
        });

        return container;        
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Merge", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 200);
	}
	
	@Override
	protected void okPressed() {
		pathOneName = pathOne.getText();
		pathTwoName = pathTwo.getText();
		pathSavedName = pathSaved.getText();
		super.okPressed();
	}
	
	public String pathOne() {
		return pathOneName;
	}
	
	public String pathTwo() {
		return pathTwoName;
	}
	
	public String pathSaved() {
		return pathSavedName;
	}
	
	public void setpathOne(String path) {
		this.pathOneName = path;
	}
	
	public void setpathTwo(String path) {
		this.pathTwoName = path;
	}
	
	public void setpathSaved(String path) {
		this.pathSavedName = path;
	}
		
}
