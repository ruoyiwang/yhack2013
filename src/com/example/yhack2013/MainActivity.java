package com.example.yhack2013;

import java.util.ArrayList;

import com.jake.quiltview.QuiltView;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
<<<<<<< HEAD
=======
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
>>>>>>> 732fde9c524963de8561ae2ce7952fc117ff22c6

public class MainActivity extends Activity {
	public QuiltView quiltView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD

		quiltView = (QuiltView) findViewById(R.id.quilt);
		quiltView.setChildPadding(5);
		addTestQuilts(100);
=======
        String FILENAME = "todo.json", jsonstring;
        StringBuilder sb = new StringBuilder();
        BufferedReader bs;
        InputStream fis;
        final Intent addItemIntent = new Intent(getBaseContext(), AddNewItem.class);
        JoySort js = new JoySort();
        ArrayList<ToDoList> todolists = new ArrayList<ToDoList>();
        Button btnAddNewItem = (Button) findViewById(R.id.btnCreateTask);
        
        btnAddNewItem.setOnClickListener(new OnClickListener() {
        	   @Override
        	   public void onClick(View v) {
        		   startActivity(addItemIntent);
        	   }
        });

        ArrayList<String> ints;
        ints.add("hello");
        
        try {
            fis = new FileInputStream(FILENAME);
            bs = new BufferedReader(new InputStreamReader(fis, ""));
            String temp;
            
            while ((temp = bs.readLine()) != null) {
            	sb.append(temp);
            }
        }
        catch (Exception e) {
        	System.out.println("Hello");
        }
        
        jsonstring = sb.toString();
        
        try {
        	JSONObject jsonObjMain = new JSONObject(jsonstring);
        	JSONArray jsonArray = jsonObjMain.getJSONArray("lists");
        	
        	for (int i = 0; i < jsonArray.length(); i++) {

        		// Creating JSONObject from JSONArray
        		JSONObject jsonObj = jsonArray.getJSONObject(i);

        		// Getting data from individual JSONObject
        		int id = jsonObj.getInt("id");
        		int importance = jsonObj.getInt("importance");
        		int duedate = jsonObj.getInt("duedate");
        		String desc = jsonObj.getString("desc");

        		todolists.add(new ToDoList(id, importance, duedate, desc));
        	}
        }
        catch (Exception e) {
        	System.out.println("Hello");
        }


        ArrayList<Shape> shapes = JoySort.sort(todolists);
        ArrayList<ToDoWidget> widgets = new ArrayList<ToDoWidget>();

        for (int i = 0; i < shapes.length(); i++) {
        	widgets.add(new ToDoWidget(shapes.get(i), todolists.get(i)));
        }
>>>>>>> 732fde9c524963de8561ae2ce7952fc117ff22c6
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
<<<<<<< HEAD

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
=======
>>>>>>> 732fde9c524963de8561ae2ce7952fc117ff22c6
}
