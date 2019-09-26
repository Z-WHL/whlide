package com.windhoverlabs.cfside.ui.views;

import java.util.List;

public class FileNode extends TreeNode {
	private String name;
	
	public FileNode(ITreeNode parent, String name) {
		super(parent);
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	protected void createChildren(List children) {

	}
	
	public boolean hasChildren() {
		return false;
	}
}
