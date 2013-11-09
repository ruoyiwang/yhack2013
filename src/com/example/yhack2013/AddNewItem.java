package com.example.yhack2013;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class AddNewItem extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_item);
		
		Button btnAddNewItem = (Button) findViewById(R.id.btnAddNew);
		final EditText textItemName = (EditText) findViewById(R.id.textItemName);
		final RatingBar rbImportance = (RatingBar) findViewById(R.id.ratingBar);
		
		
		btnAddNewItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int iImportance = (int) rbImportance.getRating();
				String sItemName = textItemName.getText().toString();
				// TODO get date
				
				// creating json
				JSONObject top = new JSONObject();
				JSONObject json = new JSONObject();
				try {
					json.put("id", "1");
					json.put("ItemName", sItemName);
					json.put("Importance", iImportance);
					top.put("top", json);
					// TODO add date
				} catch (JSONException e) {
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
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_item, menu);
		return true;
	}

}
