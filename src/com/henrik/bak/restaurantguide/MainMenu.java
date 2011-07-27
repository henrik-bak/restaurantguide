package com.henrik.bak.restaurantguide;

import com.henrik.bak.restaurantguide.R;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainMenu extends Activity {
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);
	}
	
	public void ShowList(View view) {
		Intent i=new Intent(MainMenu.this, RestaurantListActivity.class);
		startActivity(i);
	 }
	
	public void Search(View view) {
		Intent i = new Intent(MainMenu.this, Search.class);
		startActivity(i);
	}
	
	public void Searchlist(View view) {
		Intent i = new Intent(MainMenu.this, SearchResults.class);
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.main_options, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.main_help) {
			return(true);
		}
		else if (item.getItemId()==R.id.main_about) {
			return(true);
		}

		return(super.onOptionsItemSelected(item));
	}

}
