package com.example.balloontest;

//Se importo este y no el SQL

public class BuildingItem {
	
	String number;
	int longitud;
	int latitud;
	String name;
	
	

	public String getName() {
	return name;
	}
	
	public String getNumber() {
	return number;
	}
	public int getLongitud() {
	return longitud;
	}
	public int getLatitud() {
		return latitud;
		}
	
	public BuildingItem(String _name,String _number, int _longitud, int _latitud) {
	number = _number;
	name = _name;
	longitud = _longitud;
	latitud = _latitud;
	}
	

}
