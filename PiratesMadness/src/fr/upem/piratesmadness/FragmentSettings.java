package fr.upem.piratesmadness;

import java.io.IOException;
import java.util.Scanner;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class FragmentSettings extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_settings, null);
		setButtonOnListener(getActivity().getIntent().getExtras(), view);
		return view;
	}
	
	private void setButtonOnListener(final Bundle savedInstanceState, View v){
		final FragmentManager fm = getFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		Button bPersonnage = (Button) v.findViewById(R.id.choose_your_character);
		bPersonnage.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
//				Intent character = new Intent(getApplication(), PersonnageActivity.class);
//				character.putExtras(saveParam(savedInstanceState));
//				startActivity(character);
			}
		});
		Button bMap = (Button) v.findViewById(R.id.choose_your_map);
		bMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent map = new Intent(getApplication(), MapActivity.class);
//				map.putExtras(saveParam(savedInstanceState));
//				startActivity(map);
			}
		});
		Button bPlay = (Button) v.findViewById(R.id.button_play);
		bPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ft.replace(android.R.id.content, new FragmentGame());
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		Button bMenu = (Button) v.findViewById(R.id.button_menu);
		bMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("PiratesMadness", "vue fragment menu");
				ft.replace(android.R.id.content, new FragmentMain());
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		Button bScore = (Button) v.findViewById(R.id.button_score_board);
		bScore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ft.replace(android.R.id.content, new FragmentScoreBoard());
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		//utilis� saveParam pour ajouter les donn�es
		saveParam(outState, getView());
	}
	
	public Bundle saveParam(Bundle data, View v){
		if(data==null){
			data = new Bundle();
		}
		EditText edit1 = (EditText)v.findViewById(R.id.name_player1);
		String text = edit1.getText().toString();
		//tester si le text vaut null lorsque l'utilisateur ne renseigne rien
		if(text.length()==0){
			text = new String("Player1");
		}
		Log.d("PiratesMadness - SettingsActivity", "text player1 vaut : "+text);
		data.putString("namePlayerOne", text);

		EditText edit2 = (EditText)v.findViewById(R.id.name_player2);
		String text2 = edit2.getText().toString();
		//tester si le text vaut null lorsque l'utilisateur ne renseigne rien
		if(text2.length()==0){
			text2 = new String("Player2");
		}
		Log.d("PiratesMadness - SettingsActivity", "text player2 vaut : "+text2);
		data.putString("namePlayerTwo", text2);

		RadioButton modeEasy = (RadioButton) v.findViewById(R.id.easy_mode);
		String textMode=null;
		if(modeEasy.isChecked()){
			textMode = "easy";
		}else{
			textMode = "hard";
		}
		Log.d("PiratesMadness","mode : "+((modeEasy.isChecked()==true)?"easy":"hard"));
		data.putString("mode", textMode);

		RadioButton soundOn = (RadioButton) v.findViewById(R.id.button_sound_on);
		boolean sound=false;
		if(soundOn.isChecked()){
			sound=true;
		}
		Log.d("PiratesMadness","sound : "+sound);
		data.putBoolean("sound", sound);
		return data;
	}
	
	private CharacterItem[] createListOfCharacter(){
		Scanner scan;
		CharacterItem[] listofCharacter;
		try {
			scan = new Scanner(this.getActivity().getAssets().open("character_name"));
		if(!scan.hasNext()){
			Log.e("PiratesMadness - PersonnageActivity - createListOfCharacter", "le fichier est incorrect");
			System.exit(-1);
		}
		//Modifier la suite : cr�er une liste et stocker chacun des CharacterItem.
		// Puis retransformer cette liste en tableau.
		int size = Integer.parseInt(scan.nextLine().trim());
		listofCharacter = new CharacterItem[size];
		int i=0;
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			int id = getResources().getIdentifier(line.substring(0, line.indexOf(" ")), "drawable", this.getActivity().getPackageName());
			String name = line.substring(line.indexOf(" "));	
			listofCharacter[i] = new CharacterItem(id, name);
			i++;
		}
		scan.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("PiratesMadness - PersonnageActivity - createListOfCharacter", "error with the file");
			e.printStackTrace();
			listofCharacter=null;
		}
		return listofCharacter;
	}

}
