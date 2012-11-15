package com.example.balloontest;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {

	public static final int UN_CENTER_LATITUDE = 4636761;
	public static final int UN_CENTER_LONGITUDE = -74083450;
	public static final int UN_RECT_BOUNDING_N = -74094501;
	public static final int UN_RECT_BOUNDING_E = 4631543;
	public static final int UN_RECT_BOUNDING_W = 4644974;
	public static final int UN_RECT_BOUNDING_S = -74079201;
	public static final int UN_BASE_ZOOM = 17;
	private DBHelper buildingDB;

	CustomMapView unMap;
	List<Overlay> unMapOverlayList;
	BitmapOverlay buildingsOverlay;
	BitmapOverlay roadsOverlay;
	BitmapOverlay pedestrianOverlay;
	Bitmap buildingsImage;
	Bitmap roadsImage;
	Bitmap pedestrianImage;
	protected GeoPoint baseLocation;
	MapController unMapController;
	GeoPoint boundRectTopLeft;
	GeoPoint boundRectBottomRight;
	CustomTouchInputOverlay touchOverlay;
	MyLocationOverlay userPositionOverlay;
	int toggleView = Menu.FIRST;
	int toggleBuildingOverlay = Menu.FIRST + 1;
	int toggleRoadOverlay = Menu.FIRST + 2;
	int togglePedestrianOverlay = Menu.FIRST + 3;
	private int group1Id = 1;
	ImageButton searchBuildingButton;
	ItemizedTextOverlay buildingTextOverlay;
	Drawable textMarker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Assigning map to layout
		unMap = (CustomMapView) findViewById(R.id.mapViewMain);
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

		// --Create a bitmap overlay that will contain the pedestrianPaths--
		// First we get the image from the resources
		Resources res = getResources();
		pedestrianImage = BitmapFactory.decodeResource(res,
				R.drawable.pedestrian_overlay);
		// We set the geopoints that indicate the top left and bottom right
		// corner of the desired containing rectangle area,
		// since this overlay is not intended to change its position static
		// points are sent
		boundRectTopLeft = new GeoPoint(UN_RECT_BOUNDING_W, UN_RECT_BOUNDING_N);
		boundRectBottomRight = new GeoPoint(UN_RECT_BOUNDING_E,
				UN_RECT_BOUNDING_S);
		pedestrianOverlay = new BitmapOverlay(pedestrianImage,
				boundRectTopLeft, boundRectBottomRight);
		// Once the bitmap overlay is set we add it to the overlay list
		unMapOverlayList.add(pedestrianOverlay);

		// --Create a bitmap overlay that will contain the roads overlay--
		roadsImage = BitmapFactory.decodeResource(res, R.drawable.road_overlay);
		roadsOverlay = new BitmapOverlay(roadsImage, boundRectTopLeft,
				boundRectBottomRight);
		// Once the bitmap overlay is set we add it to the overlay list
		unMapOverlayList.add(roadsOverlay);

		// --Create a bitmap overlay that will contain the roads buildings--
		buildingsImage = BitmapFactory.decodeResource(res,
				R.drawable.building_overlay);
		buildingsOverlay = new BitmapOverlay(buildingsImage, boundRectTopLeft,
				boundRectBottomRight);
		// Once the bitmap overlay is set we add it to the overlay list
		unMapOverlayList.add(buildingsOverlay);


		// --Create user location tracking overlay
		userPositionOverlay = new MyLocationOverlay(MainActivity.this, unMap);
		unMapOverlayList.add(userPositionOverlay);

		// --Setting up the search button
		searchBuildingButton = (ImageButton) findViewById(R.id.building_search_button);
		searchBuildingButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(MainActivity.this,
						"ImageButton (selector) is clicked!",
						Toast.LENGTH_SHORT).show();
			}
		});

		// --Obtain the building list and information from the database
		buildingDB = new DBHelper(MainActivity.this);
		buildingDB.open();
		ArrayList<Building> buildingList = new ArrayList<Building>();
		buildingList = (ArrayList<Building>) buildingDB.getBuildings();
		// Since the DB is static we can close it now
		buildingDB.close();

		// --Testing text overlay
		textMarker = (Drawable) res.getDrawable(R.drawable.bluemarker);
		buildingTextOverlay = new ItemizedTextOverlay(textMarker,
				MainActivity.this, 13);
		int i = 0;

		while (i < buildingList.size()) {
			buildingTextOverlay.addItem(new OverlayItem(new GeoPoint(
					buildingList.get(i).getLatitud(), buildingList.get(i)
							.getLongitud()), buildingList.get(i).getName(),
					String.valueOf(buildingList.get(i).getNumber())));
			System.out.println(buildingList.get(i).getLatitud());
			i++;
			
		}

		unMapOverlayList.add(buildingTextOverlay);
		
		// --Test input overlay
		touchOverlay = new CustomTouchInputOverlay(unMap,buildingList);
		unMapOverlayList.add(touchOverlay);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		menu.add(group1Id, toggleView, toggleView, "Cambiar Vista");
		menu.add(group1Id, toggleBuildingOverlay, toggleBuildingOverlay, "Edificios");
		menu.add(group1Id, toggleRoadOverlay,toggleRoadOverlay, "Rutas Vehiculares");
		menu.add(group1Id, togglePedestrianOverlay, togglePedestrianOverlay, "Caminos Peatonales");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 1:

			if (unMap.isSatellite()) {
				Toast.makeText(MainActivity.this, "Vista Normal",
						Toast.LENGTH_SHORT).show();
				unMap.setSatellite(false);
				unMap.invalidate();
			} else {
				Toast.makeText(MainActivity.this, "Vista de Satelite",
						Toast.LENGTH_SHORT).show();
				unMap.setSatellite(true);
				unMap.invalidate();
			}
			return true;
		case 2:
			if (buildingsOverlay.isVisible()) {
				Toast.makeText(MainActivity.this,
						"Capa de edificios desactivada", Toast.LENGTH_SHORT)
						.show();
				buildingsOverlay.toggleVisibility();
				unMap.invalidate();
			} else {
				Toast.makeText(MainActivity.this,
						"Capa de edificios activada", Toast.LENGTH_SHORT)
						.show();
				buildingsOverlay.toggleVisibility();
				unMap.invalidate();
			}
			return true;
		case 3:
			if (roadsOverlay.isVisible()) {
				Toast.makeText(MainActivity.this,
						"Capa de rutas vehiculares desactivada", Toast.LENGTH_SHORT)
						.show();
				roadsOverlay.toggleVisibility();
				unMap.invalidate();
			} else {
				Toast.makeText(MainActivity.this,
						"Capa de rutas vehiculares activada", Toast.LENGTH_SHORT)
						.show();
				roadsOverlay.toggleVisibility();
				unMap.invalidate();
			}
			return true;
		case 4:
			if (pedestrianOverlay.isVisible()) {
				Toast.makeText(MainActivity.this,
						"Capa de caminos peatonales desactivada", Toast.LENGTH_SHORT)
						.show();
				pedestrianOverlay.toggleVisibility();
				unMap.invalidate();
			} else {
				Toast.makeText(MainActivity.this,
						"Capa de caminos peatonales activada", Toast.LENGTH_SHORT)
						.show();
				pedestrianOverlay.toggleVisibility();
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

}