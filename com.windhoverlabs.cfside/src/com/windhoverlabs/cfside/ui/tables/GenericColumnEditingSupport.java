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

public class GenericColumnEditingSupport extends EditingSupport {
	
	private final TableViewer viewer;
	private final CellEditor editor;
	private final int index;
	private final String property;
	
	public GenericColumnEditingSupport(TableViewer viewer, int index, String property) {
		super(viewer);
		this.viewer = viewer;
		this.index = index;
		this.property = property;
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
		Object result = null;
		NamedObject namedObj = (NamedObject) element;
		
			int counter = 0;
			JsonObject jsonObject = ((JsonObject) namedObj.getObject());
			for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

				if (!entry.getValue().isJsonObject()) {
					if (counter != index) {
						counter++;
					} else {
						result = entry.getValue().getAsString();
						break;
					}
				}
			}
		System.out.println(namedObj.getPath());
		return result;
	}
	
	@Override
	protected void setValue(Object element, Object userInputValue) {
		NamedObject namedObj = (NamedObject) element;
		JsonElement je = (JsonElement) namedObj.getObject();
		JsonObject jsonObject = jsonObject = je.getAsJsonObject();

		if (jsonObject != null) {
			jsonObject.addProperty(property, String.valueOf(userInputValue));
			namedObj.setObject(jsonObject);
			ConfigTableEditor cf = (ConfigTableEditor) viewer.getTable().getParent().getParent();
			cf.goDoSomeCoolSaving(namedObj);
			System.out.println(namedObj.getPath() + " has been changed");
			viewer.update(namedObj, null);
			viewer.refresh();
		}
	}
}
