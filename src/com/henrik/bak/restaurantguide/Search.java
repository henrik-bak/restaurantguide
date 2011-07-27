package com.henrik.bak.restaurantguide;

import com.henrik.bak.restaurantguide.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Search extends Activity {
	
	private EditText name=null;
	private EditText address=null;
	private EditText phone=null;
	private EditText web=null;
	private EditText details=null;
	public static int[] ID_ARRAY;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchscreen);
	}
	
	public void GetParams(View view) {

		String[] params = new String[5];
		
		name = (EditText)findViewById(R.id.search_name);
		address = (EditText)findViewById(R.id.search_addr);
		phone = (EditText)findViewById(R.id.search_phone);
		web = (EditText)findViewById(R.id.search_web);
		details = (EditText)findViewById(R.id.search_details);
		
		if (name.getText().toString().length()>0) params[0] = name.getText().toString();
		if (address.getText().toString().length()>0) params[1] = address.getText().toString();
		if (phone.getText().toString().length()>0) params[2] = phone.getText().toString();
		if (web.getText().toString().length()>0) params[3] = web.getText().toString();
		if (details.getText().toString().length()>0) params[4] = details.getText().toString();
				
		SearchResults.searchparams=params;
		
		Intent i=new Intent(Search.this, SearchResults.class);
		startActivity(i);
		
	}

}
