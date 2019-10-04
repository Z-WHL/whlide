package com.windhoverlabs.cfside.ui.tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GroupedJsonList {

	private ArrayList<SingleJsonObject> list;
	private String groupedLabel;
	private Set changeListeners = new HashSet();
	
	public GroupedJsonList(JsonObject currentObject, String groupedLabel) {
		this.groupedLabel = groupedLabel;
		this.list = new ArrayList<SingleJsonObject>();
		for (Map.Entry<String, JsonElement> entry : currentObject.entrySet()) {
			if (entry.getValue().isJsonObject()) {
				JsonElement tempJe = entry.getValue();
				JsonObject tempJo = tempJe.getAsJsonObject();
				
				SingleJsonObject toAdd = new SingleJsonObject(entry.getKey(), tempJo);
				list.add(toAdd);
			}
		}
	}
	
	public void setGroupedLabel(String newGroupLabel) {
		this.groupedLabel = newGroupLabel;
	}
	
	public String getGroupedLabel() {
		return this.groupedLabel;
	}
	
	public List<SingleJsonObject> getList() {
		return this.list;
	}
	
	public SingleJsonObject[] getArray() {
		int size = list.size();
		SingleJsonObject[] arr = new SingleJsonObject[size];
		int i = 0;
		for (SingleJsonObject entry : list) {
			arr[i++] = entry;
		}
		return arr;
	}

	public void addConfig() {
		JsonParser jp = new JsonParser();
		JsonObject jo = jp.parse("{}").getAsJsonObject();
		SingleJsonObject obj = new SingleJsonObject("Key", jo);
		list.add(obj);
		Iterator it = changeListeners.iterator();
		while (it.hasNext()) {
			((IConfigListViewer) it.next()).addConfig(obj);
		}
	}
	
	public void removeConfig(SingleJsonObject obj) {
		list.remove(obj);
		Iterator it = changeListeners.iterator();
		while (it.hasNext()) {
			((IConfigListViewer) it.next()).removeConfig(obj);
		}
	}
	
	public void removeChangeListener(IConfigListViewer viewer) {
		changeListeners.remove(viewer);
	}
	
	public void addChangeListener(IConfigListViewer viewer) {
		changeListeners.add(viewer);
	}

	public void jsonChanged(SingleJsonObject jsonObj) {
		Iterator it = changeListeners.iterator();
		while (it.hasNext()) {
			((IConfigListViewer) it.next()).updateConfig(jsonObj);
		}
	}
}
