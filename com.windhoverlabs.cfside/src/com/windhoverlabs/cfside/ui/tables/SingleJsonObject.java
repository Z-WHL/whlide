package com.windhoverlabs.cfside.ui.tables;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SingleJsonObject {
	
	private String jsonObjectKey;
	private JsonObject jsonObject;
	private HashMap<String, String> jsonKeyValues = new HashMap<String, String>();
	// TODO: Add enumeration so fields can be out of order in json. For now, assume schema MUST be correct.
	
	public SingleJsonObject(String jsonObjectKey, JsonObject jsonObject) {
		this.jsonObjectKey = jsonObjectKey;
		this.jsonObject = jsonObject;
		
		for (Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject().entrySet()) {
			if (!entry.getValue().isJsonObject()) {
				jsonKeyValues.put(entry.getKey(), entry.getValue().getAsString());
			}
		}
	}
	
	public void setJsonObjectKey(String newKey) {
		this.jsonObjectKey = newKey;
	}
	
	public String getJsonObjectKey() {
		return this.jsonObjectKey;
	}
	
	public void setJsonObject(JsonObject newJsonObject) {
		this.jsonObject = newJsonObject;
	}
	
	public JsonObject getJsonObject() {
		return this.jsonObject;
	}
	
	public boolean hasJsonObject() {
		for (Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject().entrySet()) {
			if (entry.getValue().isJsonObject()) {
				return true;
			}
		}
		return false;
	}
}
