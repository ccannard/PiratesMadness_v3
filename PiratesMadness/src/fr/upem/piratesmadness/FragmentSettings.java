package fr.upem.piratesmadness;

import java.io.IOException;
import java.util.Scanner;

import android.app.Fragment;
import android.util.Log;

public class FragmentSettings extends Fragment {
	
	private CharacterItem[] createListOfCharacter(){
		Scanner scan;
		CharacterItem[] listofCharacter;
		try {
			scan = new Scanner(this.getActivity().getAssets().open("character_name"));
		if(!scan.hasNext()){
			Log.e("PiratesMadness - PersonnageActivity - createListOfCharacter", "le fichier est incorrect");
			System.exit(-1);
		}
		//Modifier la suite : créer une liste et stocker chacun des CharacterItem.
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
