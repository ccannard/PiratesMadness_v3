package fr.upem.piratesmadness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BattleGroundInitializer extends
AsyncTask<View, String, BattleGround> {
	volatile MainActivity activity;

	public BattleGroundInitializer(MainActivity act) {
		activity = act;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 * Params[0] : name of file
	 * Params[1] : drawable pirate1
	 * Params[2] : drawable pirate2
	 */
	@Override
	protected BattleGround doInBackground(View... v) {
		try {
			if(isCancelled()){
				return null;
			}
//			int i=0;
//			Log.d("PiratesMadness","Test debug"+i);i++;
			publishProgress(activity.getString(R.string.init_progress1));
			BattleGround bg = new BattleGround();
			bg.obstacles = new ArrayList<Rect>();
			bg.difficulty = activity.getIntent().getExtras().getInt("mode");
			int height = 0;
			SparseArray<ArrayList<Integer>> map = new SparseArray<ArrayList<Integer>>();
			ArrayList<Pirate> pirates = new ArrayList<Pirate>();
			Point pirate1 = new Point();
			Point pirate2 = new Point();
			Log.d("PiratesMadness","activity : "+activity);
//			Log.d("PiratesMadness","Test debug"+i);i++;
			Scanner s = new Scanner(activity.getAssets().open(activity.getIntent().getExtras().getString("file_map")));
			boolean firstCircle = true;
			String line;
//			Log.d("PiratesMadness","Test debug"+i);i++;
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
				if(isCancelled()){
					return null;
				}
			}
//			Log.d("PiratesMadness","Test debug"+i);i++;
			publishProgress(activity.getString(R.string.init_progress2));
			bg.height = height;
			if (bg.width == 0 || height == 0)
				throw new IllegalStateException();
			float new_width;
			float new_height;
//			Log.d("PiratesMadness","Test debug"+i);i++;
			if(bg.isLandscape = bg.width>height)
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			else
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			if(isCancelled()){
				return null;
			}
//			Log.d("PiratesMadness","Test debug"+i);i++;
			publishProgress(activity.getString(R.string.init_progress3));
			//Ne peut pas inflat� un surfaceview a cause de la m�thode Looper.myLooper() qui est appell�e par le Handler.
//			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			LinearLayout fragment_game = (LinearLayout)layoutInflater.inflate(R.layout.fragment_game, null);
//			GameArea surfaceView = (GameArea) fragment_game.findViewById(R.id.fr_upem_piratesmadness_fragment_game_surfaceview);
			Log.d("PiratesMadness",
					"vue values; width:"+(float)v[0].getWidth() +"; height:"+(float)v[0].getHeight());
			Log.d("PiratesMadness",
					"vue "+v[0]);
			new_width = (float)v[0].getWidth() / (float)(bg.width);
			new_height = (float)v[0].getHeight() / (float)(height);
			bg.texture = rescaledBitmap(v[0], new_width, new_height, R.drawable.wall);

//			Log.d("PiratesMadness","Test debug"+i);i++;
			activity.getResources().getIdentifier(activity.getIntent().getExtras().getString("pirate1_drawable"), "drawable", activity.getPackageName());

//			Log.d("PiratesMadness","Test debug"+i);i++;
			pirates.add(
					new Pirate(
							new Point((int)(pirate1.x*new_width),
									(int)(pirate1.y*new_height)),
									activity,
									0,
									rescaledBitmap(v[0],
											new_width,
											new_height,
											activity.getResources().getIdentifier(activity.getIntent().getExtras().getString("pirate1_drawable"), "drawable", activity.getPackageName())
									)
							)
					);
//			Log.d("PiratesMadness","Test debug"+i);i++;
			pirates.add(
					new Pirate(
							new Point((int)(pirate2.x*new_width),
									(int)(pirate2.y*new_height)),
									activity,
									0,
									rescaledBitmap(v[0],
											new_width,
											new_height,
											activity.getResources().getIdentifier(activity.getIntent().getExtras().getString("pirate2_drawable"), "drawable", activity.getPackageName())
									)
							)
					);
//			Log.d("PiratesMadness","Test debug"+i);i++;
			bg.arrayPirates = pirates;
			if(isCancelled()){
				return null;
			}
//			Log.d("PiratesMadness","Test debug"+i);i++;
			publishProgress(activity.getString(R.string.init_progress4));
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

	private Bitmap rescaledBitmap(View ga, float new_width, float new_height, int drawable){
		Bitmap basic = BitmapFactory.decodeResource(ga.getResources(),
				drawable);
		Matrix matrix = new Matrix();
		Log.d("PiratesManess", "division; width:"+new_width / basic.getWidth()+"; height:"+new_height/basic.getHeight());
		matrix.postScale(new_width/basic.getWidth(),
			new_height/basic.getHeight());
			Log.d("PiratesMadness","width:"+basic.getWidth()+", height:"+basic.getHeight());
		return Bitmap.createBitmap(basic, 0, 0, basic.getWidth(),
				basic.getHeight(), matrix, true);
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		LinearLayout v = (LinearLayout)((LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fragment_initializer, null);
		TextView tx = (TextView) v.findViewById(R.id.fr_upem_piratesmadness_BattleGroundInitialize_textview);
		CharSequence tmp = tx.getText();
		if(tmp==null){
			tx.setText(values[values.length-1]);
		}else{
			tx.setText(tmp+values[values.length-1]);
		}
	}

	@Override
	protected void onPostExecute(final BattleGround result) {
		if(activity!=null){
			activity.asyncTask=null;
		}
		if(result==null){
			System.exit(-1);
		}
		super.onPostExecute(result);
		activity.runOnUiThread(new Runnable(){
			
			public void run() {
				FragmentManager fm = activity.getFragmentManager();
				FragmentGame fg = new FragmentGame();
				fg.battle = result;
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.layout.fragment_game, fg);
				ft.addToBackStack(null);
				ft.commit();}
		});
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		onPostExecute(null);
	}
	
	@Override
	protected void onCancelled(BattleGround result) {
		super.onCancelled(result);
		onPostExecute(result);
	}
}
