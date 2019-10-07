package com.windhoverlabs.cfside.ui.tables;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class KeyValueTable extends Composite {

	private TableViewer tableViewer;
	private ArrayList<KeyValueEntry> keyValueEntries = new ArrayList<KeyValueEntry>();
	
	public KeyValueTable(Composite parent, int style, JsonElement jsonObject) {
		super(parent, style);
		createTable(jsonObject, parent);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		tableViewer.refresh();
	}
	
	public void createTable(JsonElement jsonEle, Composite parent) {
		JsonObject currentObject = jsonEle.getAsJsonObject();
		for (Map.Entry<String, JsonElement> entry : currentObject.entrySet()) {
			if (!entry.getValue().isJsonObject()) {
				keyValueEntries.add(new KeyValueEntry(entry.getKey(), entry.getValue().getAsString()));
				System.out.println(entry.toString());
			}
		}
		
		System.out.println(keyValueEntries.toString());
		
		tableViewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(new KeyValueContentProvider());
		tableViewer.setLabelProvider(new KeyValueLabelProvider());
		tableViewer.setInput(keyValueEntries);
		
		createColumns(this, tableViewer);
		
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
	}
	
	public void setNewConfig(JsonElement changeJson) {
		keyValueEntries = new ArrayList<KeyValueEntry>();
		JsonObject asJsonObject = changeJson.getAsJsonObject();
		
		for (Map.Entry<String, JsonElement> entry : asJsonObject.entrySet()) {
			if (!entry.getValue().isJsonObject()) {
				keyValueEntries.add(new KeyValueEntry(entry.getKey(), entry.getValue().getAsString()));
				System.out.println(entry.toString());
			}
		}
		
		tableViewer.setInput(keyValueEntries);
		tableViewer.refresh();
	}
	
	public TableViewer getViewer() {
		return this.tableViewer;
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] properties = { "Key", "Value" };
		
		TableViewerColumn col = createTableViewerColumn(properties[0], 100, 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				KeyValueEntry entry = (KeyValueEntry) element;
				return entry.getKey();
			}
		});
		
		col = createTableViewerColumn(properties[1], 100, 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				KeyValueEntry entry = (KeyValueEntry) element;
				return entry.getValue();
			}
		});
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNum) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);;
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		return viewerColumn;
	}
	
	private class KeyValueEntry {
		
		private String key;
		private String value;
		
		public KeyValueEntry(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return this.key;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	private class KeyValueContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			ArrayList<KeyValueEntry> list = (ArrayList<KeyValueEntry>) inputElement;
			return list.toArray();
		}
	}
	
	private class KeyValueLabelProvider implements ITableLabelProvider {

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
			KeyValueEntry entry = (KeyValueEntry) element;
			switch (columnIndex) {
			case 0:
				return entry.key;
			case 1:
				return entry.value;
			}
			return null;
		}
		
	}
	
}
