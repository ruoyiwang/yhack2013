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
        
        ArrayList<ToDoList> PrioritizedToDoLists = js.sort(todolists);
        ArrayList<ToDoWidget> widgets = new ArrayList<ToDoWidget>();

        for (int i = 0; i < PrioritizedToDoLists.size(); i++) {
        	ToDoList curList = PrioritizedToDoLists.get(i);
        	
        	ToDoWidget newTodoWidget = new ToDoWidget(getApplicationContext());
        	
        	int iDaysLeft = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - curList.duedate;
        	
        	newTodoWidget.setContent(curList.title, curList.description, iDaysLeft);
        	widgets.add(newTodoWidget);
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean deleteItemFromFile(int id) {
    	try{
    		File dir = Environment.getExternalStorageDirectory();
			File file = new File(dir, "todo.json");
			if (file.exists()) {
				// read file in as json string
				FileInputStream stream = new FileInputStream(file);
				String jString = null;
				try {
					 FileChannel fc = stream.getChannel();
					 MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
					 /* Instead of using default, pass in a decoder. */
					 jString = Charset.defaultCharset().decode(bb).toString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				stream.close();
				JSONArray top = new JSONArray(jString);
				
				// make copy of json array with the item deleted
				JSONArray newTop = new JSONArray();
				for (int i = 0; i < top.length(); i++) {
					if (top.getJSONObject(i).getInt("Id") != id) {
						newTop.put(top.getJSONObject(i));
					}
				}
				
				// write the new JSONArray to file
				try {
					String sOutput = top.toString();
					FileOutputStream fos;

					fos = new FileOutputStream(file);
					fos.write(sOutput.getBytes());
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return true;
			}
			else {
				return false;
			}
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
}
