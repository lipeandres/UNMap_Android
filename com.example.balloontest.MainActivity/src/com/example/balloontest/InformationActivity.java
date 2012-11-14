package com.example.balloontest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;

public class InformationActivity extends Activity implements OnClickListener{
	private int buildingID;

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
		buildingID = extras.getInt("building_id");
		Toast.makeText(InformationActivity.this, Integer.toString(buildingID), Toast.LENGTH_LONG).show();
	}

	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
