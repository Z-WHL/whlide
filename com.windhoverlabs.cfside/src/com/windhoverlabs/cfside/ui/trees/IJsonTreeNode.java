package com.windhoverlabs.cfside.ui.trees;

import java.util.List;

public interface IJsonTreeNode {
	public String getName();
	public List getChildren();
	public boolean hasChildren();
	public IJsonTreeNode getParent();
	
}
