package com.windhoverlabs.cfside.ui.views;

import java.io.File;
import java.util.List;

public class FolderNode extends TreeNode {

	private File fFolder;
	
	public FolderNode(File folder) {
		this(null, folder);
		// TODO Auto-generated constructor stub
	}
	
	public FolderNode(ITreeNode parent, File folder) {
		super(parent);
		fFolder = folder;
	}
	
	public String getName() {
		return fFolder.getName();
	}
	
	@Override
	protected void createChildren(List children) {
		File[] childFiles = fFolder.listFiles();
		for (File file : childFiles) {
			if (file.isDirectory()) {
				children.add(new FolderNode(this, file));
			} else {
				children.add(new FileNode(this, file.getName()));
			}
		}
	}
	
}
