package widgets;

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
	private Text cfsXML;
	public String cfsXMLName;
	
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

        Label cfsLabel = new Label(container, SWT.NONE);
        cfsLabel.setText("CFS XML File:");
        
        cfsXML = new Text(container, SWT.BORDER);
        cfsXML.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        
        cfsXML.addModifyListener(e -> {
        	Text textWidget = (Text) e.getSource();
        	String text = textWidget.getText();
        	cfsXMLName = text;
        });

        return container;        
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Add XML", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 125);
	}
	
	@Override
	protected void okPressed() {
		cfsXMLName = cfsXML.getText();
		super.okPressed();
	}
	
	public String getcfsXML() {
		return cfsXMLName;
	}
	
	public void setcfsXML(String xml) {
		this.cfsXMLName = xml;
	}
		
}
