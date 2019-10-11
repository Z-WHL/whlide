package com.windhoverlabs.cfside.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.trees.NamedObject;

public class CfsConfig {
	JsonElement full;
	JsonElement local;
	String path;
	
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
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public JsonElement fullGetElement(String path){
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
	
	
	public JsonElement localGetElement(String path) {
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
	
	public NamedObject getObject(String path, String which, CfsConfig cfsConfig) {
		String[] parts = path.split("\\.|\\[|\\]");
		JsonElement toSearchElement = which.equalsIgnoreCase("local") ? this.local : this.full;
		JsonObject toSearchObject = toSearchElement.getAsJsonObject();
		
		NamedObject theObject = new NamedObject();
		theObject.setPath(path);
		
		for (int i = 0; i < parts.length; i++) {
			if (i + 1 == parts.length) {
				theObject.setName(parts[i]);
				if (toSearchObject.has(parts[i])) {
					toSearchObject = toSearchObject.get(parts[i]).getAsJsonObject();
					theObject.setObject(toSearchObject);
					if (which.equalsIgnoreCase("local")) {
						theObject.setOverridden(true);
					} else {
						if(cfsConfig.isOverridden(path)) {
							theObject.setOverridden(true);
				    	} else {
				    		theObject.setOverridden(false);
				    	}
					}
					return theObject;
				} else {
					return null;
				}
			}
			if (toSearchObject.has(parts[i])) {
				toSearchObject = toSearchObject.get(parts[i]).getAsJsonObject();
			} else {
				return null;
			}
		}
		return null;
	}
	
	public boolean isOverridden(String path) {
		JsonElement result = localGetElement(path);
		
		if((result == null) || (result == JsonNull.INSTANCE)) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setSingleRecord(NamedObject namedObj) {
		String[] parts = namedObj.getPath().split("\\.|\\[|\\]");
		int depth = parts.length;
		JsonElement localPointer = this.local;
		JsonObject localObject = localPointer.getAsJsonObject();
		
		JsonObject pointerToRecord = (JsonObject) namedObj.getObject();
		
		for (int i = 0; i < depth; i++) {
			// You are currently at the selected element
			if (i + 1 == depth) {
				if (localObject.has(parts[i])) {
					localObject = localObject.get(parts[i]).getAsJsonObject();
					pointerToRecord = pointerToRecord.get(parts[i]).getAsJsonObject();
					localObject.addProperty(namedObj.getName(), pointerToRecord.get(namedObj.getName()).getAsString() );
					break;
				} else {
					localObject.add(parts[i], new JsonObject());
					localObject = localObject.get(parts[i]).getAsJsonObject();
					pointerToRecord = pointerToRecord.get(parts[i]).getAsJsonObject();
					localObject.addProperty(namedObj.getName(), pointerToRecord.get(namedObj.getName()).getAsString() );
					break;
				}				
			} 			
			// Let's check if the path exists and if it does update the crawl to use the json object.
			// If the path doesn't exist then create an empty object inside the current crawled object.
			if (localObject.has(parts[i])) {
				// Update the element to be crawled.
				localObject = localObject.get(parts[i]).getAsJsonObject(); 
				pointerToRecord = pointerToRecord.get(parts[i]).getAsJsonObject();
			} else {
				localObject.add(parts[i], new JsonObject());
				localObject = localObject.get(parts[i]).getAsJsonObject();
				pointerToRecord = pointerToRecord.get(parts[i]).getAsJsonObject();
			}			
		}
		this.local = localPointer;
		saveToFile(this.local);
	}
	
	public void setNamedObjectLocal(NamedObject namedObj) {
		String[] parts = namedObj.getPath().split("\\.|\\[|\\]");
		int depth = parts.length;
		JsonElement localPointer = this.local;
		JsonObject localObject = localPointer.getAsJsonObject();
		
		for (int i = 0; i < depth; i++) {
			// You are currently at the selected element
			if (i + 1 == depth) {
				JsonElement toUpdate = (JsonElement) namedObj.getObject();
				localObject.add(namedObj.getName(), toUpdate);
				break;
			} 			
			// Let's check if the path exists and if it does update the crawl to use the json object.
			// If the path doesn't exist then create an empty object inside the current crawled object.
			if (localObject.has(parts[i])) {
				// Update the element to be crawled.
				localObject = localObject.get(parts[i]).getAsJsonObject(); 
			} else {
				localObject.add(parts[i], new JsonObject());
				localObject = localObject.get(parts[i]).getAsJsonObject();
			}			
		}
		
		this.local = localPointer;
	}
	
	public void setNamedObjectFull(NamedObject namedObj) {
		String[] parts = namedObj.getPath().split("\\.|\\[|\\]");
		int depth = parts.length;
		JsonElement localPointer = this.full;
		JsonObject localObject = localPointer.getAsJsonObject();
		
		for (int i = 0; i < depth; i++) {
			// You are currently at the selected element
			if (i + 1 == depth) {
				JsonElement toUpdate = (JsonElement) namedObj.getObject();
				localObject.add(namedObj.getName(), toUpdate);
				break;
			} 			
			// Let's check if the path exists and if it does update the crawl to use the json object.
			// If the path doesn't exist then create an empty object inside the current crawled object.
			if (localObject.has(parts[i])) {
				// Update the element to be crawled.
				localObject = localObject.get(parts[i]).getAsJsonObject(); 
			} else {
				localObject.add(parts[i], new JsonObject());
				localObject = localObject.get(parts[i]).getAsJsonObject();
			}			
		}
		this.full = localPointer;
	}

	public void save(NamedObject js) {
		// Update our in-memory object/representation
		setNamedObjectLocal(js);
		setNamedObjectFull(js);
		// Now let's update the persistent representation. Can be file, database etc.
		saveToFile(this.local);
	}
	
	public void saveLocal(NamedObject localObject) {
		setNamedObjectLocal(localObject);
		saveToFile(this.local);
	}
	
	public void saveFull(NamedObject fullObject) {
		setNamedObjectFull(fullObject);
	}
	
	public void saveToFile(JsonElement json) {
		String toBeSaved = JsonObjectsUtil.beautifyJson(json.getAsJsonObject().toString());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(toBeSaved);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
