package com.example.balloontest;


//Se importo este y no el SQL

public class Building {
	
	private String name;
	private String number;
	private Integer longitude;
	private Integer latitude;
	private Integer id;
	private String info;

	
	
	public Building(String name, String number, Integer latitude, Integer longitude,Integer id,String info) {
		super();
		this.name=name;
		this.number=number;
		this.longitude=longitude;
		this.latitude=latitude;
		this.id=id;
		this.info=info;
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
	public int getLongitudeE6() {
	return longitude;
	}
	public int getLatitudeE6() {
		return latitude;
		}
	
	public String getInfo() {
		return info;
		}


}
