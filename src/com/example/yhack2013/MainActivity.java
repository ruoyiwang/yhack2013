package com.example.yhack2013;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jake.quiltview.QuiltView;

public class MainActivity extends Activity {
	public QuiltView quiltView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String FILENAME = "todo.json", jsonstring;
        StringBuilder sb = new StringBuilder();
        BufferedReader bs;
        InputStream fis;
        final Intent addItemIntent = new Intent(getBaseContext(), AddNewItem.class);
        // JoySort js = new JoySort();
        ArrayList<ToDoList> todolists = new ArrayList<ToDoList>();
        Button btnAddNewItem = (Button) findViewById(R.id.btnCreateTask);
        
        btnAddNewItem.setOnClickListener(new OnClickListener() {
        	   @Override
        	   public void onClick(View v) {
        		   startActivity(addItemIntent);
        	   }
        });

        ArrayList<String> ints;
        // ints.add("hello");
        
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
        		int id = jsonObj.getInt("Id");
        		int importance = jsonObj.getInt("Importance");
        		int day = jsonObj.getInt("Day");
        		int month = jsonObj.getInt("Month");
        		int year = jsonObj.getInt("Year");
                Calendar ca1 = Calendar.getInstance();
                ca1.set(year,month,day);
                int duedate = ca1.get(Calendar.DAY_OF_YEAR);
                
        		String desc = jsonObj.getString("desc");
        		String title = jsonObj.getString("ItemName");

        		todolists.add(new ToDoList(id, importance, duedate, title, desc));
        	}
        }
        catch (Exception e) {
        	System.out.println("Hello");
        }


        /*
        ArrayList<Shape> shapes = JoySort.sort(todolists);
        ArrayList<ToDoWidget> widgets = new ArrayList<ToDoWidget>();

        for (int i = 0; i < shapes.length(); i++) {
        	widgets.add(new ToDoWidget(shapes.get(i), todolists.get(i)));
        }
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
