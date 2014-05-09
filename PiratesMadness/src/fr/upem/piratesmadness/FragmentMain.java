package fr.upem.piratesmadness;

import java.io.ObjectInputStream.GetField;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class FragmentMain extends Fragment{

	public FragmentMain() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		Bundle extras = this.getActivity().getIntent().getExtras();
//
//		ImageButton bSound = (ImageButton) this.getActivity().findViewById(R.id.main_menu_sound);
//		Log.d("PiratesMadness", "button bSound : "+bSound.getId());
//		if(savedInstanceState!=null && savedInstanceState.getBoolean("sound"))bSound.setImageResource(R.drawable.sound_on);
//		else bSound.setImageResource(R.drawable.sound_off);
		setButtonOnListener(extras, v);
		//		media = MediaPlayer.create(this, R.raw.neantitude_robin);
		//		media.start();
		//		media.setLooping(true);
		return v;
	}

	private void setButtonOnListener(final Bundle savedInstanceState, View v){
		final Activity main = this.getActivity();
		final FragmentManager fm = main.getFragmentManager();
		Button bPlay = (Button) v.findViewById(R.id.main_menu_play);
		bPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fm.beginTransaction().add(android.R.id.content, new FragmentGame()).commit();  
			}
		});
		Button bSettings = (Button) v.findViewById(R.id.main_menu_settings);
		bSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "coucou", Toast.LENGTH_SHORT).show();
				fm.beginTransaction().add(android.R.id.content, new FragmentSettings()).commit();  
			}
		});
		Button bScore = (Button) v.findViewById(R.id.main_menu_score_board);
		Log.d("PiratesMadness", "button bScore : "+bScore.getId());
		bScore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fm.beginTransaction().add(android.R.id.content, new FragmentScoreBoard()).commit();  
			}
		});
		final ImageButton bSound = (ImageButton) v.findViewById(R.id.main_menu_sound);
		bScore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				savedInstanceState.putBoolean("sound", !savedInstanceState.getBoolean("sound"));
				if(savedInstanceState.getBoolean("sound"))bSound.setImageResource(R.drawable.sound_on);
				else bSound.setImageResource(R.drawable.sound_off);
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setButtonOnListener(getActivity().getIntent().getExtras(), this.getView());
	}
}
