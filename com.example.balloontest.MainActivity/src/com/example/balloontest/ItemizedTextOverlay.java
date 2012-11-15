package com.example.balloontest;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ItemizedTextOverlay extends ItemizedOverlay<OverlayItem> {
	// member variables
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private int baseTextSize;
	private int mTextSize;
	private boolean noName;

	public ItemizedTextOverlay(Drawable defaultMarker, Context context,
			int textSize) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		mTextSize = textSize;
		baseTextSize= textSize;
		noName=true;
	}

	// In order for the populate() method to read each OverlayItem, it will make
	// a request to createItem(int) define this method to properly read from our ArrayList
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

//	@Override
//	protected boolean onTap(int index) {
//		OverlayItem item = mOverlays.get(index);
//
//		// Do stuff here when you tap, i.e. :
//		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//		dialog.setTitle(item.getTitle());
//		dialog.setMessage(item.getSnippet());
//		dialog.show();
//
//		// return true to indicate we've taken care of it
//		return true;
//	}

	@Override
	public void draw(android.graphics.Canvas canvas, MapView mapView,
			boolean shadow) {
		super.draw(canvas, mapView, shadow);

		if ((shadow == false) && (mapView.getZoomLevel()>=17)) {
			// cycle through all overlays
			if(mapView.getZoomLevel()>=17 && mapView.getZoomLevel()<19){
				mTextSize = mapView.getZoomLevel()-8;
				noName=true;
			}
			else{
				mTextSize = baseTextSize;
				noName=false;
			}

			for (int index = 0; index < mOverlays.size(); index++) {
				OverlayItem item = mOverlays.get(index);

				// Converts lat/lng-Point to coordinates on the screen
				GeoPoint point = item.getPoint();
				Point ptScreenCoord = new Point();
				mapView.getProjection().toPixels(point, ptScreenCoord);

//				// Paint
//				Paint paint = new Paint();
//				paint.setTextAlign(Paint.Align.CENTER);
//				paint.setTextSize(mTextSize);
//				paint.setARGB(255, 255, 255, 255);
//				paint.// alpha, r, g, b (Black, semi
//												// see-through)
				
				Paint strokePaint = new Paint();
			    strokePaint.setARGB(255, 0, 0, 0);
			    strokePaint.setTextAlign(Paint.Align.CENTER);
			    strokePaint.setTextSize(mTextSize);
			    strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
			    strokePaint.setStyle(Paint.Style.STROKE);
			    strokePaint.setStrokeWidth(4);

			    Paint textPaint = new Paint();
			    textPaint.setARGB(255, 255, 255, 255);
			    textPaint.setTextAlign(Paint.Align.CENTER);
			    textPaint.setTextSize(mTextSize);
			    textPaint.setTypeface(Typeface.DEFAULT_BOLD);

				// Print the building name and number
			    if(noName)
			    {
					canvas.drawText(item.getSnippet(), ptScreenCoord.x,
							ptScreenCoord.y + mTextSize, strokePaint);
					canvas.drawText(item.getSnippet(), ptScreenCoord.x,
							ptScreenCoord.y + mTextSize, textPaint);			    	
			    }
			    else
			    {
					canvas.drawText(item.getSnippet()+"-"+item.getTitle(), ptScreenCoord.x,
							ptScreenCoord.y + mTextSize, strokePaint);
					canvas.drawText(item.getSnippet()+"-"+item.getTitle(), ptScreenCoord.x,
							ptScreenCoord.y + mTextSize, textPaint);
			    }
			}
		}
	}

	public void addItem(OverlayItem overlayItem) {
		mOverlays.add(overlayItem);
		populate();
	}

	public void removeItem(OverlayItem overlayItem) {
		mOverlays.remove(overlayItem);
		populate();
	}

	public void clear() {
		mOverlays.clear();
		populate();
	}
}