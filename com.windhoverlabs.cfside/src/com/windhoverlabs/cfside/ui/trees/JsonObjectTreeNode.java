package com.windhoverlabs.cfside.ui.trees;

import java.util.List;import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonObjectTreeNode extends JsonTreeNode {
	
	private JsonElement jeObject;
	private String jeObjectLabel;
	
	public JsonObjectTreeNode(JsonElement jeObject, String label) {
		this(null, jeObject, label);
	}
	
	public JsonObjectTreeNode(IJsonTreeNode parent, JsonElement jeObject, String label) {
		super(parent);
		this.jeObject = jeObject;
		this.jeObjectLabel = label;
	}

	@Override
	public String getName() {
		return this.jeObjectLabel;
	}
	
	public JsonElement getJsonElement() {
		return this.jeObject;
	}

	@Override
	protected void createChildren(List childrenObject) {
		JsonObject joObject = jeObject.getAsJsonObject();
		for(Map.Entry<String, JsonElement> entry : joObject.entrySet()) {
			if (entry.getValue().isJsonObject()) {
				JsonObject currentEntry = entry.getValue().getAsJsonObject();
				boolean isLeaf = false;
				for (Map.Entry<String, JsonElement> curEntry : currentEntry.entrySet()) {
					if (curEntry.getValue().isJsonObject()) {
						isLeaf = isLeaf | true;
					}
				}
				if (isLeaf) {
					childrenObject.add(new JsonObjectTreeNode(this, entry.getValue(), entry.getKey()));
				} else {
					childrenObject.add(new JsonValueTreeNode(this, entry.getValue(), entry.getKey()));
				}
			}
		}
	}
}
