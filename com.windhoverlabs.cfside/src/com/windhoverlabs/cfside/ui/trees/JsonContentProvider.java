package com.windhoverlabs.cfside.ui.trees;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class JsonContentProvider implements ITreeContentProvider {
	CfsConfig config;
	
	
	public JsonContentProvider(CfsConfig config) {
		setCfsConfig(config);
	}
	
	public void setCfsConfig(CfsConfig config) {
		this.config = config;
	}
	
	public CfsConfig getCfsConfig() {
	    return this.config;
	}

	@Override
	public Object[] getElements(Object parentObject) {
		/* Iterate through the modules and add pages for them. */  
		NamedObject namedObject = (NamedObject) parentObject;        
		JsonElement configElem = (JsonElement) namedObject.getObject();
		
		System.out.println("getChildren " + parentObject);
		
		if(hasChildren(parentObject)) {
			if(configElem.isJsonArray()) {
				JsonArray array = configElem.getAsJsonArray();
				
				Object[] outArray = new Object[array.size()];
				
				for(int i = 0; i < array.size(); ++i) {
					outArray[i] = array.get(i);
				}
				
				return outArray;

			} else if(configElem.isJsonObject()) {
				JsonObject obj = configElem.getAsJsonObject();
				
				ArrayList<Object> outArray = new ArrayList<Object>();

		        for (Map.Entry<String,JsonElement> entry :  obj.entrySet()) {
		        	if (!(entry.getKey().charAt(0) == '_')) {
			        	NamedObject outNamedObject = new NamedObject();
			        	
			        	outNamedObject.setName(entry.getKey());
			        	String newPath = "";
			        	if(namedObject.getPath() != "") {
			        		newPath = namedObject.getPath() + ".";
			        	}
			        	newPath = newPath + entry.getKey();
			        	outNamedObject.setPath(newPath);
			        	
			        	outNamedObject.setObject(entry.getValue());
			        	
			        	outArray.add(outNamedObject);
			        	
			        	if(this.config.isOverridden(newPath)) {
			        		outNamedObject.setOverridden(true);
			        	} else {
			        		outNamedObject.setOverridden(false);
			        	}
		        	}
		        } 
				
				return outArray.toArray();
			}
		} 
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		/* Iterate through the modules and add pages for them. */  
		NamedObject namedObject = (NamedObject) parentElement;        
		JsonElement configElem = (JsonElement) namedObject.getObject();
		
		System.out.println("getChildren " + parentElement);
		
		if(hasChildren(parentElement)) {
			if(configElem.isJsonArray()) {
				JsonArray array = configElem.getAsJsonArray();
				
				Object[] outArray = new Object[array.size()];
				
				for(int i = 0; i < array.size(); ++i) {
					outArray[i] = array.get(i);
				}
				
				return outArray;

			} else if(configElem.isJsonObject()) {
				JsonObject obj = configElem.getAsJsonObject();
				
				ArrayList<Object> outArray = new ArrayList<Object>();

		        for (Map.Entry<String,JsonElement> entry :  obj.entrySet()) {
		        	if (!(entry.getKey().charAt(0) == '_')) {
			        	NamedObject outNamedObject = new NamedObject();
			        	
			        	outNamedObject.setName(entry.getKey());
			        	String newPath = "";
			        	if(namedObject.getPath() != "") {
			        		newPath = namedObject.getPath() + ".";
			        	}
			        	newPath = newPath + entry.getKey();
			        	outNamedObject.setPath(newPath);
			        	
			        	outNamedObject.setObject(entry.getValue());
			        	
			        	outArray.add(outNamedObject);
			        	
			        	if(this.config.isOverridden(newPath)) {
			        		outNamedObject.setOverridden(true);
			        	} else {
			        		outNamedObject.setOverridden(false);
			        	}
		        	}
		        } 
				
				return outArray.toArray();
			}
		} 
		return null;
	}

	@Override
	public Object getParent(Object element) {
		NamedObject namedObj = (NamedObject) element;
		String path = namedObj.getPath();
	    String[] parts = path.split("\\.|\\[|\\]");

	    StringBuilder sb = new StringBuilder();
	    for (int i = 0 ; i < parts.length - 1; i++) {
	    	sb.append(parts[i]);
	    }
	    JsonElement foundJsonElement = config.fullGetElement(sb.toString());
	    
		return foundJsonElement;
	}

	@Override
	public boolean hasChildren(Object element) {
		NamedObject namedObject = (NamedObject) element;     
		JsonElement configElem = (JsonElement) namedObject.getObject();
		
		if(configElem.isJsonArray()) {
			JsonArray array = configElem.getAsJsonArray();
			
			if(array.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else if(configElem.isJsonObject()) {
			JsonObject obj = configElem.getAsJsonObject();
			
			if(obj.size() > 0) {
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}

}
