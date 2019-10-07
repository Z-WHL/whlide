package com.windhoverlabs.cfside.ui.editors;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.trees.JsonLabelProvider;
import com.windhoverlabs.cfside.ui.trees.JsonContentProvider;
import com.windhoverlabs.cfside.ui.trees.JsonObjectTreeNode;
import com.windhoverlabs.cfside.ui.trees.JsonValueTreeNode;
import com.windhoverlabs.cfside.ui.trees.NamedObject;

public class ConfigTreeViewer extends TreeViewer implements ISelectionChangedListener {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public ConfigTreeViewer(Composite parent, int style, JsonObject moduleConfig) {
		super(parent, style);
		
		NamedObject namedObject = new NamedObject();

		Tree tree = getTree();
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_FOREGROUND));
		toolkit.paintBordersFor(tree);
		setLabelProvider(new JsonLabelProvider());
		setContentProvider(new JsonContentProvider());

		namedObject.setName("ROOT");
		namedObject.setObject(moduleConfig);
		setInput(namedObject);
		
		addSelectionChangedListener(this);
	}


	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
		Object selectedNode = thisSelection.getFirstElement();

		NamedObject namedObject = (NamedObject) selectedNode; 
		JsonElement selectedElem = (JsonElement) namedObject.getObject();
			
		if(selectedElem.isJsonArray()) {
			System.out.println("This is a JSON Array");
		} else if(selectedElem.isJsonObject() ) {
			System.out.println("This is a JSON Object");
		} else if(selectedElem.isJsonNull() ) {
			System.out.println("This is a JSON Null");
		} else if(selectedElem.isJsonPrimitive() ) {
			System.out.println("This is a JSON Primitive");
		} else {
			System.out.println("I don't know what this is.");
		}
		
	}
}
