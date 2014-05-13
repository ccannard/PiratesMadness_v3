package fr.upem.piratesmadness;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameArea extends SurfaceView implements SurfaceHolder.Callback{
	BattleGround battle;
	Thread workerThread;
	SurfaceHolder holder;
	
	public GameArea(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = getHolder();
		final MainActivity activity = (MainActivity) getContext();
		Bundle extras = activity.getIntent().getExtras();
		if(extras==null){
			extras = new Bundle();
		}
		extras.putInt("mode", 1);
		extras.putString("pirate1_drawable", "pirate1");
		extras.putString("pirate2_drawable", "pirate2");
		extras.putString("file_map", "1");
		activity.getIntent().putExtras(extras);
		workerThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!Thread.interrupted()){
//					//Initialisation du terrain
					Canvas canvas = holder.lockCanvas();
//					battle = BattleGround.init("1", activity, canvas);
					holder.unlockCanvasAndPost(canvas);
					//Boucle de mise à jour des données (pirates)
					while(true){
						canvas = holder.lockCanvas();
//						canvas.drawARGB(255, 0, 255, 0);
						holder.unlockCanvasAndPost(canvas);
					}
					//Rien. Thread terminé.
				}
			}
		});
		holder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("PiratesMadness", "surfacecreated");
		workerThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		workerThread.interrupt();
		workerThread = null;
	}

}
