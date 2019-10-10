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
import com.windhoverlabs.cfside.utils.JsonObjectsUtil;

public class ConfigTableEditor extends Composite {
	
	SashForm sashForm;
	KeyValueTable keyValueTable;
	CommonGroup scrollableGroups;

	CfsConfig cfsConfig;
	NamedObject namedObject;
	NamedObject parentObject = null;
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

	public void updateKeyValue(String name, String value, NamedObject parentObject) {
		this.parentObject = parentObject;
		System.out.println("Update Key value" + this.parentObject.getName() + this.parentObject.getPath());
		keyValueTable.setNewPair(name, value, parentObject);
	}
	
	public boolean isNotPrimitive() {
		return this.parentObject == null;
	}

	public void updateParentObjectKey(String oldKey, KeyValueEntry namedObj) {
		String newKey = namedObj.getKey();
		String value = namedObj.getValue();
		JsonObject parentAsJson = (JsonObject) this.parentObject.getObject();
		parentAsJson.remove(oldKey);
		parentAsJson.addProperty(newKey, value);
		System.out.println(parentAsJson.toString());
		this.parentObject.setObject(parentAsJson);
		cfsConfig.save(parentObject);
	}

	public void updateParentObjectValue(KeyValueEntry namedObj) {
		String value = namedObj.getValue();
		JsonObject parentAsJSon = (JsonObject) this.parentObject.getObject();
		String newPath = this.parentObject.getPath();
		String newName = parentObject.getName();
		NamedObject insert = new NamedObject();
		insert.setName(namedObj.getKey());
		insert.setPath(newPath);
		
		JsonObject empty = new JsonObject();
		JsonObject pointerToEmpty = empty;
		String[] parts = newPath.split("\\.|\\[|\\]");
		
		for (int i = 0; i < parts.length; i++) {
			empty.add(parts[i], new JsonObject());
			empty = empty.get(parts[i]).getAsJsonObject();
		}
		
		empty.addProperty(namedObj.getKey(), value);
		insert.setObject(pointerToEmpty);
		
		if(this.cfsConfig.isOverridden(newPath))
		{
			insert.setOverridden(true);
    	} else {
    		insert.setOverridden(false);
    	}
		cfsConfig.setSingleRecord(insert);	
		keyValueTable.setNewPair(namedObj.getKey(), namedObj.getValue(), this.parentObject);
	}
}
