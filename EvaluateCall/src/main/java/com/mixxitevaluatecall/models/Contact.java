package com.mixxitevaluatecall.models;

public class Contact {
private int id, type, idCat, color;
private String idContact;

public Contact(int id, int type, int idCat, int color, String idContact) {
	super();
	this.id = id;
	this.type = type;
	this.idCat = idCat;
	this.color = color;
	this.idContact = idContact;
}



public Contact(int idCat, String idContact) {
	super();
	this.idCat = idCat;
	this.idContact = idContact;
}



public Contact(int idCat) {
	super();
	this.idCat = idCat;
}



public int getColor() {
	return color;
}



public void setColor(int color) {
	this.color = color;
}



public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public int getIdCat() {
	return idCat;
}
public void setIdCat(int idCat) {
	this.idCat = idCat;
}
public String getIdContact() {
	return idContact;
}
public void setIdContact(String idContact) {
	this.idContact = idContact;
} 




}
