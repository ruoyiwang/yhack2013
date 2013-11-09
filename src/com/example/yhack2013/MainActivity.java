package com.example.yhack2013;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

<<<<<<< Updated upstream
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
=======
import com.jake.quiltview.QuiltView;
import com.jake.quiltview.QuiltViewPatch;
>>>>>>> Stashed changes

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
		addTestQuilts(20);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	public void addTestQuilts(int num){
		ArrayList<View> notes = new ArrayList<View>();
		ArrayList<QuiltViewPatch.Size> notesizes = new ArrayList<QuiltViewPatch.Size>();
		for(int i = 0; i < num; i++){
			ToDoWidget note = new ToDoWidget(this);
			if(i % 7 == 0){
				note.setBackgroundColor(0xFFEDF393);//(R.drawable.mayer);
				notesizes.add(QuiltViewPatch.Size.Huge);
			}else if (i%7 == 1){
				note.setBackgroundColor(0xFFF5E665);//(R.drawable.mayer);
				notesizes.add(QuiltViewPatch.Size.Big);
			}else if (i%7 == 2){
				note.setBackgroundColor(0xFFFFC472);//(R.drawable.mayer);
				notesizes.add(QuiltViewPatch.Size.Small);
			}else if (i%7 == 3){
				note.setBackgroundColor(0xFFFFA891);//(R.drawable.mayer);
				notesizes.add(QuiltViewPatch.Size.Tall);
			}else if (i%7 == 4){
				note.setBackgroundColor(0xFFFFC472);//(R.drawable.mayer);
				notesizes.add(QuiltViewPatch.Size.Small);
			}else if (i%7 == 5){
				note.setBackgroundColor(0xFFFFA891);//(R.drawable.mayer);
				notesizes.add(QuiltViewPatch.Size.Tall);
			}else{ 
				note.setBackgroundColor(0xFF89BABE);//image.setImageResource(R.drawable.mayer1);
				notesizes.add(QuiltViewPatch.Size.Wide);
			}
			note.setContent("FOO", "BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR BAR ", "1 day");
			notes.add(note);
		}
		quiltView.addPatchYViews(notes,notesizes);
	}
}
