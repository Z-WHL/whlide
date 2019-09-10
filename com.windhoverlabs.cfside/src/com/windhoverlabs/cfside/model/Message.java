package com.windhoverlabs.cfside.model;

public class Message {
	private int miid;
	private String identifier;
	private String description;
	
	public Message(Integer miid, String identifier, String description) {
		this.miid = miid;
		this.identifier = identifier;
		this.description = description;
	}
	
	public int getMiid() {
		return miid;
	}
	
	public void setMiid(int miid) {
		this.miid = miid;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Message copy() {
		return new Message(miid, identifier, description);
	}
	
	public String toString() {
		return (this.miid + " " + this.identifier + " " + this.description);
	}
	
	public boolean equals(Object o) {
		if (o instanceof Message) {
			Message obj = (Message) o;
			return obj.miid == miid && obj.identifier == identifier && obj.description == description;
		}
		return false;
	}
}
