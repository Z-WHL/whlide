package com.windhoverlabs.cfside.ui.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ScrollableGroups extends Composite {
	
	HashMap<String, ArrayList<String>> groupLabels = new HashMap<String, ArrayList<String>>();
	HashMap<String, JsonObject> commonGroups = new HashMap<String, JsonObject>();
	HashMap<String, TableViewer> tableViewerMaps = new HashMap<String, TableViewer>();
	HashMap<String, Table> tableMaps = new HashMap<String, Table>();
	
	public ScrollableGroups(Composite scrollableHolder, int style, JsonElement current, String currentConfigName) {
		super(scrollableHolder, style);
		doGrouping(currentConfigName, current.getAsJsonObject());
		int countOfTables = commonGroups.size();
		
		setLayout(new FillLayout(SWT.HORIZONTAL));
		createTable(currentConfigName, current.getAsJsonObject());
		/**
		commonGroups.forEach((groupLabel, jsonObject) -> {
			createTable(groupLabel, jsonObject);
		});
		
		tableViewerMaps.forEach((label, viewer) -> {
			viewer.refresh();
		});
		**/
		
	}
	
	private void createTable(String groupLabel, JsonObject currentObject) {
		TableViewer temp = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL |SWT.FULL_SELECTION);
		temp.setContentProvider(new JsonContentProvider());
		temp.setLabelProvider(new JsonLabelProvider());
		temp.setInput(currentObject);
		
		tableViewerMaps.put(groupLabel, temp);
		
		createColumns(this, temp, groupLabel);
		
		Table table = temp.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tableMaps.put(groupLabel, table);
		temp.refresh();
	}
	
	private void createColumns(Composite parent, TableViewer viewer, String groupLabel) {
		ArrayList<String> selectedGroup = groupLabels.get(groupLabel);
		String[] properties = new String[selectedGroup.size()];
		int i = 0;
		for (String objs : selectedGroup) {
			properties[i++] = (String) objs;
		}
		TableViewerColumn col = null;
		
		addFirstColumn(col, "Key", 100, 0, groupLabel);
		
		if (properties.length > 0) {
			col = createTableViewerColumn(properties[0], 150, 1, groupLabel);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					JsonObject entry = (JsonObject) element;
					for (Map.Entry<String, JsonElement> ent : entry.entrySet()) {
						if (!ent.getValue().isJsonObject()) {
							return ent.getValue().getAsString();
						}
					}
					return null;
				}
			});
		}
		
		if (properties.length > 1) {
			for (int j = 1; j < properties.length; j++) {
				addColumn(col, properties[j], 150, j+1, groupLabel);
			}
		}
		
	}
	
	private void addFirstColumn(TableViewerColumn col, String title, int bound, final int colNum, String groupLabel) {
		col = createTableViewerColumn("Key", bound, 0, groupLabel);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				JsonElement entry = (JsonElement) element;
				for (Map.Entry<String, JsonElement> ent : entry.getAsJsonObject().entrySet()) {
					if (ent.getValue().isJsonObject()) {
						if (ent.getValue().equals(entry)) {
							return ent.getKey();
						}
					}
				}
				return null;
			}
		});
	}
		
	private void addColumn(TableViewerColumn col, String title, int bound, final int colNum, String groupLabel) {
		col = createTableViewerColumn(title, bound, colNum, groupLabel);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				int tempCounter = 0;
				JsonObject obj = (JsonObject) element;
				for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
					if (!entry.getValue().isJsonObject()) {
						if (tempCounter != colNum) {
							tempCounter++;
						} else {
							return entry.getValue().getAsString();
						}
					}
				}
				return null;
			}
		});
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNum, String groupLabel) {
		TableViewer temp = tableViewerMaps.get(groupLabel);
		final TableViewerColumn viewerColumn = new TableViewerColumn(temp, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		return viewerColumn;
	}
	
	/**
	 * Function will take the input Json Object and group them based on their common labels.
	 * Stores in a HashMap.
	 * @param inputJson
	 */
	private void doGrouping(String currentConfigName, JsonObject inputJson) {
		groupLabels.put(currentConfigName, new ArrayList<String>());
		// Iterate through the inputJson
		for (Map.Entry<String, JsonElement> entry : inputJson.entrySet()) {
			// If it is not an object, then it is a value and will be showned in the key-value table.
			// If it is, we need to retrieve the key labels of this. only keys that are value not an object though.
			// We will assume all inner groups of an object will have consistent labeling, but is is extensible for
			// inner groups that do not have the same key groups.
			int currentUnique = 1;
			if (entry.getValue().isJsonObject()) {
				// Let's add the keygrouping if it is not in the current.
				
				for (Map.Entry<String, JsonElement> innerElement : entry.getValue().getAsJsonObject().entrySet()) {
					// If it is a label then add it to the property list for the particular type of group.
					// However, we only want to add it if we don't have a mapping, so we must check.
					if (!innerElement.getValue().isJsonObject()) {
						if (canAdd(currentConfigName, innerElement.getKey())) {
							groupLabels.get(currentConfigName).add(innerElement.getKey());
						}
					}
				}
				commonGroups.put(currentConfigName, entry.getValue().getAsJsonObject());
			}
		}
	}

	private boolean canAdd(String currentGroup, String keyElement) {
		ArrayList<String> selectedGroup = groupLabels.get(currentGroup);
		if (!selectedGroup.contains(keyElement)) {
			return true;
		} else {
			return false;
		}
	}
	
	private class JsonContentProvider implements IStructuredContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			ArrayList<JsonObject> list = new ArrayList<JsonObject>();
			JsonObject jo = (JsonObject) inputElement;
			
			for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
				if (entry.getValue().isJsonObject()) {
					list.add(entry.getValue().getAsJsonObject());
				}
			}
			return list.toArray();
		}
	}
	
	private class JsonLabelProvider implements ITableLabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {
			
		}

		@Override
		public void dispose() {
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			JsonObject jsonObject = (JsonObject) element;
			int counter = 0;
			for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				if (!entry.getValue().isJsonObject()) {
					if (counter == columnIndex) {
						return entry.getValue().getAsString();
					} else {
						counter++;
					}
				}
			}
			return null;
		}
	}
}
