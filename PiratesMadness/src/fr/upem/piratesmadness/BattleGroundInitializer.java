package fr.upem.piratesmadness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

public class BattleGroundInitializer extends
AsyncTask<Activity, String, BattleGround> {

	@Override
	protected BattleGround doInBackground(Activity... params) {
		try {
			Activity daddy = params[0];
			publishProgress(daddy.getString(R.string.init_progress1));
			BattleGround bg = new BattleGround();
			bg.obstacles = new ArrayList<Rect>();
			bg.difficulty = daddy.getIntent().getExtras().getInt("mode");
			int height = 0;
			SparseArray<ArrayList<Integer>> map = new SparseArray<ArrayList<Integer>>();
			ArrayList<Pirate> pirates = new ArrayList<Pirate>();
			Point pirate1 = new Point();
			Point pirate2 = new Point();
			Scanner s = new Scanner(daddy.getAssets().open(daddy.getIntent().getExtras().getString("level")));
			boolean firstCircle = true;
			String line;
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
			publishProgress(daddy.getString(R.string.init_progress2));
			bg.height = height;
			if (bg.width == 0 || height == 0)
				throw new IllegalStateException();
			float new_width;
			float new_height;
			if(bg.isLandscape = bg.width>height)
				daddy.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			else
				daddy.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			publishProgress(daddy.getString(R.string.init_progress3));
			View v = (View)daddy.findViewById(R.layout.fragment_game);
			new_width = (float)v.getWidth() / (float)(bg.width);
			new_height = (float)v.getHeight() / (float)(height);
			bg.texture = rescaledBitmap(v, new_width, new_height, R.drawable.wall);
			
			daddy.getResources().getIdentifier(daddy.getIntent().getExtras().getString("pirate2_drawable"), "drawable", daddy.getPackageName());
			pirates.add(
					new Pirate(
							new Point((int)(pirate1.x*new_width),
									(int)(pirate1.y*new_height)),
									daddy,
									0,
									rescaledBitmap(v,
											new_width,
											new_height,
											daddy.getResources().getIdentifier(daddy.getIntent().getExtras().getString("pirate1_drawable"), "drawable", daddy.getPackageName())
									)
							)
					);
			pirates.add(
					new Pirate(
							new Point((int)(pirate2.x*new_width),
									(int)(pirate2.y*new_height)),
									daddy,
									0,
									rescaledBitmap(v,
											new_width,
											new_height,
											daddy.getResources().getIdentifier(daddy.getIntent().getExtras().getString("pirate2_drawable"), "drawable", daddy.getPackageName())
									)
							)
					);
			bg.arrayPirates = pirates;
			publishProgress(daddy.getString(R.string.init_progress4));
			return bg;
		} catch (IOException ise) {
			Log.e("pirate", "Can't parse level file!");
			System.exit(-1);
		}
		Log.e("pirate", "Can't create BattleGround! Abord activity!");
		System.exit(-1);
		return null;

	}

	private Bitmap rescaledBitmap(View ga, float new_width, float new_height, int drawable){
		Bitmap basic = BitmapFactory.decodeResource(ga.getResources(),
				drawable);
		Matrix matrix = new Matrix();
		matrix.postScale(new_width / basic.getWidth(),
			new_height / basic.getHeight());
		return Bitmap.createBitmap(basic, 0, 0, basic.getWidth(),
				basic.getHeight(), matrix, true);
	}

}
