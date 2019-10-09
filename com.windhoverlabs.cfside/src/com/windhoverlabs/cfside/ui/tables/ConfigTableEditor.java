package com.windhoverlabs.cfside.ui.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.windhoverlabs.cfside.ui.editors.ModuleConfigEditor;
import com.windhoverlabs.cfside.ui.trees.NamedObject;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class ConfigTableEditor extends Composite {
	
	SashForm sashForm;
	KeyValueTable keyValueTable;
	CommonGroup scrollableGroups;

	CfsConfig cfsConfig;
	NamedObject namedObject;
	JsonElement currentJsonElement;
	
	public ConfigTableEditor(Composite parent, int style, JsonElement jsonElement, NamedObject nameObj, CfsConfig cfsConfig) {
		super(parent, style);
		setLayout(new FillLayout(SWT.VERTICAL));
		this.cfsConfig = cfsConfig;
		this.currentJsonElement = jsonElement;
		this.namedObject = nameObj;
		keyValueTable = new KeyValueTable(this, SWT.FILL, jsonElement, nameObj, cfsConfig);
		//scrollableGroups = new CommonGroup(sashForm, SWT.FILL, jsonElement, "Empty", nameObj);
	}

	public void goDoSomeCoolSaving(NamedObject namedObj) {
		System.out.println(namedObj.getPath());
		cfsConfig.save(namedObj);
	}

	public void updateValue(KeyValueEntry namedObj) {
		String key = namedObj.getKey();
		JsonObject asJsonObject = currentJsonElement.getAsJsonObject();
		asJsonObject.addProperty(key, namedObj.getValue());
		this.namedObject.setObject(asJsonObject);
		cfsConfig.save(namedObject);
	}
	// Updates the Key label, however due to the nature of deleting and adding, it does not retain the same order.
	public void updateKey(String key, KeyValueEntry namedObj) {
		String newKey = namedObj.getKey();
		String value = namedObj.getValue();
		JsonObject asJsonObject = currentJsonElement.getAsJsonObject();
		asJsonObject.remove(key);
		asJsonObject.add(newKey, new JsonPrimitive(value));
		this.namedObject.setObject(asJsonObject);
		cfsConfig.save(namedObject);
	}
	
	public void reflectChangesOnTree() {
		ModuleConfigEditor mdf = (ModuleConfigEditor) getParent();
		mdf.refreshTree();
	}
}
