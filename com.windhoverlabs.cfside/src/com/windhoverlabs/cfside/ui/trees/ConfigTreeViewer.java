package com.windhoverlabs.cfside.ui.trees;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.editors.ModuleConfigEditor;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class ConfigTreeViewer extends TreeViewer implements ISelectionChangedListener {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	/**
	 * @wbp.parser.entryPoint
	 */
	public ConfigTreeViewer(Composite parent, int style, String jsonPath, CfsConfig cfsConfig) {
		super(parent, style);
		
		NamedObject namedObject = new NamedObject();
		Tree tree = getTree();
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_FOREGROUND));
		toolkit.paintBordersFor(tree);
		

		FontData[] boldFontData = getModifiedFontData(tree.getFont().getFontData(), SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), boldFontData);
		
		setLabelProvider(new JsonLabelProvider(boldFont));
		setContentProvider(new JsonContentProvider(cfsConfig));

		namedObject.setName("ROOT");
		JsonObject module = cfsConfig.getJsonElement(jsonPath).getAsJsonObject();
		namedObject.setObject(module);
		namedObject.setPath(jsonPath);
		setInput(namedObject);
		
		addSelectionChangedListener(this);
	}

	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
		Object selectedNode = thisSelection.getFirstElement();

		NamedObject namedObject = (NamedObject) selectedNode; 
		JsonElement selectedElem = (JsonElement) namedObject.getObject();
		
		System.out.println(namedObject.getName());
		ModuleConfigEditor s = (ModuleConfigEditor) getTree().getParent();
		if (selectedElem.isJsonObject()) {
			s.goUpdate(namedObject.getName(), selectedElem);
			getTree().getParent().layout(true, true);
		}
		
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
	
	private static FontData[] getModifiedFontData(FontData[] originalData, int additionalStyle) {
		FontData[] styleData = new FontData[originalData.length];
		for (int i = 0; i < styleData.length; i++) {
			FontData base = originalData[i];
			styleData[i] = new FontData(base.getName(), base.getHeight(), base.getStyle() | additionalStyle);
		}
		return styleData;
	}
}
