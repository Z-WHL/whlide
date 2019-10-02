package com.windhoverlabs.cfside.ui.trees;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class JsonLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex == 0) {
			return ((IJsonTreeNode) element).getName();
		} else {
			return Integer.toString(((IJsonTreeNode) element).getChildren().size());
		}
	}
}
