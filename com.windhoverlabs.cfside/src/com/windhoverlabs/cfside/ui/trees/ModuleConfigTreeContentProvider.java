package com.windhoverlabs.cfside.ui.trees;

import org.eclipse.jface.viewers.ITreeContentProvider;

public class ModuleConfigTreeContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object parentElement) {
		System.out.print("getElements");
		return getChildren(parentElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		System.out.print("getChildren");
		return ((IJsonTreeNode) parentElement).getChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		System.out.print("getParent");
		return ((IJsonTreeNode) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		System.out.print("hasChildren");
		return ((IJsonTreeNode) element).hasChildren();
	}

}
