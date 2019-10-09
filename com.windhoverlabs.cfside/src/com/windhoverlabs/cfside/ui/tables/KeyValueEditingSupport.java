package com.windhoverlabs.cfside.ui.tables;

import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.trees.NamedObject;

public class KeyValueEditingSupport extends EditingSupport {
	
	private final TableViewer viewer;
	private final CellEditor editor;
	private final int index;
	
	public KeyValueEditingSupport(TableViewer viewer, int index) {
		super(viewer);
		this.viewer = viewer;
		this.index = index;
		this.editor = new TextCellEditor((Composite)getViewer().getControl());
	}
	
	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}
	
	@Override
	protected boolean canEdit(Object element) {
		return true;
	}
	
	@Override
	protected Object getValue(Object element) {
		KeyValueEntry namedObj = (KeyValueEntry) element;
		return index == 0 ? namedObj.getKey() : namedObj.getValue();
	}
	
	@Override
	protected void setValue(Object element, Object userInputValue) {
		KeyValueEntry namedObj = (KeyValueEntry) element;
		// This is the column editor for label or key.
		// Else it is the column editor for the value.
		ConfigTableEditor cf = (ConfigTableEditor) viewer.getTable().getParent().getParent();
		if (index == 0) {
			String key = namedObj.getKey();
			namedObj.setKey(String.valueOf(userInputValue));
			cf.updateKey(key, namedObj);
		} else {
			namedObj.setValue(String.valueOf(userInputValue));
			cf.updateValue(namedObj);
		}
		viewer.update(namedObj, null);
		cf.reflectChangesOnTree();
	}
}

