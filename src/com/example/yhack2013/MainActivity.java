package com.example.yhack2013;

import java.util.ArrayList;

import com.jake.quiltview.QuiltView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	public QuiltView quiltView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		quiltView = (QuiltView) findViewById(R.id.quilt);
		quiltView.setChildPadding(5);
		addTestQuilts(100);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	public void addTestQuilts(int num){
		ArrayList<View> notes = new ArrayList<View>();
		for(int i = 0; i < num; i++){
			ToDoWidget note = new ToDoWidget(this);
			if(i % 5 == 0)
				note.setBackgroundColor(0xFFEDF393);//(R.drawable.mayer);
			else if (i%5 == 1)
				note.setBackgroundColor(0xFFF5E665);//(R.drawable.mayer);
			else if (i%5 == 2)
				note.setBackgroundColor(0xFFFFC472);//(R.drawable.mayer);
			else if (i%5 == 3)
				note.setBackgroundColor(0xFFFFA891);//(R.drawable.mayer);
			else 
				note.setBackgroundColor(0xFF89BABE);//image.setImageResource(R.drawable.mayer1);
			
			note.setContent("FOO", "BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR ", "1 day");
			notes.add(note);
		}
		quiltView.addPatchViews(notes);
	}
}
