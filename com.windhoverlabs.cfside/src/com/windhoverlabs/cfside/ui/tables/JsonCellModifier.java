package com.windhoverlabs.cfside.ui.tables;

import java.util.Map;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.trees.NamedObject;

public class JsonCellModifier {
/**
	private CommonGroup tableViewerInstance;
	private String[] columnNames;
	
	public JsonCellModifier(CommonGroup tableViewerInstance) {
		super();
		this.tableViewerInstance = tableViewerInstance;
	}
	
	// This is where we can read from a schema and determine what can be altered.
	// For now, assume everything can be modified.
	public boolean canModify(Object element, String property) {
		return true;
	}
	
	public Object getValue(Object element, String property) {
	//	int columnIndex = tableViewerInstance.getColumnNames().indexOf(property);
		Object result = null;
		NamedObject jsonObject = (NamedObject) element;
		if (columnIndex == 0) {
			System.out.println("ColumIndex = 0" + jsonObject.getName());
			result = jsonObject.getName();
		} else {

			int counter = 0;
			JsonObject asJsonObject = (JsonObject) jsonObject.getObject();
			for (Map.Entry<String, JsonElement> entry : asJsonObject.entrySet()) {
				System.out.println("ColumIndex = " + counter + jsonObject.getName());

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
		NamedObject jsonObj = (NamedObject) item.getData();
		String valueString;
		
		if (columnIndex == 0) {
			valueString = ((String) value).trim();
			jsonObj.setName(valueString);
		} else {
			int counter = 1;
			JsonObject object = (JsonObject) jsonObj.getObject();
			for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
				if (!entry.getValue().isJsonObject()) {
					if (counter == columnIndex) {
						valueString = entry.getValue().getAsString().trim();
						object.addProperty(entry.getKey(), valueString);
						System.out.println("valueString"+valueString);
						System.out.println("key"+entry.getKey());
						jsonObj.setObject(object);
						break;
					} else {
						counter++;
					}
				}
			}
		}
		
		tableViewerInstance.getJsonList().jsonChanged(jsonObj);
	}**/
}
