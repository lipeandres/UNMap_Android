package com.example.balloontest;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class CustomMapView extends MapView {

	public static final int UN_CENTER_LATITUDE = 4636761;
	public static final int UN_CENTER_LONGITUDE = -74083450;

	public CustomMapView(Context context, String apiKey) {
		super(context, apiKey);

	}

	public CustomMapView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CustomMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	public void dispatchDraw(Canvas canvas) {

		// limit zoom level
		if (getZoomLevel() < 15) {
			getController().setZoom(15);
			getController().setCenter(new GeoPoint(UN_CENTER_LATITUDE, UN_CENTER_LONGITUDE));
			return;
		}
		super.dispatchDraw(canvas);
	}

}
