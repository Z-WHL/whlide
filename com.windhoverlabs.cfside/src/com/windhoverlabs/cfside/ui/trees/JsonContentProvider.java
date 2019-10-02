package com.windhoverlabs.cfside.ui.trees;

import org.eclipse.jface.viewers.ITreeContentProvider;

public class JsonContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object parentElement) {
		return getChildren(parentElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return ((IJsonTreeNode) parentElement).getChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return ((IJsonTreeNode) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((IJsonTreeNode) element).hasChildren();
	}

}
