package com.windhoverlabs.cfside.ui.dialogs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class CFSDialog extends Dialog {

	private JsonElement jsonElement;
	private String name;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text nameValue;
	
	private HashMap<Text, Text> keyValueWidgets = new HashMap<Text, Text>();
	private HashMap<String, String> keyvalues = new HashMap<String, String>();
	private HashSet<Text> classesWidgets = new HashSet<Text>();
	private Text txtNewText_3;
	
	public CFSDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        ScrolledForm scrldfrmNewScrolledform = formToolkit.createScrolledForm(container);
        formToolkit.paintBordersFor(scrldfrmNewScrolledform);
        scrldfrmNewScrolledform.setText("Add Object");
        scrldfrmNewScrolledform.getBody().setLayout(new GridLayout(3, false));
        
        Label lblNewLabel = formToolkit.createLabel(scrldfrmNewScrolledform.getBody(), "Name", SWT.NONE);
        GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_lblNewLabel.widthHint = 139;
        lblNewLabel.setLayoutData(gd_lblNewLabel); 

        nameValue = formToolkit.createText(scrldfrmNewScrolledform.getBody(), "value", SWT.NONE);
        nameValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(scrldfrmNewScrolledform.getBody(), SWT.NONE);
        
        scrldfrmNewScrolledform.getToolBarManager().add(new Action("Add Field") {
        	public void run() {
                Text newField = formToolkit.createText(scrldfrmNewScrolledform.getBody(), "field", SWT.NONE);
                newField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                
                Text value = formToolkit.createText(scrldfrmNewScrolledform.getBody(), "value", SWT.NONE);
                value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                new Label(scrldfrmNewScrolledform.getBody(), SWT.NONE);
                keyValueWidgets.put(newField, value);
                scrldfrmNewScrolledform.getBody().layout();
        	}
        });
        
        scrldfrmNewScrolledform.getToolBarManager().add(new Action("Add Class") {
        	public void run() {
        		Text newClass = formToolkit.createText(scrldfrmNewScrolledform.getBody(), "New Text", SWT.NONE);
        		newClass.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        		classesWidgets.add(newClass);
                scrldfrmNewScrolledform.getBody().layout();

        	}
        });
        scrldfrmNewScrolledform.updateToolBar();
        return container;        
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Add", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 468);
	}
	
	@Override
	protected void okPressed() {
		JsonObject newJson = new JsonObject();
		for (Map.Entry<Text, Text> entry : keyValueWidgets.entrySet()) {
			newJson.addProperty(entry.getKey().getText(), entry.getValue().getText());
		}
		for (Text entry : classesWidgets) {
			newJson.add(entry.getText(), new JsonObject());
		}
		name = nameValue.getText();
		jsonElement = newJson;
		super.okPressed();
	}
	
	public void setJsonElement(JsonElement jsonElement) {
		this.jsonElement = jsonElement;
	}
	
	public JsonElement getJsonElement() {
		return this.jsonElement;
	}
	
	public void setName(String name) {
		this.name = name;
	}
		
	public String getName() {
		return this.name;
	}
}
