package com.henrik.bak.restaurantguide;

import com.henrik.bak.restaurantguide.R;
import com.henrik.bak.restaurantguide.RestaurantListActivity.RestaurantAdapter;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ReviewsList extends ListActivity {
	
	RestaurantAdapter adapter=null;
	RestaurantHelper helper=null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		helper=new RestaurantHelper(this);
	}

	
	
	static class ReviewHolder {
		private TextView name=null;
		private TextView address=null;
		private ImageView icon=null;
		
		ReviewHolder(View row) {
			name=(TextView)row.findViewById(R.id.row_title);
			address=(TextView)row.findViewById(R.id.address);
			icon=(ImageView)row.findViewById(R.id.icon);
		}
		
		void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getResName(c));
			address.setText(helper.getResAddress(c));
			icon.setImageResource(R.drawable.ball_green);

		}
	}
}
