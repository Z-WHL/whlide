package com.windhoverlabs.cfside.ui.tables;

import com.windhoverlabs.cfside.ui.trees.NamedObject;

public interface IConfigListViewer {

	public void addConfig(SingleJsonObject singleObject);
	public void removeConfig(SingleJsonObject singleObject);
	public void updateConfig(NamedObject jsonObj);
	
}
