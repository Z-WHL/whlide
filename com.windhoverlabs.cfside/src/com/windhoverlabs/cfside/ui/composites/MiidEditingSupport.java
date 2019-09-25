package com.windhoverlabs.cfside.ui.composites;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.windhoverlabs.cfside.model.Message;

public class MiidEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private CellEditor editor;
	
	public MiidEditingSupport(ColumnViewer viewer) {
		super(viewer);
		this.editor = new TextCellEditor(((TableViewer) viewer).getTable());
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
		return ((Message) element).getMiid();
	}
	
	@Override
	protected void setValue(Object element, Object userInputValue) {
		if ((element instanceof Message) && (userInputValue instanceof String)) {
			String userValue = (String) userInputValue;
			Integer newMiid = Integer.valueOf(userValue);
			((Message) element).setMiid(newMiid);
			getViewer().update(element, null);
			getViewer().refresh();
		}
	}
}
