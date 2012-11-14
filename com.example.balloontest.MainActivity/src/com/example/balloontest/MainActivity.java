package com.example.balloontest;

import java.util.List;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MainActivity extends MapActivity {

	public static final int UN_CENTER_LATITUDE = 4636761;
	public static final int UN_CENTER_LONGITUDE = -74083450;
	public static final int UN_RECT_BOUNDING_N = -74094501;
	public static final int UN_RECT_BOUNDING_E = 4631543;
	public static final int UN_RECT_BOUNDING_W = 4644974;
	public static final int UN_RECT_BOUNDING_S = -74079201;
	public static final int UN_BASE_ZOOM = 17;

	MapView unMap;
	List<Overlay> unMapOverlayList;
	BitmapOverlay buildingsOverlay;
	Bitmap buildingsImage;
	protected GeoPoint baseLocation;
	MapController unMapController;
	GeoPoint boundRectTopLeft;
	GeoPoint boundRectBottomRight;
	CustomTouchInputOverlay touchOverlay;
	MyLocationOverlay userPositionOverlay;
	int buttonView = Menu.FIRST;
	int buttonShow = Menu.FIRST + 1;
	private int group1Id = 1;
	ImageButton searchBuildingButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Assigning map to layout
		unMap = (MapView) findViewById(R.id.mapViewMain);
		// Obtain the existing (default) map overlays
		unMapOverlayList = unMap.getOverlays();
		unMap.setBuiltInZoomControls(true);

		// --Setting up the map
		// Set the center and zoom of the map to show the complete extension of
		// UN
		// for that purpose a map controller must be created
		unMapController = unMap.getController();
		// we set the center
		baseLocation = new GeoPoint(UN_CENTER_LATITUDE, UN_CENTER_LONGITUDE);
		// the map is animated to be in the correct location and zoom
		unMapController.animateTo(baseLocation);
		unMapController.setZoom(UN_BASE_ZOOM);


		// --Create a bitmap overlay that will contain the buildings--
		// First we get the image from the resources
		Resources res = getResources();
		buildingsImage = BitmapFactory
				.decodeResource(res, R.drawable.unmaptest);
		// We set the geopoints that indicate the top left and bottom right
		// corner of the desired containing rectangle area,
		// since this overlay is not intended to change its position static
		// points are sent
		boundRectTopLeft = new GeoPoint(UN_RECT_BOUNDING_W, UN_RECT_BOUNDING_N);
		boundRectBottomRight = new GeoPoint(UN_RECT_BOUNDING_E,
				UN_RECT_BOUNDING_S);
		buildingsOverlay = new BitmapOverlay(buildingsImage, boundRectTopLeft,
				boundRectBottomRight);
		// Once the bitmap overlay is set we add it to the overlay list
		unMapOverlayList.add(buildingsOverlay);

		//--Test input overlay
		touchOverlay = new CustomTouchInputOverlay(unMap);
		unMapOverlayList.add(touchOverlay);

		//--Create user location tracking overlay
		userPositionOverlay = new MyLocationOverlay(MainActivity.this, unMap);
		unMapOverlayList.add(userPositionOverlay);

		//--Setting up the search button
		searchBuildingButton = (ImageButton) findViewById(R.id.building_search_button);
		searchBuildingButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(MainActivity.this,
						"ImageButton (selector) is clicked!",
						Toast.LENGTH_SHORT).show();
			}
		});

		//Calculate location between 2 geopoints (TEST!!!)
		float[] distance;
//		Location.distanceBetween((float)(boundRectTopLeft.getLatitudeE6()/1E6), (float)(boundRectTopLeft.getLongitudeE6()/1E6),
//				(float)(boundRectBottomRight.getLatitudeE6()/1E6), (float)(boundRectBottomRight.getLongitudeE6()/1E6), distance);
//		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		menu.add(group1Id, buttonView, buttonView, "Cambiar Vista");
		menu.add(group1Id, buttonShow, buttonShow, "Mostrar UN");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 1:

			if (unMap.isSatellite()) {
				Toast.makeText(MainActivity.this, "Vista Normal",
						Toast.LENGTH_LONG).show();
				unMap.setSatellite(false);
				unMap.invalidate();
			} else {
				Toast.makeText(MainActivity.this, "Vista de Satelite",
						Toast.LENGTH_LONG).show();
				unMap.setSatellite(true);
				unMap.invalidate();
			}
			return true;
		case 2:
			if (buildingsOverlay.isVisible()) {
				Toast.makeText(MainActivity.this,
						"Extension del mapa desactivada", Toast.LENGTH_LONG)
						.show();
				buildingsOverlay.toggleVisibility();
				unMap.invalidate();
			} else {
				Toast.makeText(MainActivity.this,
						"Extension del mapa activada", Toast.LENGTH_LONG)
						.show();
				buildingsOverlay.toggleVisibility();
				unMap.invalidate();
			}
			return true;

		default:
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		userPositionOverlay.disableMyLocation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userPositionOverlay.enableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class MyApplication extends Application {

		private String someVariable;

		public String getSomeVariable() {
			return someVariable;
		}

		public void setSomeVariable(String someVariable) {
			this.someVariable = someVariable;
		}
	}

}