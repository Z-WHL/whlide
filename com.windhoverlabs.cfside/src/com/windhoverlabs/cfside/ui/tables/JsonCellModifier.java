package com.windhoverlabs.cfside.ui.tables;

import java.util.Map;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonCellModifier implements ICellModifier {

	private ScrollableGroups tableViewerInstance;
	private String[] columnNames;
	
	public JsonCellModifier(ScrollableGroups tableViewerInstance) {
		super();
		this.tableViewerInstance = tableViewerInstance;
	}
	
	// This is where we can read from a schema and determine what can be altered.
	// For now, assume everything can be modified.
	public boolean canModify(Object element, String property) {
		return true;
	}
	
	public Object getValue(Object element, String property) {
		int columnIndex = tableViewerInstance.getColumnNames().indexOf(property);
		Object result = null;
		SingleJsonObject jsonObject = (SingleJsonObject) element;
		if (columnIndex == 0) {
			result = jsonObject.getJsonObjectKey();
		} else {
			int counter = 1;
			JsonObject asJsonObject = jsonObject.getJsonObject();
			for (Map.Entry<String, JsonElement> entry : asJsonObject.entrySet()) {
				if (!entry.getValue().isJsonObject()) {
					if (counter == columnIndex) {
						result = entry.getValue().getAsString();
						break;
					} else {
						counter++;
					}
				}
			}
		}
		return result;
	}
	
	// Modify the object. This is where validation would be most sensible.
	// Assume no validation.
	public void modify(Object element, String property, Object value) {
		int columnIndex = tableViewerInstance.getColumnNames().indexOf(property);
		System.out.println("columnIndex" + columnIndex);
		TableItem item = (TableItem) element;
		SingleJsonObject jsonObj = (SingleJsonObject) item.getData();
		String valueString;
		
		if (columnIndex == 0) {
			valueString = ((String) value).trim();
			jsonObj.setJsonObjectKey(valueString);
		} else {
			int counter = 1;
			JsonObject object = jsonObj.getJsonObject();
			for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
				if (!entry.getValue().isJsonObject()) {
					if (counter == columnIndex) {
						valueString = entry.getValue().getAsString().trim();
						object.addProperty(entry.getKey(), valueString);
						System.out.println("valueString"+valueString);
						System.out.println("key"+entry.getKey());

						break;
					} else {
						counter++;
					}
				}
			}
		}
		
		tableViewerInstance.getJsonList().jsonChanged(jsonObj);
	}
}
