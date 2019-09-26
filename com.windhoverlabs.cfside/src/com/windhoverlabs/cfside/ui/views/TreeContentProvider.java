package com.windhoverlabs.cfside.ui.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object parentElement) {
		return getChildren(parentElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return ((ITreeNode) parentElement).getChildren().toArray();

	}

	@Override
	public Object getParent(Object element) {
		return ((ITreeNode) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((ITreeNode) element).hasChildren();
	}
	
	public void dispose() {
		
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}
