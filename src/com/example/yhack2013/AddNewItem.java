package com.example.yhack2013;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AddNewItem extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_item, menu);
		return true;
	}

}
