package com.windhoverlabs.cfside.model;

public class Message {
	private int miid;
	private String identifier;
	private String name;
	private String type;
	private String description;
	
	public Message(Integer miid, String identifier, String name, String type, String description) {
		this.miid = miid;
		this.identifier = identifier;
		this.name = name;
		this.type = type;
		//TODO CHECK TYPE IS VALID
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setType(String type) {
		if (!type.equalsIgnoreCase("cmd") && !type.equalsIgnoreCase("tlm")) {
			System.out.println("The type is not valid");
			return;
			//TODO Do something meaningful.
		}
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Message copy() {
		return new Message(miid, identifier, name, type, description);
	}
	
	public String toString() {
		String tostring = "{'miid':" + Integer.toString(this.miid) + 
				",'identifier':'" + this.identifier +
				"','name':'" + this.name +
				"','type':'" + this.type +
				"','description':'" + this.description + "'}";
				
		return tostring;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Message) {
			Message obj = (Message) o;
			return obj.miid == miid && obj.identifier == identifier && obj.description == description;
		}
		return false;
	}
}





