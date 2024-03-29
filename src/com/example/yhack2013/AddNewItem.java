package com.example.yhack2013;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class AddNewItem extends Activity {
	private int iDay = 0, iMonth = 0, iYear = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final Intent intent = getIntent();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_item);
		// init the dates
		
		final Calendar c = Calendar.getInstance();
		iYear = c.get(Calendar.YEAR);
		iMonth = c.get(Calendar.MONTH);
		iDay = c.get(Calendar.DAY_OF_MONTH);
		final EditText textItemName = (EditText) findViewById(R.id.textItemName);
		final EditText editTextDueDate = (EditText) findViewById(R.id.editTextDueDate);
		final EditText editTextDesc = (EditText) findViewById(R.id.textDesc);
		final RatingBar rbImportance = (RatingBar) findViewById(R.id.ratingBar);
		
		editTextDueDate.setText(iMonth + "/" + iDay + "/" + iYear);
		editTextDueDate.setKeyListener(null);
		// add new button inits
		Button btnAddNewItem = (Button) findViewById(R.id.btnAddNew);		
		
		btnAddNewItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int iImportance = (int) rbImportance.getRating();
				String sItemName = textItemName.getText().toString();	
				String sDesc = editTextDesc.getText().toString();

				if (!sItemName.isEmpty()) {
					JSONArray top = null;
					
					try {
						File dir = Environment.getExternalStorageDirectory();
						File file = new File(dir, "todo.json");
	
						// if file exists read json from file
						if (file.exists()) {
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
							top = new JSONArray(jString); 
							
							}
						else {
							// creating json
							top = new JSONArray();
						}
						
						// compute new id
						int iNewId = 0;
						if (top.length() > 0 ) {
							JSONObject lastobj = top.getJSONObject(top.length()-1);
							iNewId = lastobj.getInt("Id")+1;
						}
						
						// adding new info the json
						JSONObject json = new JSONObject();
						json.put("Id", iNewId);
							// new item's id will be the last item's id + 1
						json.put("ItemName", sItemName);
						json.put("Importance", iImportance);
						json.put("Desc", sDesc);
						json.put("Day", iDay);
						json.put("Month", iMonth);
						json.put("Year", iYear);
						
						top.put(iNewId, json);
						
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
					
					try {
						String sOutput = top.toString();
						String filename = "todo.json";
						File file = new File(Environment.getExternalStorageDirectory().toString(), filename);
						FileOutputStream fos;
	
						fos = new FileOutputStream(file);
						fos.write(sOutput.getBytes());
						fos.flush();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					// show toast
					Toast.makeText(
						AddNewItem.this,
						"\"" + textItemName.getText().toString() + "\" has been added to your list",
						Toast.LENGTH_SHORT
					).show();
					// reset fields
					textItemName.setText("");
					editTextDesc.setText("");
					rbImportance.setRating(0);
					
					iYear = c.get(Calendar.YEAR);
					iMonth = c.get(Calendar.MONTH);
					iDay = c.get(Calendar.DAY_OF_MONTH);
					editTextDueDate.setText(iMonth + "/" + iDay + "/" + iYear);		

					
					finish();
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
				}
				else {
					Toast.makeText(AddNewItem.this, "Task Name cannot be empty", Toast.LENGTH_LONG).show();
				}
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_item, menu);
		return true;
	}
	
	// used to show the date picker dialog to pick dates
	public void showDatePickerDialog(View v) {
		 DialogFragment newFragment = new DatePickerFragment(){
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				iDay = day;
				iMonth = month;
				iYear = year;
			EditText textItemName = (EditText) findViewById(R.id.editTextDueDate);
			textItemName.setText(iMonth + "/" + iDay + "/" + iYear);
			}
		};
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
	    View view = getCurrentFocus();
	    boolean ret = super.dispatchTouchEvent(event);

	    if (view instanceof EditText) {
	        View w = getCurrentFocus();
	        int scrcoords[] = new int[2];
	        w.getLocationOnScreen(scrcoords);
	        float x = event.getRawX() + w.getLeft() - scrcoords[0];
	        float y = event.getRawY() + w.getTop() - scrcoords[1];
	        
	        if (event.getAction() == MotionEvent.ACTION_UP 
	 && (x < w.getLeft() || x >= w.getRight() 
	 || y < w.getTop() || y > w.getBottom()) ) { 
	            InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	        }
	    }
	 return ret;
	}
	
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
}
