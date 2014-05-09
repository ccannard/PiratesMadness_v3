package fr.upem.piratesmadness;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public class Pirate {
	Point coordinate;
	int life;
	String name;
	Bitmap texture;
	int speed;
	Direction gravity;
	boolean noGravity;
	Direction direction;
	final Rect padBuffer;
	Activity ga;
	
	public Pirate(Point initialCoordinate, Activity activity, int id, Bitmap face) {
		View v = (View)activity.findViewById(R.layout.fragment_game);
		this.texture = face;
		this.ga = activity;
		coordinate = initialCoordinate;
		final int middleX;
		final int middleY;
		if(v.getWidth()<v.getHeight()){
			middleX = v.getWidth();
			middleY = v.getHeight()/2;
		}else{
			middleX = v.getWidth()/2;
			middleY = v.getHeight();
		}
		this.padBuffer = new Rect(0, 0, middleX, middleY);
	}
	
	public Rect getPiratePadBuffer(){
		switch(ga.getIntent().getExtras().getInt("mode")){
			case 2 : return this.padBuffer;
			default : return new Rect(coordinate.x+50, coordinate.y-50, coordinate.x-50, coordinate.y+50);
		}
	}

	public Rect getPirateBuffer() {
		Rect area = new Rect(coordinate.x+(texture.getWidth()/2),coordinate.y-(texture.getHeight()/2), coordinate.x-(texture.getWidth()/2), coordinate.y+(texture.getHeight()/2));
		return area;
	}
}
