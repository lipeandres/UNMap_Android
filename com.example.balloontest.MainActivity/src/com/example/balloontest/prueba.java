package com.example.balloontest;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

public class prueba extends Activity {
	 
	private DBHelper BD;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	 
	//Creamos y abrimos la base de datos
	BD=new DBHelper(this);
	BD.open();
	 
		 
	//Obtenemos un listado de todos los edificios
	ArrayList<Building> buildings = new ArrayList<Building>();
	buildings =(ArrayList<Building>) BD.getBuildings();
	 
	}
	 
	@Override
	public void onPause() {
	super.onPause();
	BD.close();
	}
	@Override
	public void onResume() {
	super.onResume();
	BD.open();
}
}

