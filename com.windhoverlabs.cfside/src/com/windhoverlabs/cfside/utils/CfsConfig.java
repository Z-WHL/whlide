package com.windhoverlabs.cfside.utils;

import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.trees.NamedObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;

public class CfsConfig {
	JsonElement full;
	JsonElement local;
	
	
	public void setFull(JsonElement config) {
		this.full = config;
	}
	
	
	public JsonElement getFull() {
		return this.full;
	}
	
	
	public void setLocal(JsonElement config) {
		this.local = config;
	}
	
	
	public JsonElement getLocal() {
		return this.local;
	}
	
	
	public JsonElement getJsonElement(String path){
	    String[] parts = path.split("\\.|\\[|\\]");
	    JsonElement result = this.full;

	    for (String key : parts) {

	        key = key.trim();
	        if (key.isEmpty())
	            continue;

	        if (result == null){
	            result = JsonNull.INSTANCE;
	            break;
	        }

	        if (result.isJsonObject()){
	            result = ((JsonObject)result).get(key);
	        }
	        else if (result.isJsonArray()){
	            int ix = Integer.valueOf(key) - 1;
	            result = ((JsonArray)result).get(ix);
	        }
	        else break;
	    }

	    return result;
	}
	
	
	public JsonElement localContainsElement(String path) {
		String[] parts = path.split("\\.|\\[|\\]");
	    JsonElement result = this.local;

	    for (String key : parts) {

	        key = key.trim();
	        if (key.isEmpty())
	            continue;

	        if (result == null){
	            result = JsonNull.INSTANCE;
	            break;
	        }

	        if (result.isJsonObject()){
	            result = ((JsonObject)result).get(key);
	        }
	        else if (result.isJsonArray()){
	            int ix = Integer.valueOf(key) - 1;
	            result = ((JsonArray)result).get(ix);
	        }
	        else break;
	    }

	    return result;
	}
	
	public boolean isOverridden(String path) {
		if(localContainsElement(path) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setNamedObject(NamedObject namedObj) {
		
	}


	public void save(NamedObject js) {
		setNamedObject(js);
		System.out.println("Here is the path we need to save to! " + js.getPath());
		
	}
}
