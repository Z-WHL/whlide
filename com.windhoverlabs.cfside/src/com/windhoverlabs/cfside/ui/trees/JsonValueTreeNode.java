package com.windhoverlabs.cfside.ui.trees;

import java.util.List;

import com.google.gson.JsonElement;

public class JsonValueTreeNode extends JsonTreeNode {
	
	private JsonElement leafElement;
	private String label;
	
	public JsonValueTreeNode(IJsonTreeNode parent, JsonElement leafElement, String label) {
		super(parent);
		this.leafElement = leafElement;
		this.label = label;
	}

	@Override
	public String getName() {
		return this.label;
	}

	@Override
	protected void createChildren(List childrenObject) {
		
	}
	
	public JsonElement getJsonElement() {
		return this.leafElement;
	}
	
	public boolean hasChildren() {
		return false;
	}

}
