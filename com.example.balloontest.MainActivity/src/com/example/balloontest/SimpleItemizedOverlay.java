package com.example.balloontest;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class SimpleItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Context c;
	private int buildingID;
	
	public SimpleItemizedOverlay(Drawable defaultMarker, MapView mapView, int _buildingID) {
		super(boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
		buildingID=_buildingID;
	}

	public void addOverlay(OverlayItem overlay) {
	    m_overlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		Intent buildingInfoIntent = new Intent(c,InformationActivity.class);
		buildingInfoIntent.setClassName("com.example.balloontest", "com.example.balloontest.InformationActivity");
		buildingInfoIntent.putExtra("building_id", buildingID);
		c.startActivity(buildingInfoIntent);
		return true;
	}
	
}