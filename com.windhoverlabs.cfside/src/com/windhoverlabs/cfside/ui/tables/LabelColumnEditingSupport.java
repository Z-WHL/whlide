package com.windhoverlabs.cfside.ui.tables;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.windhoverlabs.cfside.ui.trees.NamedObject;

public class LabelColumnEditingSupport extends EditingSupport{
	    private final TableViewer viewer;
		private final CellEditor editor;
		
		public LabelColumnEditingSupport(TableViewer viewer) {
		    super(viewer);
		    this.viewer = viewer;
		    this.editor = new TextCellEditor(viewer.getTable());
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
		    return ((NamedObject) element).getName();
		}
		
		@Override
		protected void setValue(Object element, Object userInputValue) {
		  //  ((Person) element).setFirstName(String.valueOf(userInputValue));
		  //  viewer.update(element, null);
		}
}
