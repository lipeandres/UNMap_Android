package com.example.balloontest;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class CustomTouchInputOverlay extends Overlay {
	private long start, stop;
	private boolean moved;
	private int x, y;
	private GeoPoint touchedPoint;
	private GeoPoint BoundsTopLeftCorner;
	private GeoPoint BoundsBottomLeftCorner;
	private GeoPoint BoundsTopRightCorner;
	private MapView map;
	private Location touchedLocation;
	private Building nearestBuilding;
	public Context context;
	ArrayList<Building> buildingList;
	private SimpleItemizedOverlay buildingBalloon;
	private boolean balloonExists;// tracks whether or not a balloon has been
									// created
	Drawable buildingMarker;
	List<Overlay> mapOverlayList;
	private static final int UN_LIMIT_W = -74094501;
	private static final int UN_LIMIT_S = 4631543;
	private static final int UN_LIMIT_N = 4644974;
	private static final int UN_LIMIT_E = -74079201;

	public CustomTouchInputOverlay(MapView _map,
			ArrayList<Building> _buildingList) {
		map = _map;
		context = _map.getContext();
		mapOverlayList = _map.getOverlays();
		balloonExists = false;
		BoundsTopLeftCorner = new GeoPoint(UN_LIMIT_N, UN_LIMIT_W);
		BoundsBottomLeftCorner = new GeoPoint(UN_LIMIT_S, UN_LIMIT_W);
		BoundsTopRightCorner = new GeoPoint(UN_LIMIT_N, UN_LIMIT_E);
		buildingList = _buildingList;
		touchedLocation = new Location("");
		nearestBuilding = new Building();
	}

	public boolean onTouchEvent(MotionEvent e, MapView m) {

		// when touched, get coordinates and event start time
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			start = e.getEventTime();
			moved = false;
			x = (int) e.getX();
			y = (int) e.getY();
			touchedPoint = map.getProjection().fromPixels(x, y);
		}
		// when finger is removed but not moved set event stop time
		if (e.getAction() == MotionEvent.ACTION_UP && !moved) {
			stop = e.getEventTime();
			moved = false;
			if (balloonExists) {
				buildingBalloon.hideBalloon();
				mapOverlayList.remove(buildingBalloon);
				balloonExists = false;
			}
		}
		// If the press was long enough, show balloon
		if (stop - start > 150) {
			//Get the nearest building to the touch calculating the distance to each one
			//we use Location.distanceTo() which receives Location objects, in this case created
			//with the help of the existing geopoints
			touchedLocation.setLatitude((float)(touchedPoint.getLatitudeE6()/1E6));
			touchedLocation.setLongitude((float)(touchedPoint.getLongitudeE6()/1E6));
			int nearestBuildingIndex = getNearestBuildingIndex();
			nearestBuilding = buildingList.get(nearestBuildingIndex);
			GeoPoint nearestBuildingPoint = new GeoPoint(nearestBuilding.getLatitud(), nearestBuilding.getLongitud());
			
			balloonExists = true;
			buildingMarker = context.getResources().getDrawable(
					R.drawable.orangemarker);
			buildingBalloon = new SimpleItemizedOverlay(buildingMarker, map, nearestBuilding.getId());
			buildingBalloon.setShowClose(false);
			OverlayItem overlayItem = new OverlayItem(nearestBuildingPoint,
					"Edificio "+String.valueOf(nearestBuilding.getNumber()), nearestBuilding.getName());
			buildingBalloon.addOverlay(overlayItem);
			mapOverlayList.add(buildingBalloon);
			buildingBalloon.setFocus(overlayItem);
			return true;
		}
		// if the map moves, don't place marks and place boundaries for the
		// movement
		if (e.getAction() == MotionEvent.ACTION_MOVE) {
			start = 0;
			stop = 0;
			moved = true;
			// (only works for north of equator)
			// * map right side (lat) can't go past the left (lat) of screen

			// get geopoints of the 4 corners of the screen
			Projection proj = map.getProjection();
			GeoPoint screenTopLeft = proj.fromPixels(0, 0);
			GeoPoint screenTopRight = proj.fromPixels(map.getWidth(), 0);
			GeoPoint screenBottomLeft = proj.fromPixels(0, map.getHeight());

			double screenTopLat = screenTopLeft.getLatitudeE6() / 1E6;
			double screenBottomLat = screenBottomLeft.getLatitudeE6() / 1E6;
			double screenLeftlong = screenTopLeft.getLongitudeE6() / 1E6;
			double screenRightlong = screenTopRight.getLongitudeE6() / 1E6;

			double mapTopLat = BoundsTopLeftCorner.getLatitudeE6() / 1E6;
			double mapBottomLat = BoundsBottomLeftCorner.getLatitudeE6() / 1E6;
			double mapLeftlong = BoundsTopLeftCorner.getLongitudeE6() / 1E6;
			double mapRightlong = BoundsTopRightCorner.getLongitudeE6() / 1E6;

			// screen bottom greater than map top
			// screen top less than map bottom
			// screen right less than map left
			// screen left greater than map right
			boolean movedLeft = false;
			boolean movedRight = false;
			boolean movedUp = false;
			boolean movedDown = false;

			boolean offscreen = false;
			if (screenBottomLat > mapTopLat) {
				movedUp = true;
				offscreen = true;
			}
			if (screenTopLat < mapBottomLat) {
				movedDown = true;
				offscreen = true;
			}
			if (screenRightlong < mapLeftlong) {
				movedLeft = true;
				offscreen = true;
			}
			if (screenLeftlong > mapRightlong) {
				movedRight = true;
				offscreen = true;
			}

			if (offscreen) {
				// work out on which plane it's been moved off screen (lat/lng)

				if (movedLeft || movedRight) {

					double newBottomLat = screenBottomLat;
					double newTopLat = screenTopLat;

					double centralLat = newBottomLat
							+ ((newTopLat - newBottomLat) / 2);
					if (movedRight)
						map.getController().setCenter(
								new GeoPoint((int) (centralLat * 1E6),
										(int) (mapRightlong * 1E6)));
					else
						map.getController().setCenter(
								new GeoPoint((int) (centralLat * 1E6),
										(int) (mapLeftlong * 1E6)));

				}
				if (movedUp || movedDown) {

					// longs will all remain the same
					double newLeftLong = screenLeftlong;
					double newRightLong = screenRightlong;

					double centralLong = (newRightLong + newLeftLong) / 2;

					if (movedUp)
						map.getController().setCenter(
								new GeoPoint((int) (mapTopLat * 1E6),
										(int) (centralLong * 1E6)));

					else
						map.getController().setCenter(
								new GeoPoint((int) (mapBottomLat * 1E6),
										(int) (centralLong * 1E6)));
				}

			}

		}

		return false;
	}

	private int getNearestBuildingIndex() {
		int i = 0;
		int nearestBuildingIndex=0;
		Location tempBuildingLocation;
		tempBuildingLocation = new Location("");
		float distance = 0;
		float prev_distance = 9999999;
		while (i < buildingList.size()) {
			tempBuildingLocation.setLatitude((double) (buildingList.get(i)
					.getLatitud() / 1E6));
			tempBuildingLocation.setLongitude((double) (buildingList.get(i)
					.getLongitud() / 1E6));
			distance = touchedLocation.distanceTo(tempBuildingLocation);
			if (distance < prev_distance) {
				prev_distance = distance;
				nearestBuildingIndex=i;
			}
			i++;
		}
		System.out.println(String.valueOf(nearestBuildingIndex));
		return nearestBuildingIndex;
	}
}
