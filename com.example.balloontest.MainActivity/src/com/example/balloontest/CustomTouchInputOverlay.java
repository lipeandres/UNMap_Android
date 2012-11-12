package com.example.balloontest;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class CustomTouchInputOverlay extends Overlay{
private long start,stop;
private boolean moved;
private int x,y;
private GeoPoint touchedPoint;
private MapView map;
public Context context;
private SimpleItemizedOverlay buildingBalloon;
private boolean balloonExists;//tracks wheter or not a balloon has been created
Drawable buildingMarker;
List<Overlay> mapOverlayList;

public CustomTouchInputOverlay (MapView _map){
	map=_map;
	context=_map.getContext();
	mapOverlayList = _map.getOverlays();
	balloonExists=false;
}
	public boolean onTouchEvent(MotionEvent e, MapView m) {
		//if the map moves, don't place marks
		if (e.getAction() == MotionEvent.ACTION_MOVE) {
			start = 0;
			stop = 0;
			moved = true;
		}
		//when touched, get coordinates and event start time
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			start = e.getEventTime();
			moved = false;
			x = (int) e.getX();
			y = (int) e.getY();
			touchedPoint = map.getProjection().fromPixels(x, y);
		}
		//when finger is removed but not moved set event stop time
		if (e.getAction() == MotionEvent.ACTION_UP && !moved) {
			stop = e.getEventTime();
			moved = false;
			if(balloonExists)
			{
				buildingBalloon.hideBalloon();
				mapOverlayList.remove(buildingBalloon);
				balloonExists=false;
			}
		}
		//If the press was long enough, show balloon
		if (stop - start > 150) {
		balloonExists = true;	
		//GeoPoint point = new GeoPoint(4631543,-74094501);
		buildingMarker = context.getResources().getDrawable(R.drawable.marker);
		buildingBalloon = new SimpleItemizedOverlay(buildingMarker, map);
		buildingBalloon.setShowClose(false);
		OverlayItem overlayItem = new OverlayItem(touchedPoint, "Edificio 411", 
				"Laboratorios de Ingenieria");
		buildingBalloon.addOverlay(overlayItem);
		mapOverlayList.add(buildingBalloon);
		buildingBalloon.setFocus(overlayItem);
		return true;
		}

		return false;
	}
}
