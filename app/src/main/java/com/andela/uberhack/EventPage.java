package com.andela.uberhack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class EventPage extends Activity {
    List<String> list;
    String[] taxiTypes;
    TextView summary;
    TextView destination;
    TextView organiser;
    TextView status;
    TextView startTime;
    TextView endTime;
    ToggleButton shared;
    Spinner taxiType;
    ListView eventList;
    String locationName;
    protected LocationManager locationManager;
    private Location location;
    Double longitude;
    Double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        eventList = (ListView)findViewById(R.id.events);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(EventPage.this);
                dialog.setContentView(R.layout.event_details);
                summary = (TextView)dialog.findViewById(R.id.summary);
                destination = (TextView)dialog.findViewById(R.id.destination);
                status = (TextView)dialog.findViewById(R.id.status);
                startTime = (TextView)dialog.findViewById(R.id.start_time);
                endTime = (TextView)dialog.findViewById(R.id.end_time);
                taxiType = (Spinner)dialog.findViewById(R.id.taxi_type);
                taxiTypes = new String[]{"UBER X","UBER BLACK","UBER SUN"};
                taxiType.setAdapter(new ArrayAdapter<String>(EventPage.this, android.R.layout.simple_spinner_dropdown_item, taxiTypes));
                getLocation();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Vars.Toaster("Error with Standort", this, 0);
            } else {
                // this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10, (LocationListener) this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                           longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            Log.v("Locations are ", longitude.toString() + " "+latitude.toString() );
//                            setMapLocation(location);
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled && location == null) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, (LocationListener) this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {

//                            setMapLocation(location);
                        }
                    }

                }
            }
        }
    }
    public class EventAdapter extends BaseAdapter{
        private Context context;
        String events[];

        public EventAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return events.length;
        }

        @Override
        public Object getItem(int position) {
            return events[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.event_page_adapter, parent, false);
            }
            else{
                row = convertView;
            }
            TextView summary = (TextView)row.findViewById(R.id.summary);
            TextView location = (TextView)row.findViewById(R.id.location);
            TextView startTime = (TextView)row.findViewById(R.id.start_time);
            return null;
        }
    }
}
