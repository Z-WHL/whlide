package com.windhoverlabs.cfside.ui.tables;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.actions.TreeContextMenuActions;
import com.windhoverlabs.cfside.ui.editors.ModuleConfigEditor;
import com.windhoverlabs.cfside.ui.trees.ConfigTreeViewer;
import com.windhoverlabs.cfside.ui.trees.NamedObject;
import com.windhoverlabs.cfside.utils.CfsConfig;

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

	public void createMenu(Composite current) {
		
	}
	
	
	public void goDoSomeCoolSaving(NamedObject namedObj) {
		System.out.println(namedObj.getPath());
		cfsConfig.save(namedObj);
	}

	public NamedObject updateValue(KeyValueEntry namedObj) {
		String key = namedObj.getKey();
		String value = namedObj.getValue();
		String pathNamedObject = namedObject.getPath();
		NamedObject localNamedObject = cfsConfig.getObject(pathNamedObject, "local", cfsConfig);
		NamedObject fullNamedObject = cfsConfig.getObject(pathNamedObject, "full", cfsConfig);
		// There is no named object in local configurations. Create and save one.
		if (localNamedObject == null) {
			NamedObject newObject = new NamedObject();
			String[] parts = pathNamedObject.split("\\.|\\[|\\]");
			newObject.setPath(pathNamedObject);
			newObject.setName(parts[parts.length - 1]);
			newObject.setOverridden(true);
			JsonObject jsonObject = new JsonObject();
			jsonObject.add(newObject.getName(), new JsonObject());
			jsonObject = jsonObject.get(newObject.getName()).getAsJsonObject();
			jsonObject.addProperty(key, value);
			newObject.setObject(jsonObject);
			
			JsonObject fullObject = (JsonObject) fullNamedObject.getObject();
			fullObject.addProperty(key, value);
			fullNamedObject.setObject(fullObject);
			fullNamedObject.setOverridden(true);
			this.namedObject = fullNamedObject;
			this.currentJsonElement = (JsonElement) fullObject;
			
			cfsConfig.saveLocal(newObject);
			cfsConfig.saveFull(this.namedObject);
			return fullNamedObject;
		} else {
			JsonObject localObject = (JsonObject) localNamedObject.getObject();
			localObject.addProperty(key, value);
			localNamedObject.setObject(localObject);
			
			JsonObject fullObject = (JsonObject) fullNamedObject.getObject();
			fullObject.addProperty(key, value);
			fullNamedObject.setObject(fullObject);
			fullNamedObject.setOverridden(true);
			
			this.namedObject = fullNamedObject;
			this.currentJsonElement = (JsonElement) fullObject;
			
			cfsConfig.saveLocal(localNamedObject);
			cfsConfig.saveFull(this.namedObject);	
			return fullNamedObject;
		}
	}

	public NamedObject updateKey(String oldKey, KeyValueEntry namedObj) {
		String key = namedObj.getKey();
		String value = namedObj.getValue();
		String pathNamedObject = namedObject.getPath();
		NamedObject localNamedObject = cfsConfig.getObject(pathNamedObject, "local", cfsConfig);
		NamedObject fullNamedObject = cfsConfig.getObject(pathNamedObject, "full", cfsConfig);

		// There is no named object in local configurations. Create and save one.
		if (localNamedObject == null) {
			NamedObject newObject = new NamedObject();
			String[] parts = pathNamedObject.split("\\.|\\[|\\]");
			newObject.setPath(pathNamedObject);
			newObject.setName(parts[parts.length - 1]);
			newObject.setOverridden(true);
			JsonObject jsonObject = new JsonObject();
			jsonObject.add(newObject.getName(), new JsonObject());
			jsonObject = jsonObject.get(newObject.getName()).getAsJsonObject();
			jsonObject.addProperty(key, value);
			newObject.setObject(jsonObject);
			
			JsonObject fullObject = (JsonObject) fullNamedObject.getObject();
			fullObject.remove(oldKey);
			fullObject.addProperty(key, value);
			fullNamedObject.setObject(fullObject);
			fullNamedObject.setOverridden(true);
			this.namedObject = fullNamedObject;
			this.currentJsonElement = (JsonElement) fullObject;
			
			this.cfsConfig.saveLocal(newObject);
			this.cfsConfig.saveFull(this.namedObject);
			return fullNamedObject;
		} else {
			JsonObject localObject = (JsonObject) localNamedObject.getObject();
			localObject.remove(oldKey);
			localObject.addProperty(key, value);
			localNamedObject.setObject(localObject);
			
			JsonObject fullObject = (JsonObject) fullNamedObject.getObject();
			fullObject.remove(oldKey);
			fullObject.addProperty(key, value);
			fullNamedObject.setObject(fullObject);
			fullNamedObject.setOverridden(true);
			this.namedObject = fullNamedObject;
			this.currentJsonElement = (JsonElement) fullObject;
			
			this.cfsConfig.saveLocal(localNamedObject);
			this.cfsConfig.saveFull(this.namedObject);
			return fullNamedObject;
		}
	}
	
	public CfsConfig getcfsconfig() {
		return this.cfsConfig;
	}
	
	public void reflectChangesOnTree(NamedObject namedObj, CfsConfig cfsConfig2) {
		ModuleConfigEditor mdf = (ModuleConfigEditor) getParent();
		mdf.refreshTree(namedObj, cfsConfig2);
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
	

	
	public void updateKeyValue(String name, String value, NamedObject parentObject) {
		this.parentObject = parentObject;
		System.out.println("Update Key value" + this.parentObject.getName() + this.parentObject.getPath());
		keyValueTable.setNewPair(name, value, parentObject);
	}
}
