package com.henrik.bak.restaurantguide;

import com.henrik.bak.restaurantguide.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class RestaurantListActivity extends ListActivity {
	public final static String ID_EXTRA="com.henrik.bak.restaurantguide._ID";
	boolean search=false;
	Cursor model=null;
	RestaurantAdapter adapter=null;
	RestaurantHelper helper=null;
	SharedPreferences prefs=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		helper=new RestaurantHelper(this);
		
		try{
			
			helper.createDataBase();
			helper.openDataBase();
			
			} catch (Exception e)
			{
				
			}
			
		prefs=PreferenceManager.getDefaultSharedPreferences(this);
		initList();
		prefs.registerOnSharedPreferenceChangeListener(prefListener);
		registerForContextMenu(getListView());
	}
	

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	@Override
	public void onListItemClick(ListView list, View view,
															int position, long id) {
		Intent i=new Intent(RestaurantListActivity.this, Details.class);

		i.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.add) {
			startActivity(new Intent(RestaurantListActivity.this, EditForm.class));
			
			return(true);
		}
		else if (item.getItemId()==R.id.prefs) {
			startActivity(new Intent(this, EditPreferences.class));
		
			return(true);
		}

		return(super.onOptionsItemSelected(item));
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    		super.onCreateContextMenu(menu, v, menuInfo);
    		MenuInflater mi = getMenuInflater();
        	mi.inflate(R.menu.list_menu_item_longpress, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menu_delete:
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo)
    			item.getMenuInfo();
    			helper.deleteRestaurantById(String.valueOf(info.id));
    			initList();
    		return true;
    	case R.id.menu_edit:
    		AdapterContextMenuInfo info2 = (AdapterContextMenuInfo)
			item.getMenuInfo();
    		Intent i=new Intent(RestaurantListActivity.this, EditForm.class);
    		i.putExtra(ID_EXTRA, String.valueOf(info2.id));
    		startActivity(i);
    		return true;
    	}
    	
    	
    	
    	return super.onContextItemSelected(item);
    }
	
	private void initList() {
		if (model!=null) {
			stopManagingCursor(model);
			model.close();
		}
		
		model=helper.getAllRestaurants(prefs.getString("sort_order", "name"));
		startManagingCursor(model);
		adapter=new RestaurantAdapter(model);
		setListAdapter(adapter);
	}
	
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener=
	 new SharedPreferences.OnSharedPreferenceChangeListener() {
		public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
																					String key) {
			if (key.equals("sort_order")) {
				initList();
			}
		}
	};
	
	class RestaurantAdapter extends CursorAdapter {
		RestaurantAdapter(Cursor c) {
			super(RestaurantListActivity.this, c);
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