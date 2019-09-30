package com.windhoverlabs.cfside.ui.views;

import java.util.List;

public class FileNode extends TreeNode {
	private String name;
	private String fullName;
	private String relativeName;
	
	public FileNode(ITreeNode parent, String name) {
		super(parent);
		this.name = name;
		this.fullName = parent.getFullName().concat(name);
		this.relativeName = parent.getName().concat("/"+name);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFullName() {
		return fullName;
	}

	public String getRelativeName() {
		return relativeName;
	}

	@Override
	protected void createChildren(List children) {

	}
	
	public boolean hasChildren() {
		return false;
	}
}
