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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jake.quiltview.QuiltView;
import com.jake.quiltview.QuiltViewPatch;

public class MainActivity extends Activity {
	public QuiltView quiltView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		quiltView = (QuiltView) findViewById(R.id.quilt);
        String FILENAME = "todo.json", jsonstring;
        StringBuilder sb = new StringBuilder();
        BufferedReader bs;
        InputStream fis;
        final Intent addItemIntent = new Intent(getBaseContext(), AddNewItem.class);
        JoySort js = new JoySort();
        ArrayList<ToDoList> todolists = new ArrayList<ToDoList>();

        ArrayList<String> ints;
        // ints.add("hello");
        /*
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
        */
        
        JSONArray top = null;
		try {
			File dir = Environment.getExternalStorageDirectory();
			File file = new File(dir, "todo.json");
			FileInputStream stream = new FileInputStream(file);
			String jString = null;
			 FileChannel fc = stream.getChannel();
			 MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			 /* Instead of using default, pass in a decoder. */
			jString = Charset.defaultCharset().decode(bb).toString();
			stream.close();
			top = new JSONArray(jString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //jsonstring = sb.toString();
        
        try {
        	// JSONObject jsonObjMain = new JSONObject(jsonstring);
        	JSONArray jsonArray = top;
        	
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
                
        		String desc = jsonObj.getString("Desc");
        		String title = jsonObj.getString("ItemName");

        		todolists.add(new ToDoList(id, importance, duedate, title, desc));
        	}
        }
        catch (Exception e) {
        	System.out.println("Hellol");
        }
        
        ArrayList<ToDoList> PrioritizedToDoLists = js.sort(todolists);
        ArrayList<View> widgets = new ArrayList<View>();
        ArrayList<QuiltViewPatch.Size> sizes = new ArrayList<QuiltViewPatch.Size>();

        for (int i = 0; i < PrioritizedToDoLists.size(); i++) {
        	ToDoList curList = PrioritizedToDoLists.get(i);
        	
        	ToDoWidget newTodoWidget = new ToDoWidget(this);
        	
        	int iDaysLeft = curList.duedate - Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        	
        	newTodoWidget.setContent(curList.title, curList.description, iDaysLeft, curList.id);
        	widgets.add(newTodoWidget);
        	
			if(i % 5 == 0)
				newTodoWidget.setBackgroundColor(0xFFEDF393);//(R.drawable.mayer);
			else if (i%5 == 1)
				newTodoWidget.setBackgroundColor(0xFFF5E665);//(R.drawable.mayer);
			else if (i%5 == 2)
				newTodoWidget.setBackgroundColor(0xFFFFC472);//(R.drawable.mayer);
			else if (i%5 == 3)
				newTodoWidget.setBackgroundColor(0xFFFFA891);//(R.drawable.mayer);
			else 
				newTodoWidget.setBackgroundColor(0xFF89BABE);//image.setImageResource(R.drawable.mayer1);
			
        	if(PrioritizedToDoLists.get(i).priority == 1){
        		sizes.add(QuiltViewPatch.Size.Huge);
        	}
        	else if(PrioritizedToDoLists.get(i).priority == 2){
        		sizes.add(QuiltViewPatch.Size.Big);
        	}
        	else if(PrioritizedToDoLists.get(i).priority == 3){
        		sizes.add(QuiltViewPatch.Size.Tall);
        	}
        	else if(PrioritizedToDoLists.get(i).priority == 4){
        		sizes.add(QuiltViewPatch.Size.Wide);
        	}
        	else if(PrioritizedToDoLists.get(i).priority == 5){
        		sizes.add(QuiltViewPatch.Size.Small);
        	}
        }
        quiltView.addPatchYViews(widgets,sizes);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.ic_add_item:
	        	startActivity(new Intent(getBaseContext(), AddNewItem.class));
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
}
