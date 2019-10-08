package com.windhoverlabs.cfside.ui.trees;

import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.utils.CfsConfig;
import com.google.gson.JsonArray;

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
		NamedObject namedObject = (NamedObject) parentObject;      
		JsonElement configElem = (JsonElement) namedObject.getObject();
		
		System.out.println("getElements " + parentObject);
		
		if(hasChildren(parentObject)) {
			if(configElem.isJsonArray()) {
				JsonArray array = configElem.getAsJsonArray();
				
				Object[] outArray = new Object[array.size()];
				
				for(int i = 0; i < array.size(); ++i) {
					outArray[i] = array.get(i);
				}
				
				return outArray;

			} else if(configElem.isJsonObject()) {
				int i = 0;
				JsonObject obj = configElem.getAsJsonObject();
				
				Object[] outArray = new Object[obj.size()];

		        for (Map.Entry<String,JsonElement> entry :  obj.entrySet()) {
		        	NamedObject outNamedObject = new NamedObject();
		        	
		        	outNamedObject.setName(entry.getKey());

		        	String newPath = "";
		        	if(namedObject.getPath() != "") {
		        		newPath = namedObject.getPath() + ".";
		        	}
		        	newPath = newPath + entry.getKey();
		        	outNamedObject.setPath(newPath);
		        	
		        	outNamedObject.setObject(entry.getValue());
		        	outArray[i] = outNamedObject;
		        	if (i % 3 == 0) {
		        		outNamedObject.setOverridden(true);
		        	}
		        	i = i + 1;
		        	
		        	if(this.config.isOverridden(newPath)) {
		        		outNamedObject.setOverridden(true);
		        	} else {
		        		outNamedObject.setOverridden(false);
		        	}
		        } 
				
				return outArray;
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
				int i = 0;
				JsonObject obj = configElem.getAsJsonObject();
				
				Object[] outArray = new Object[obj.size()];

		        for (Map.Entry<String,JsonElement> entry :  obj.entrySet()) {
		        	NamedObject outNamedObject = new NamedObject();
		        	
		        	outNamedObject.setName(entry.getKey());
		        	String newPath = "";
		        	if(namedObject.getPath() != "") {
		        		newPath = namedObject.getPath() + ".";
		        	}
		        	newPath = newPath + entry.getKey();
		        	outNamedObject.setPath(newPath);
		        	
		        	outNamedObject.setObject(entry.getValue());
		        	
		        	outArray[i] = outNamedObject;
		        	if (i % 3 == 0) {
		        		outNamedObject.setOverridden(true);
		        	}
		        	i = i + 1;
		        	
		        	if(this.config.isOverridden(newPath)) {
		        		outNamedObject.setOverridden(true);
		        	} else {
		        		outNamedObject.setOverridden(false);
		        	}
		        } 
				
				return outArray;
			}
		} 
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return ((IJsonTreeNode) element).getParent();
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
