package com.example.balloontest;


//Se importo este y no el SQL

public class Building {
	
	private String name;
	private String number;
	private Integer longitud;
	private Integer latitud;
	private Integer id;
	
	
	public Building(String name, String number, Integer longitud, Integer latitud,Integer id) {
		super();
		this.name=name;
		this.number=number;
		this.longitud=longitud;
		this.latitud=latitud;
		this.id=id;
		}
	
	public Building() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
	return name;
	}
	
	public Integer getId() {
	return id;
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

}
