package com.henrik.bak.restaurantguide;

public class Restaurant {

	private String name="";
	private String address="";
	private String type="";
	private String notes="";
	
	public String getName() {
		return(name);
	}
	
	public String getNote() {
		return notes;
	}

	public void setNote(String note) {
		this.notes = note;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name=name;
	}
	
	public String getAddress() {
		return(address);
	}
	
	public void setAddress(String address) {
		this.address = address;

	}
	
	public String toString(){
		return getName();
	}
}
