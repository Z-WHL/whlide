package com.windhoverlabs.cfside.ui.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonElement;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.SashForm;

public class ConfigTableEditor extends Composite {
	KeyValueTable keyValueTable;

	ScrollableGroups scrollableGroups;
	
	public ConfigTableEditor(Composite parent, int style, JsonElement je) {
		super(parent, style);
		setLayout(new FillLayout(SWT.VERTICAL));
		
		SashForm sashForm = new SashForm(this, SWT.VERTICAL);
		keyValueTable = new KeyValueTable(sashForm, SWT.FILL, je);
		
		scrollableGroups = new ScrollableGroups(sashForm, SWT.FILL, (JsonElement) je, "Empty");
		sashForm.setWeights(new int[] {1, 1});
	}
}
