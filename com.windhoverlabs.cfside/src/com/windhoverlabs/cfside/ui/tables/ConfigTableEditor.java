package com.windhoverlabs.cfside.ui.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.editors.ModuleConfigEditor;
import com.windhoverlabs.cfside.ui.trees.NamedObject;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class ConfigTableEditor extends Composite {
	
	SashForm sashForm;
	KeyValueTable keyValueTable;
	ScrollableGroups2 scrollableGroups;
	CfsConfig cfsConfig;
	NamedObject namedObject;
	
	public ConfigTableEditor(Composite parent, int style, JsonElement jsonElement, NamedObject nameObj, CfsConfig cfsConfig) {
		super(parent, style);
		setLayout(new FillLayout(SWT.VERTICAL));
		this.cfsConfig = cfsConfig;
		sashForm = new SashForm(this, SWT.VERTICAL);
		keyValueTable = new KeyValueTable(sashForm, SWT.FILL, jsonElement);
		scrollableGroups = new ScrollableGroups2(sashForm, SWT.FILL, jsonElement, "Empty", nameObj);
	}
	

	public void goDoSomeCoolSaving(NamedObject namedObj) {
		System.out.println("I HAVE SVAED I HAVE SAVED SAVED SAVED SAVED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		//JsonObject js = (JsonObject) namedObj.getObject();
		System.out.println(namedObj.getPath());
		//System.out.println(js);
		cfsConfig.save(namedObj);
		System.out.println("YEAH BABY I HAVE BEEN SAVED!");
	}
	
}
