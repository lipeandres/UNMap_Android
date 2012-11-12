package com.example.balloontest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class BitmapOverlay extends Overlay {
	private Bitmap image;
	private GeoPoint topLeft;
	private GeoPoint bottomRight;
	
    public BitmapOverlay(Bitmap _image,GeoPoint _topLeft,GeoPoint _bottomRight){
    image=_image;
    topLeft=_topLeft;
    bottomRight=_bottomRight;
    }
    
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
        Rect src = new Rect( 0,0,image.getWidth() - 1, image.getHeight() - 1 ); 
        Rect dst = new Rect( top_left.x, top_left.y, bottom_right.x,bottom_right.y ); 

        // draw bitmap 
        canvas.drawBitmap(image, src, dst, null); 
}
}