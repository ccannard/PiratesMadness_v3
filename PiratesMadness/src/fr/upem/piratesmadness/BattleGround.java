package fr.upem.piratesmadness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class BattleGround {
	ArrayList<Rect> obstacles;
	Bitmap texture;
	MediaPlayer sound;
	//Useful : indicates if the map is more higher than wider
	boolean isLandscape;
	int width;
	int height;
	int difficulty;
	ArrayList<Pirate> arrayPirates;

	public static BattleGround init(String file, Activity activity, Canvas canvas){
		try{
			BattleGround bg = new BattleGround();
			bg.obstacles = new ArrayList<Rect>();
			bg.difficulty = activity.getIntent().getExtras().getInt("mode");
			int height = 0;
			SparseArray<ArrayList<Integer>> map = new SparseArray<ArrayList<Integer>>();
			ArrayList<Pirate> pirates = new ArrayList<Pirate>();
			Point pirate1 = new Point();
			Point pirate2 = new Point();
			//		Log.d("PiratesMadness","Test debug"+i);i++;
			Scanner s = new Scanner(activity.getAssets().open(file));
			boolean firstCircle = true;
			String line;
			//		Log.d("PiratesMadness","Test debug"+i);i++;
			while (s.hasNextLine()) {
				line = s.nextLine();
				int x = 0;
				for (char c : line.toCharArray()) { //while pour perf
					ArrayList<Integer> current = map.get(height,
							new ArrayList<Integer>());
					if (c == 'x'){
						current.add(x);
					}else if(c == '1' || c == '2'){ // A modifier pour plus de joueurs
						if(Integer.parseInt(Character.toString(c))==1)
							pirate1 = new Point(x, height);
						else
							pirate2 = new Point(x, height);
					}
					map.put(height, current);
					x++;
				}
				height++;
				if (firstCircle)
					bg.width = x;
				firstCircle = false;
			}
			//		Log.d("PiratesMadness","Test debug"+i);i++;
			bg.height = height;
			if (bg.width == 0 || height == 0)
				throw new IllegalStateException();
			float new_width;
			float new_height;
			//		Log.d("PiratesMadness","Test debug"+i);i++;
			if(bg.isLandscape = bg.width>height)
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			else
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			//		Log.d("PiratesMadness","Test debug"+i);i++;
			//Ne peut pas inflaté un surfaceview a cause de la méthode Looper.myLooper() qui est appellée par le Handler.
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout fragment_game = (LinearLayout)layoutInflater.inflate(R.layout.fragment_game, null);
			GameArea surfaceView = (GameArea) fragment_game.findViewById(R.id.fr_upem_piratesmadness_fragment_game_surfaceview);
			new_width = (float)canvas.getWidth() / (float)(bg.width);
			new_height = (float)canvas.getHeight() / (float)(height);
			Log.d("PiratesMadness","Value : height"+new_height+"; width:"+new_width);
			bg.texture = rescaledBitmap(fragment_game, new_width, new_height, R.drawable.wall);

			//		Log.d("PiratesMadness","Test debug"+i);i++;
			activity.getResources().getIdentifier(activity.getIntent().getExtras().getString("pirate1_drawable"), "drawable", activity.getPackageName());

			//		Log.d("PiratesMadness","Test debug"+i);i++;
			pirates.add(
					new Pirate(
							new Point((int)(pirate1.x*new_width),
									(int)(pirate1.y*new_height)),
									activity,
									0,
									rescaledBitmap(surfaceView,
											new_width,
											new_height,
											activity.getResources().getIdentifier(activity.getIntent().getExtras().getString("pirate1_drawable"), "drawable", activity.getPackageName())
											)
							)
					);
			//		Log.d("PiratesMadness","Test debug"+i);i++;
			pirates.add(
					new Pirate(
							new Point((int)(pirate2.x*new_width),
									(int)(pirate2.y*new_height)),
									activity,
									0,
									rescaledBitmap(surfaceView,
											new_width,
											new_height,
											activity.getResources().getIdentifier(activity.getIntent().getExtras().getString("pirate2_drawable"), "drawable", activity.getPackageName())
											)
							)
					);
			//		Log.d("PiratesMadness","Test debug"+i);i++;
			bg.arrayPirates = pirates;
			//		Log.d("PiratesMadness","Test debug"+i);i++;
			return bg;
		} catch (IOException ise) {
			Log.e("pirate", "Can't parse level file!");
			ise.printStackTrace();
			System.exit(-1);
		}
		Log.e("pirate", "Can't create BattleGround! Abord activity!");
		System.exit(-1);
		return null;
	}

	private static Bitmap rescaledBitmap(View v,float new_width, float new_height, int drawable){	
		Bitmap basic = BitmapFactory.decodeResource(v.getResources(),
				drawable);
		Matrix matrix = new Matrix();
		matrix.postScale(new_width / basic.getWidth(),
				new_height / basic.getHeight());
		return Bitmap.createBitmap(basic, 0, 0, basic.getWidth(),
				basic.getHeight(), matrix, true);
	}	

}
