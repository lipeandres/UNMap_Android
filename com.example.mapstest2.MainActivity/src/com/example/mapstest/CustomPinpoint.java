package com.example.mapstest;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CustomPinpoint extends ItemizedOverlay<OverlayItem>{

	private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
	private Context c;
	
	public CustomPinpoint(Drawable m, Context context_in) {
		this(m);
		c = context_in;
	}
	
	public CustomPinpoint(Drawable defaulMarker) {
		super(boundCenter(defaulMarker));
		
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return pinpoints.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return pinpoints.size();
	}
	
	public void insertPinpoints(OverlayItem mapItem){
		pinpoints.add(mapItem);
		this.populate();
	}

}
