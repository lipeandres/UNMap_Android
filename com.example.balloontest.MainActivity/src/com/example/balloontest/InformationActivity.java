package com.example.balloontest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class InformationActivity extends Activity{
	private int buildingID;
	private DBHelper buildingDB;
	Building building;
	TextView buildingTitleView;
	ImageView buildingImageView;
	TextView buildingInfoView;
	

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
		buildingTitleView = (TextView) findViewById(R.id.Building_Name);
		buildingTitleView.setText(building.getName() + " - " + building.getNumber());
		//Getting the image to display
		buildingImageView = (ImageView) findViewById(R.id.Building_Image);
		UrlImageViewHelper.setUrlDrawable(buildingImageView, "https://dl.dropbox.com/u/1284250/UNMap/201 derecho.jpg", R.drawable.placeholder);
		//Getting Info
		buildingInfoView=(TextView) findViewById(R.id.Building_Info);
		buildingInfoView.setText(building.getInfo());
	}

}
