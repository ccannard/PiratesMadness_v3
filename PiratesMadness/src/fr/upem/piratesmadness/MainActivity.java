package fr.upem.piratesmadness;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {
	BattleGroundInitializer asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        this.getFragmentManager().beginTransaction().add(android.R.id.content, new FragmentMain()).commit();  
    }

}
