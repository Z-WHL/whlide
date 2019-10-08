package com.windhoverlabs.cfside.ui.trees;


public class NamedObject {
	
	private String name;
	private Object object;
	private String path;
	private boolean overridden;
	
	public NamedObject() {
		this.overridden = false;
		this.path = "";
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setObject(Object object) {
		this.object = object;
	}
	
	public Object getObject() {
		return this.object;
	}
	
	public void setOverridden(boolean overridden) {
		this.overridden = overridden;
	}
	
	public boolean getOverridden() {
		return this.overridden;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}

	public void save() {
		System.out.println(getPath());
		
	}
	
	
}
