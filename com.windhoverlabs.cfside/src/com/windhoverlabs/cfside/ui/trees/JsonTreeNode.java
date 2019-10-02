package com.windhoverlabs.cfside.ui.trees;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

public abstract class JsonTreeNode implements IJsonTreeNode {
	protected IJsonTreeNode parentObject;
	protected List childrenObject;
	
	public JsonTreeNode(IJsonTreeNode parent) {
		this.parentObject = parent;
	}
	
	public boolean hasChildren() {
		return true;
	}
	
	public IJsonTreeNode getParent() {
		return this.parentObject;
	}
	
	public List getChildren() {
		if (childrenObject != null) {
			return childrenObject;
		}
		
		childrenObject = new ArrayList<>();
		createChildren(childrenObject);
		
		return this.childrenObject;
	}

	protected abstract void createChildren(List childrenObject);
}
