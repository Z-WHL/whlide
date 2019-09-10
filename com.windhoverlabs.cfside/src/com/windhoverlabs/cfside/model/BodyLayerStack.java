package com.windhoverlabs.cfside.model;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;

	public class BodyLayerStack extends AbstractLayerTransform {
		private SelectionLayer selectionLayer;
		
		public BodyLayerStack(IDataProvider dataProvider) {
			DataLayer bodyDataLayer = new DataLayer(dataProvider);
			ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
			ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnReorderLayer);
			selectionLayer = new SelectionLayer (columnHideShowLayer);
			ViewportLayer viewportlayer = new ViewportLayer(selectionLayer);
			setUnderlyingLayer(viewportlayer);
			
		}
		
		public SelectionLayer getSelectionLayer() {
			return selectionLayer;
		}
	}
	
	
	


