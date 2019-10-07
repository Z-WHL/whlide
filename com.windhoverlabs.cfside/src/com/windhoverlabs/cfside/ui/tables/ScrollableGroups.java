package com.windhoverlabs.cfside.ui.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ScrollableGroups extends Composite {
	
	HashMap<String, ArrayList<String>> groupLabels = new HashMap<String, ArrayList<String>>();
	HashMap<String, JsonObject> commonGroups = new HashMap<String, JsonObject>();
	HashMap<String, TableViewer> tableViewerMaps = new HashMap<String, TableViewer>();
	HashMap<String, Table> tableMaps = new HashMap<String, Table>();
	GroupedJsonList jsonList;
	String currentGroup;
	Table jsonTable;
	TableViewer tableViewer;
	
	public ScrollableGroups(Composite scrollableHolder, int style, JsonElement current, String currentConfigName) {
		super(scrollableHolder, style);
		
		doGrouping(currentConfigName, current.getAsJsonObject());
		int countOfTables = commonGroups.size();
		this.currentGroup = currentConfigName;
		goCreate(scrollableHolder, currentConfigName, current.getAsJsonObject());
		setLayout(new FillLayout(SWT.HORIZONTAL));

		/**
		commonGroups.forEach((groupLabel, jsonObject) -> {
			createTable(groupLabel, jsonObject);
		});
		
		tableViewerMaps.forEach((label, viewer) -> {
			viewer.refresh();
		});
		**/
		
	}
	
	private void goCreate(Composite composite, String groupLabel, JsonObject currentObject) {
		
		createTable(composite, groupLabel, currentObject);
		
		//TableViewer temp = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL |SWT.FULL_SELECTION);
		//tableViewerMaps.put(groupLabel, temp);
		
		createTableViewer();
		/**
		Table table = temp.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		createColumns(this, temp, groupLabel);
	**/
		
		
		tableViewer.setContentProvider(new JsonContentProvider());
		tableViewer.setLabelProvider(new JsonLabelProvider());
		jsonList = new GroupedJsonList(currentObject, groupLabel);
		tableViewer.setInput(jsonList);
		
		tableMaps.put(groupLabel, jsonTable);
		
		tableViewer.refresh();
	}
	
	private void createTable(Composite composite, String groupLabel, JsonObject currentObject) {
		jsonTable = new Table(this, SWT.NONE);
		jsonTable.setHeaderVisible(true);
		jsonTable.setLinesVisible(true);
		
		TableColumn column = new TableColumn(jsonTable, SWT.CENTER, 0);
		column.setText("Label");
		column.setWidth(150);
		
		List<String> columns = getColumnNames();
		for (int i = 1; i < columns.size(); i++) {
			column = new TableColumn(jsonTable, SWT.LEFT, i);
			column.setText(columns.get(i));
			column.setWidth(150);
		}
	}
	
	private void createTableViewer() {
		tableViewer = new TableViewer(jsonTable);
		Object[] obj = getColumnNames().toArray();
		String[] strarray = new String[obj.length];
		for (int i = 0; i < obj.length; i++) {
			strarray[i] = (String) obj[i];
		}
		
		tableViewer.setColumnProperties(strarray);
		
		List<String> columns = getColumnNames();
		//System.out.println(columns.toString());
		CellEditor[] editors = new CellEditor[columns.size()];
		
		// This is also where we can add verify listeners. For now, no verification	
		for (int i = 0; i < columns.size(); i++) {
			TextCellEditor textEditor = new TextCellEditor(jsonTable);
			((Text) textEditor.getControl()).setTextLimit(150);
			editors[i] = textEditor;
		}
		
		tableViewer.setCellEditors(editors);
		JsonCellModifier mod = new JsonCellModifier(this);
		tableViewer.setCellModifier(mod);	
		tableViewerMaps.put(currentGroup, tableViewer);
	}
	
	private ArrayList<SingleJsonObject> createSingleJsonObjectList(JsonObject currentObject) {
		ArrayList<SingleJsonObject> list = new ArrayList<SingleJsonObject>();
		for (Map.Entry<String, JsonElement> entry : currentObject.entrySet()) {
			if (entry.getValue().isJsonObject()) {
				JsonElement tempJe = entry.getValue();
				JsonObject tempJo = tempJe.getAsJsonObject();
				
				SingleJsonObject toAdd = new SingleJsonObject(entry.getKey(), tempJo);
				list.add(toAdd);
			}
		}
		return list;
	}
	
	
	
	private void createColumns(Composite parent, TableViewer viewer, String groupLabel) {
		ArrayList<String> selectedGroup = groupLabels.get(groupLabel);
		String[] properties = new String[selectedGroup.size()];
		int i = 0;
		for (String objs : selectedGroup) {
			properties[i++] = (String) objs;
		}
		TableViewerColumn col = null;
		
		addFirstColumn(col, "Label", 100, 0, groupLabel);
		
		if (properties.length > 0) {
			col = createTableViewerColumn(properties[0], 150, 1, groupLabel);
			//System.out.println(properties.length + "properties length");
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					SingleJsonObject singleJsonObject = (SingleJsonObject) element;
					//System.out.println(singleJsonObject.toString());
					//System.out.println(singleJsonObject.getJsonObject().toString());
					for (Map.Entry<String, JsonElement> ent : singleJsonObject.getJsonObject().entrySet()) {
						if (!ent.getValue().isJsonObject()) {
							return ent.getValue().getAsString();
						}
					}
					return null;
				}
			});
		}
		
		if (properties.length > 1) {
			for (int j = 0; j < properties.length; j++) {
				addColumn(col, properties[j], 150, j+1, groupLabel);
			}
		}
		
	}
	
	private void addFirstColumn(TableViewerColumn col, String title, int bound, final int colNum, String groupLabel) {
		col = createTableViewerColumn("Key", bound, 0, groupLabel);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SingleJsonObject entry = (SingleJsonObject) element;
				return entry.getJsonObjectKey();
			}
		});
	}
		
	private void addColumn(TableViewerColumn col, String title, int bound, final int colNum, String groupLabel) {
		col = createTableViewerColumn(title, bound, colNum, groupLabel);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				int tempCounter = 0;
				SingleJsonObject singleJsonObject = (SingleJsonObject) element;
				for (Map.Entry<String, JsonElement> entry : singleJsonObject.getJsonObject().entrySet()) {
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
	
	private class JsonContentProvider implements IStructuredContentProvider, IConfigListViewer {
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null) {
				((GroupedJsonList) newInput).addChangeListener(this);
			}
			if (oldInput != null) {
				((GroupedJsonList) oldInput).removeChangeListener(this);
			}
		}
		
		public void dispose() {
			jsonList.removeChangeListener(this);
		}
		
		
		@Override
		public Object[] getElements(Object inputElement) {
			GroupedJsonList group = (GroupedJsonList) inputElement;
			return (group.getArray());
		}

		@Override
		public void addConfig(SingleJsonObject singleObject) {
			tableViewer.add(singleObject);
		}

		@Override
		public void removeConfig(SingleJsonObject singleObject) {
			tableViewer.remove(singleObject);			
		}

		@Override
		public void updateConfig(SingleJsonObject singleObject) {
			tableViewer.update(singleObject, null);			
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
			SingleJsonObject singleJsonObject = (SingleJsonObject) element;
			JsonObject jsonObject = singleJsonObject.getJsonObject();
			
			if (columnIndex == 0) {
				return singleJsonObject.getJsonObjectKey();
			} else {
				int counter = 1;
				for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
					if (!entry.getValue().isJsonObject()) {
						if (counter == columnIndex) {
							return entry.getValue().getAsString();
						} else {
							counter++;
						}
					}
				}
			}
			return null;
		}
	}

	public List<String> getColumnNames() {
		ArrayList<String> selectedGroup = groupLabels.get(currentGroup);
		if (!selectedGroup.contains("Label")) {
			selectedGroup.add(0, "Label");
		}
		//System.out.println(selectedGroup.toString());
		return selectedGroup;
	}
	
	public GroupedJsonList getJsonList() {
		return this.jsonList;
	}
}
