package com.henrik.bak.restaurantguide;

import com.henrik.bak.restaurantguide.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditForm extends Activity {
	EditText name=null;
	EditText address=null;
	EditText phone=null;
	EditText web=null;
	EditText details=null;
	RestaurantHelper helper=null;
	String restaurantId=null;
	TextView location=null;
	LocationManager locMgr=null;
	double latitude=0.0d;
	double longitude=0.0d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_form);
		
		locMgr=(LocationManager)getSystemService(LOCATION_SERVICE);
		helper=new RestaurantHelper(this);
		
		try{
			
		helper.createDataBase();
		helper.openDataBase();
		
		} catch (Exception e)
		{
			
		}
		
		name=(EditText)findViewById(R.id.edit_name);
		address=(EditText)findViewById(R.id.edit_addr);
		phone=(EditText)findViewById(R.id.edit_phone);
		web=(EditText)findViewById(R.id.edit_web);
		details=(EditText)findViewById(R.id.edit_details);
		location=(TextView)findViewById(R.id.edit_location);
		details.setMovementMethod(ScrollingMovementMethod.getInstance());
		restaurantId=getIntent().getStringExtra(RestaurantListActivity.ID_EXTRA);
		
		if (restaurantId!=null) {
			load();
		}
	}
	
	@Override
	public void onPause() {
		save();
		locMgr.removeUpdates(onLocationChange);
		super.onPause();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onDestroy() {
		helper.close();
		locMgr.removeUpdates(onLocationChange);
		
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.edit_options, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (restaurantId==null) {
			menu.findItem(R.id.save_all).setEnabled(false);
			menu.findItem(R.id.save_loc).setEnabled(false);
		}

		return(super.onPrepareOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.save_loc) {
			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
																		0, 0, onLocationChange);
			
			return(true);
		}
		else if (item.getItemId()==R.id.save_all) {
			save();
			
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
		
		location.setText(String.valueOf(latitude)
											+", "
											+String.valueOf(longitude));
		
		c.close();
	}
	
	private void save() {
		if (name.getText().toString().length()>0) {
	
			if (restaurantId==null) {
				helper.insertRestaurant(name.getText().toString(),
											address.getText().toString(), phone.getText().toString(),
											web.getText().toString(),
											details.getText().toString());
			}
			else {
				helper.updateRestaurant(restaurantId, name.getText().toString(),
											address.getText().toString(),  phone.getText().toString(),
											web.getText().toString(),
											details.getText().toString());
			}
		}
	}
	
	LocationListener onLocationChange=new LocationListener() {
		public void onLocationChanged(Location fix) {
			helper.updateLocation(restaurantId, fix.getLatitude(),
														fix.getLongitude());
			location.setText(String.valueOf(fix.getLatitude())
											+", "
											+String.valueOf(fix.getLongitude()));
			locMgr.removeUpdates(onLocationChange);
			
			Toast
				.makeText(EditForm.this, "Location saved",
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