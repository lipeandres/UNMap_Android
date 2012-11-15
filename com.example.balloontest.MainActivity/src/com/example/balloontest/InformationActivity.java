package com.example.balloontest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InformationActivity extends Activity{
	private int buildingID;
	private DBHelper buildingDB;
	Building building;
	TextView title;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Inflate the layout
		setContentView(R.layout.infoact_layout);
		//Get the intent extras, that set the information of the building
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}	
		//Get the building information from the database
		buildingID = extras.getInt("building_id");
		// --Obtain the building list and information from the database
		buildingDB = new DBHelper(InformationActivity.this);
		buildingDB.open();
		building = (Building) buildingDB.getBuilding(buildingID);
		// Since the DB is static we can close it now
		buildingDB.close();
		//Print the title
		title = (TextView) findViewById(R.id.infoact_title);
		title.setText(building.getName());
	}

}
