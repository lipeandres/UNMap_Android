package com.example.mapstest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class MainActivity extends MapActivity implements LocationListener{

	MapView unMap;
	long start;
	long stop;
	boolean moved;
	MyLocationOverlay compass;
	MapController controller;
	int x, y;
	GeoPoint touchedPoint;
	Drawable pin,locationPin;
	List<Overlay> overlayList;
	LocationManager lm;
	String towers;
	int currentLat;
	int currentLong;
	GeoPoint topLeft;
	GeoPoint bottomRight;
	int top_left;
	int bottom_right;
	Bitmap bmp;
	SimpleItemizedOverlay ballonTest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		unMap = (MapView) findViewById(R.id.mapViewMain);
		//unMap = (TapControlledMapView) findViewById(R.id.mapViewMain);
		unMap.setBuiltInZoomControls(true);
		locationPin = getResources().getDrawable(R.drawable.location_icon);
		ballonTest = new SimpleItemizedOverlay(locationPin, unMap);
		Touchy t = new Touchy();
		BuildingOverlay b=new BuildingOverlay();
		overlayList = unMap.getOverlays();
		overlayList.add(t);
		overlayList.add(b);
		compass = new MyLocationOverlay(MainActivity.this, unMap);
		overlayList.add(compass);
		controller = unMap.getController();
		GeoPoint point = new GeoPoint(4636761, -74083450);
		topLeft=new GeoPoint(4644974, -74094501);
		bottomRight = new GeoPoint(4631543, -74079201);
		controller.animateTo(point);
		controller.setZoom(17);
		GeoPoint ing = new GeoPoint(4638451,-74082602);
		pin = getResources().getDrawable(R.drawable.smiley_icon);
		OverlayItem ingpin = new OverlayItem(ing,"Edificio 411", "Laboratorios de Ingenieria");
		ballonTest.addOverlay(ingpin);
		overlayList.add(ballonTest);
		
		//Current Position
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		towers = lm.getBestProvider(crit, false);
		
		Location currentLoc =lm.getLastKnownLocation(towers);
		if(currentLoc!=null){
		currentLat=(int) currentLoc.getLatitude();
		currentLong=(int) currentLoc.getLongitude();
		GeoPoint currentLocation = new GeoPoint(currentLat, currentLong);
		OverlayItem overlayItem = new OverlayItem(touchedPoint, "Hola", "2");
		CustomPinpoint cpinpoint=new CustomPinpoint(locationPin,MainActivity.this);
		cpinpoint.insertPinpoints(overlayItem);	
		overlayList.add(cpinpoint);
		}
		else{
			Toast.makeText(MainActivity.this, "No se pudo obtener la posicion", Toast.LENGTH_LONG).show();
		}
		
		Resources res = getResources();
		bmp = BitmapFactory.decodeResource(res, R.drawable.unmaptest);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		compass.disableCompass();
		lm.removeUpdates(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		compass.enableCompass();
		lm.requestLocationUpdates(towers, 500, 1, this);
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touchy extends Overlay {
		public boolean onTouchEvent(MotionEvent e, MapView m) {
			if (e.getAction() == MotionEvent.ACTION_MOVE) {
				start = 0;
				stop = 0;
				moved = true;
			}
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				moved = false;
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = unMap.getProjection().fromPixels(x, y);
			}
			if (e.getAction() == MotionEvent.ACTION_UP && !moved) {
				stop = e.getEventTime();
				moved = false;
			}
			if (stop - start > 150) {
				AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
						.create();
				alert.setTitle("Elija una opcion");
				// alert.setMessage("");
				alert.setButton("ponga pin",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
							OverlayItem overlayItem = new OverlayItem(touchedPoint, "Hola", "2");	
							CustomPinpoint cpinpoint=new CustomPinpoint(pin,MainActivity.this);
							cpinpoint.insertPinpoints(overlayItem);	
							overlayList.add(cpinpoint);	
							}
						});
				alert.setButton2("obtener direccion",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Geocoder geocoder = new Geocoder(
										getBaseContext(), Locale.getDefault());
								try {
									List<Address> address = geocoder.getFromLocation(
											touchedPoint.getLatitudeE6() / 1E6,
											touchedPoint.getLongitudeE6() / 1E6,
											1);
									if (address.size() > 0) {
										String display = "";
										for (int i = 0; i < address.get(0)
												.getMaxAddressLineIndex(); i++) {
											display += address.get(0)
													.getAddressLine(i) + "\n";
										}
										Toast t = Toast.makeText(
												getBaseContext(), display,
												Toast.LENGTH_LONG);
										t.show();
									}
								} catch (IOException e) {
									e.printStackTrace();
								} finally {

								}
							}
						});
				alert.setButton3("Vista",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
							if(unMap.isSatellite()){
								unMap.setSatellite(false);
								unMap.setStreetView(true);
							}
							else{
								unMap.setStreetView(false);	
								unMap.setSatellite(true);							
							}

							}
						});
				alert.show();
				return true;
			}

			return false;
		}
	}
	
	class BuildingOverlay extends Overlay {
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	        if(shadow){
	       return;
	    }

	    super.draw(canvas, mapView, shadow);

	    // convert bitmap's bounding box into pixels 
	        Point top_left = new Point(); 
			mapView.getProjection().toPixels(topLeft, top_left); 
	        Point bottom_right = new Point(); 
	        mapView.getProjection().toPixels(bottomRight, bottom_right); 
	        // Prepare two rectangles (pixels) 
	        Rect src = new Rect( 0,0,bmp.getWidth() - 1, bmp.getHeight() - 1 ); 
	        Rect dst = new Rect( top_left.x, top_left.y, bottom_right.x,bottom_right.y ); 

	        // draw bitmap 
	        canvas.drawBitmap(bmp, src, dst, null); 
	}
	}

	public void onLocationChanged(Location l) {
		currentLat=(int) (l.getLatitude()*1E6);
		currentLong=(int) (l.getLongitude()*1E6);
		GeoPoint currentLocation = new GeoPoint(currentLat, currentLong);
		OverlayItem overlayItem = new OverlayItem(touchedPoint, "Hola", "2");	
		CustomPinpoint cpinpoint=new CustomPinpoint(locationPin,MainActivity.this);
		cpinpoint.insertPinpoints(overlayItem);	
		overlayList.add(cpinpoint);
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
