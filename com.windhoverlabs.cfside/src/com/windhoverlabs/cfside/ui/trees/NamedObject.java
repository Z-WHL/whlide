package com.windhoverlabs.cfside.ui.trees;


public class NamedObject {
	
	private String name;
	private Object object;
	private boolean overwritten;
	
	public void setOverritten(boolean input) {
		this.overwritten = input;
	}
	
	public boolean getOverwritten() {
		return this.overwritten;
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
}
