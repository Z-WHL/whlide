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


public class AddMessageIDDialog extends Dialog {
	private Text messageIDInput;
	public String messageID;
	
	public AddMessageIDDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(2, false);
        layout.marginRight = 5;
        layout.marginLeft = 10;
        container.setLayout(layout);

        Label cfsLabel = new Label(container, SWT.NONE);
        cfsLabel.setText("Add Message ID:");
        
        messageIDInput = new Text(container, SWT.BORDER);
        messageIDInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        
        messageIDInput.addModifyListener(e -> {
        	Text textWidget = (Text) e.getSource();
        	String text = textWidget.getText();
        	messageID = text;
        });

        return container;        
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Add", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 125);
	}
	
	@Override
	protected void okPressed() {
		messageID = messageIDInput.getText();
		super.okPressed();
	}
	
	public String getcfsXML() {
		return messageID;
	}
	
	public void setcfsXML(String xml) {
		this.messageID = xml;
	}
		
}
