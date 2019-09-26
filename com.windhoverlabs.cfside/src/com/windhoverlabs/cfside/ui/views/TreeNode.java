package com.windhoverlabs.cfside.ui.views;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeNode implements ITreeNode {
	protected ITreeNode folderParent;
	protected List folderChildren;
	protected String number;
	
	public TreeNode(ITreeNode parent) {
		folderParent = parent;
	}
	
	public boolean hasChildren() {
		return true;
	}
	
	public ITreeNode getParent() {
		return folderParent;
	}
	
	public List getChildren() {
		if (folderChildren != null) {
			return folderChildren;
		}
		
		folderChildren = new ArrayList<>();
		createChildren(folderChildren);
		
		return folderChildren;
	}
	
	protected abstract void createChildren(List folderChildren);
}
