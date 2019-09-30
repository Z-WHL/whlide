package com.windhoverlabs.cfside.ui.views;

import java.util.List;

public interface ITreeNode {
	public String getName();
	
	public String getFullName();
	
	public String getRelativeName();
	
	public List getChildren();
	
	public boolean hasChildren();
	
	public ITreeNode getParent();
}
