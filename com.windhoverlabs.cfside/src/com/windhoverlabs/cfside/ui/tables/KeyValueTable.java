package com.windhoverlabs.cfside.ui.tables;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.trees.NamedObject;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class KeyValueTable extends Composite {

	private TableViewer tableViewer;
	private ArrayList<KeyValueEntry> keyValueEntries = new ArrayList<KeyValueEntry>();
	private JsonElement currentJsonElement;
	private NamedObject namedObj;
	private CfsConfig cfsConfig;
	
	public KeyValueTable(Composite parent, int style, JsonElement jsonObject, NamedObject namedObj, CfsConfig cfsConfig) {
		super(parent, style);
		this.currentJsonElement = jsonObject;
		this.namedObj = namedObj;
		this.cfsConfig = cfsConfig;
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
		tableViewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.setContentProvider(new KeyValueContentProvider());
		
		createColumns(this, tableViewer);
		Table table = tableViewer.getTable();

		FontData[] boldFontData = getModifiedFontData(tableViewer.getTable().getFont().getFontData(), SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), boldFontData);
		tableViewer.setLabelProvider(new KeyValueLabelProvider(boldFont, namedObj, cfsConfig));
		tableViewer.setInput(keyValueEntries);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}
	
	public void setNewConfig(JsonElement changeJson) {
		keyValueEntries = new ArrayList<KeyValueEntry>();
	
		if (changeJson.isJsonObject()) {
			JsonObject asJsonObject = changeJson.getAsJsonObject();
			
			for (Map.Entry<String, JsonElement> entry : asJsonObject.entrySet()) {
				if (!entry.getValue().isJsonObject()) {
					keyValueEntries.add(new KeyValueEntry(entry.getKey(), entry.getValue().getAsString()));
					System.out.println(entry.toString());
				}
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
		col.setEditingSupport(new KeyValueEditingSupport(viewer, 0));

		col = createTableViewerColumn(properties[1], 100, 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				KeyValueEntry entry = (KeyValueEntry) element;
				return entry.getValue();
			}
		});
		col.setEditingSupport(new KeyValueEditingSupport(viewer, 1));
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
	
	
	private class KeyValueContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			ArrayList<KeyValueEntry> list = (ArrayList<KeyValueEntry>) inputElement;
			return list.toArray();
		}
	}
	
	private class KeyValueLabelProvider extends StyledCellLabelProvider   {
		
		private final Styler fBoldStyler;
		private final NamedObject namedObj;
		private final CfsConfig cfsConfig;
		
		public KeyValueLabelProvider(final Font boldFont, NamedObject namedObj, CfsConfig cfsConfig) {
			fBoldStyler = new Styler() {
				@Override
				public void applyStyles(TextStyle textStyle) {
					textStyle.font = boldFont;
				}
			};
			this.namedObj = namedObj;
			this.cfsConfig = cfsConfig;
		}
		
		@Override
		public void update(ViewerCell cell) {
			KeyValueEntry keyValue = (KeyValueEntry) cell.getElement();
			NamedObject outNamedObject = prepareNamed(keyValue);
			
        	if (cell.getColumnIndex() == 0) {
        		if (outNamedObject.getOverridden()) {
        			Styler style = fBoldStyler;
        			StyledString styledString = new StyledString(keyValue.getKey(), style);
        			cell.setText(styledString.toString());
        			cell.setStyleRanges(styledString.getStyleRanges());
        		}
        		cell.setText(keyValue.getKey());
			} else {
				if (outNamedObject.getOverridden()) {
        			Styler style = fBoldStyler;
        			StyledString styledString = new StyledString(keyValue.getValue(), style);
        			cell.setText(styledString.toString());
        			cell.setStyleRanges(styledString.getStyleRanges());
        		}
        		cell.setText(keyValue.getValue());
			}
		
			super.update(cell);
		}
		
		private NamedObject prepareNamed(KeyValueEntry keyValue) {
			NamedObject outNamedObject = new NamedObject();
        	outNamedObject.setName(keyValue.getKey());
        	String newPath = "";
        	if(namedObj.getPath() != "") {
        		newPath = namedObj.getPath() + ".";
        	}
        	newPath = newPath + keyValue.getKey();
        	if(this.cfsConfig.isOverridden(newPath)) {
        		outNamedObject.setOverridden(true);
        	} else {
        		outNamedObject.setOverridden(false);
        	}
        	outNamedObject.setPath(newPath);
        	outNamedObject.setObject(keyValue.getValue());
        	return outNamedObject;
		}
		
		@Override
		protected void measure(Event event, Object element) {
			super.measure(event,  element);
		}
	}
	
	private static FontData[] getModifiedFontData(FontData[] originalData, int additionalStyle) {
		FontData[] styleData = new FontData[originalData.length];
		for (int i = 0; i < styleData.length; i++) {
			FontData base = originalData[i];
			styleData[i] = new FontData(base.getName(), base.getHeight(), base.getStyle() | additionalStyle);
		}
		return styleData;
	
	}
}
