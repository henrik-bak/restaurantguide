package com.henrik.bak.restaurantguide;


import com.henrik.bak.restaurantguide.R;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchResults extends ListActivity {

	Cursor model=null;
	RestaurantAdapter adapter=null;
	RestaurantHelper helper=null;
	public static String[] searchparams;
	
	private CursorAdapter dataSource;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		helper = new RestaurantHelper(this);

		setListAdapter(dataSource);
		initList();

	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	private void initList() {
		if (model!=null) {
			stopManagingCursor(model);
			model.close();
		}
		
		model=helper.getSearchResults(searchparams[0], searchparams[1], searchparams[2], searchparams[3], searchparams[4]);
		startManagingCursor(model);
		adapter=new RestaurantAdapter(model);
		setListAdapter(adapter);
	}
	
	class RestaurantAdapter extends CursorAdapter {
		RestaurantAdapter(Cursor c) {
			super(SearchResults.this, c);
		}
		
		@Override
		public void bindView(View row, Context ctxt,
												 Cursor c) {
			RestaurantHolder holder=(RestaurantHolder)row.getTag();
			
			holder.populateFrom(c, helper);
		}
		
		@Override
		public View newView(Context ctxt, Cursor c,
												 ViewGroup parent) {
			LayoutInflater inflater=getLayoutInflater();
			View row=inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder=new RestaurantHolder(row);
			
			row.setTag(holder);
			
			return(row);
		}
	}
	
	static class RestaurantHolder {
		private TextView name=null;
		private TextView address=null;
		private ImageView icon=null;
		
		RestaurantHolder(View row) {
			name=(TextView)row.findViewById(R.id.row_title);
			address=(TextView)row.findViewById(R.id.address);
			icon=(ImageView)row.findViewById(R.id.icon);		
		}
		
		void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getResName(c).toString());
			address.setText(helper.getResAddress(c));
			icon.setImageResource(R.drawable.ball_green);

		}
	}

}
