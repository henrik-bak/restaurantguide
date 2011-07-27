package com.henrik.bak.restaurantguide;

import com.henrik.bak.restaurantguide.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends Activity {
	TextView name=null;
	TextView address=null;
	TextView phone=null;
	TextView web=null;
	TextView details = null;
	RestaurantHelper helper=null;
	String restaurantId=null;
	TextView location=null;
	LocationManager locMgr=null;
	double latitude=0.0d;
	double longitude=0.0d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		locMgr=(LocationManager)getSystemService(LOCATION_SERVICE);
		helper=new RestaurantHelper(this);
		
		try{
			
		helper.createDataBase();
		helper.openDataBase();
		
		} catch (Exception e)
		{
			
		}
		
		name=(TextView)findViewById(R.id.details_name);
		address=(TextView)findViewById(R.id.details_address);
		phone=(TextView)findViewById(R.id.details_phone);
		web=(TextView)findViewById(R.id.details_web);
		details=(TextView)findViewById(R.id.details_details);
		
		restaurantId=getIntent().getStringExtra(RestaurantListActivity.ID_EXTRA);
		
		if (restaurantId!=null) {
			load();
		}
	}
	
	@Override
	public void onPause() {		
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		helper.close();
		locMgr.removeUpdates(onLocationChange);
		
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.details_option, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
			menu.findItem(R.id.location).setEnabled(false);

		return(super.onPrepareOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.location) {
			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
																		0, 0, onLocationChange);
			
			return(true);
		}
		else if (item.getItemId()==R.id.map) {
			Intent i=new Intent(this, RestaurantMap.class);
			
			i.putExtra(RestaurantMap.EXTRA_LATITUDE, latitude);
			i.putExtra(RestaurantMap.EXTRA_LONGITUDE, longitude);
			i.putExtra(RestaurantMap.EXTRA_NAME, name.getText().toString());
			
			startActivity(i);
			
			return(true);
		}

		return(super.onOptionsItemSelected(item));
	}
	
	
	private void load() {
		Cursor c=helper.getRestaurantById(restaurantId);

		c.moveToFirst();		
		name.setText(helper.getResName(c));
		address.setText(helper.getResAddress(c));
		phone.setText(helper.getResPhone(c));
		web.setText(helper.getResWeb(c));
		details.setText(helper.getResDetails(c));
		
		
		latitude=helper.getLatitude(c);
		longitude=helper.getLongitude(c);
		
		
		c.close();
	}
	
	LocationListener onLocationChange=new LocationListener() {
		public void onLocationChanged(Location fix) {
			helper.updateLocation(restaurantId, fix.getLatitude(),
														fix.getLongitude());
			locMgr.removeUpdates(onLocationChange);
			
			Toast
				.makeText(Details.this, "Location saved",
									Toast.LENGTH_LONG)
				.show();
		}
		
		public void onProviderDisabled(String provider) {
			// required for interface, not used
		}
		
		public void onProviderEnabled(String provider) {
			// required for interface, not used
		}
		
		public void onStatusChanged(String provider, int status,
																	Bundle extras) {
			// required for interface, not used
		}
	};
}