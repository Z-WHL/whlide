package com.windhoverlabs.cfside.ui.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
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
import com.windhoverlabs.cfside.ui.trees.NamedObject;

public class ScrollableGroups2 extends Composite {
	
	HashMap<String, ArrayList<String>> groupLabels = new HashMap<String, ArrayList<String>>();
	HashMap<String, JsonObject> commonGroups = new HashMap<String, JsonObject>();
	HashMap<String, TableViewer> tableViewerMaps = new HashMap<String, TableViewer>();
	HashMap<String, Table> tableMaps = new HashMap<String, Table>();
	GroupedJsonList jsonList;
	String currentGroup;
	Table jsonTable;
	TableViewer viewer;
	List<String> currentGroupLabels = new ArrayList<String>();
	
	public ScrollableGroups2(Composite scrollableHolder, int style, JsonElement current, String currentConfigName) {
		super(scrollableHolder, style);
		
		doGrouping(currentConfigName, current.getAsJsonObject());
		int countOfTables = commonGroups.size();
		this.currentGroup = currentConfigName;
		createViewer(scrollableHolder, current);
		setLayout(new FillLayout(SWT.HORIZONTAL));	
	}
	
	private void createViewer(Composite parent, JsonElement current) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(new ConfigModelProvider(current).getJsons());
		
	}
	
	public TableViewer getViewer() {
		return viewer;
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer) {
		TableViewerColumn col = createTableViewerColumn("Label", 150, 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((NamedObject) element).getName();
			}
		});
		col.setEditingSupport(new GenericColumnEditingSupport(viewer, 0, "Label"));
		
		if (currentGroupLabels.size() > 0) {
			col = createTableViewerColumn(currentGroupLabels.get(0), 150, 1);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					NamedObject namedObj = (NamedObject) element;
					JsonObject jsonOjbect = (JsonObject) namedObj.getObject();
					for (Map.Entry<String, JsonElement> ent : jsonOjbect.entrySet()) {
						if (!ent.getValue().isJsonObject()) {
							return ent.getValue().getAsString();
						}
					}
					return null;
				}
			});
			col.setEditingSupport(new GenericColumnEditingSupport(viewer, 1, currentGroupLabels.get(0)));
		}
		
		if (currentGroupLabels.size() > 1) {
			for (int j = 1; j < currentGroupLabels.size(); j++) {
				addColumn(col, currentGroupLabels.get(j), 150, j);
			}
		}
	}
	
	private void addColumn(TableViewerColumn col, String title, int bound, final int colNum) {
		col = createTableViewerColumn(title, bound, colNum);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				int tempCounter = 0;
				NamedObject namedObj = (NamedObject) element;
				JsonObject singleJsonObject = (JsonObject) namedObj.getObject();
				for (Map.Entry<String, JsonElement> entry : singleJsonObject.entrySet()) {
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
		col.setEditingSupport(new GenericColumnEditingSupport(viewer, colNum, title));
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNum) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		return viewerColumn;
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
							currentGroupLabels.add(innerElement.getKey());
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
	
	private class JsonTableContentProvider implements IStructuredContentProvider, IConfigListViewer {
		private GroupedJsonList list;
		private TableViewer viewer;
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			this.viewer = (TableViewer) v;
			if (list != null) {
				((GroupedJsonList) list).removeChangeListener(this);
			}
			this.list = (GroupedJsonList) newInput;
			if (list != null) {
				((GroupedJsonList) list).addChangeListener(this);
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
			viewer.add(singleObject);
		}

		@Override
		public void removeConfig(SingleJsonObject singleObject) {
			viewer.remove(singleObject);			
		}

		@Override
		public void updateConfig(SingleJsonObject singleObject) {
			viewer.update(singleObject, null);		
			viewer.refresh();
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
		System.out.println(selectedGroup.toString());
		return selectedGroup;
	}
	
	public GroupedJsonList getJsonList() {
		return this.jsonList;
	}
}
