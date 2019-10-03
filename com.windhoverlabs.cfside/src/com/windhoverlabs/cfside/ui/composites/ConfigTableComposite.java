package com.windhoverlabs.cfside.ui.composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.windhoverlabs.cfside.model.Message;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import java.util.ArrayList;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.layout.FillLayout;

public class ConfigTableComposite extends Composite {


	private TableViewer viewer;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ConfigTableComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		createViewer();
		viewer.refresh();
	}
	
	public void createViewer() {
		
		ArrayList<Message> list = new ArrayList<Message>();
		list.add(new Message(1, "SCH_CMD_MID", "MessageTest1", "CMD", "SCH Ground Commands Message ID"));
		list.add(new Message(2, "SCH_UNUSED_MID", "MessageTest2", "CMD", "SCH MDT Unused Message Message ID"));
		list.add(new Message(3, "SCH_SEND_HK_MID", "MessageTest3", "CMD", "SCH Send Houskeeping Message ID"));
		list.add(new Message(4, "SCH_HK_TLM_MID", "MessageTest4", "TLM", "SCH Houskeeping Telemetry Message ID"));
		list.add(new Message(5, "SCH_DIAG_TLM_MID", "MessageTest5", "TLM", "SCH Diagnostic Telemetry Message ID"));
		
		viewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.NONE);

		viewer.setContentProvider(new ConfigContentProvider());
		viewer.setLabelProvider(new ConfigLabelProvider());
		viewer.setInput(list);
		
		
		
		createColumns(this, viewer);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		
		CellEditor[] editors = new CellEditor[1];
		editors[0] = new TextCellEditor(table);
		viewer.setCellEditors(editors);
						
	}
	public TableViewer getViewer() {
		return this.viewer;
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] properties = { "Miid", "Identifier", "Name", "Type", "Description" };
		int[] bounds = { 50, 150, 150, 50, 300};
		
		TableViewerColumn col = createTableViewerColumn(properties[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Message mes = (Message) element;
				return Integer.toString(mes.getMiid());
			}
		});
		
		col = createTableViewerColumn(properties[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Message mes = (Message) element;
				return mes.getIdentifier();
			}
		});
		
		col = createTableViewerColumn(properties[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Message mes = (Message) element;
				return mes.getName();
			}
		});
		
		col = createTableViewerColumn(properties[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Message mes = (Message) element;
				return mes.getType();
			}
		});
		
		col = createTableViewerColumn(properties[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Message mes = (Message) element;
				return mes.getDescription();
			}
		});
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		return viewerColumn;
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	class ConfigLabelProvider implements ITableLabelProvider {

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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			Message message = (Message) element;
			switch (columnIndex) {
			case 0:
				return Integer.toString(message.getMiid());
			case 1:
				return message.getIdentifier();
			case 2:
				return message.getName();
			case 3: 
				return message.getType();
			case 4:
				return message.getDescription();		
			}
			return null;
		}
		
	}

}
