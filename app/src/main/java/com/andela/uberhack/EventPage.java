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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andela.uberhack.models.Calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class EventPage extends Activity {
    List<String> list;
    String[] taxiTypes;

    ListView eventList;
    protected LocationManager locationManager;
    private Location location;
    Double longitude;
    Double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        taxiTypes = new String[]{"UBER X", "UBER BLACK", "UBER SUN"};
        eventList = (ListView) findViewById(R.id.events);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(EventPage.this);
                dialog.setTitle("Event");
                dialog.setContentView(R.layout.event_details);
                Calendar calendar = Vars.calendars[position];
                ((TextView) dialog.findViewById(R.id.summary)).setText(calendar.summary);
                ((TextView) dialog.findViewById(R.id.destination)).setText(calendar.location);
                ((TextView) dialog.findViewById(R.id.status)).setText(calendar.status);
                try {
                    ((TextView) dialog.findViewById(R.id.start_time)).setText(Vars.dateToRelativeString(calendar.start));
                    ((TextView) dialog.findViewById(R.id.end_time)).setText(Vars.dateToRelativeString(calendar.end));
                }
                catch (Exception e) {

                }
                Spinner taxiType = (Spinner) dialog.findViewById(R.id.taxi_type);
                taxiType.setAdapter(new ArrayAdapter<String>(EventPage.this, android.R.layout.simple_spinner_dropdown_item, taxiTypes));

                //getLocation();
                dialog.show();
            }
        });

        eventList.setAdapter(new EventAdapter(this, Vars.calendars));
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
                            Log.v("Locations are ", longitude.toString() + " " + latitude.toString());
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

    public class EventAdapter extends BaseAdapter {
        private Context context;

        private Calendar[] calendars;

        public EventAdapter(Context context, Calendar[] calendars) {
            this.context = context;
            this.calendars = calendars;
        }

        @Override
        public int getCount() {
            return calendars.length;
        }

        @Override
        public Object getItem(int position) {
            return calendars[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            Calendar calendar = calendars[position];
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.event_page_adapter, parent, false);
            } else {
                row = convertView;
            }
            ((TextView) row.findViewById(R.id.summary)).setText(calendar.summary);
            ((TextView) row.findViewById(R.id.location)).setText(calendar.location);
            try {
                ((TextView) row.findViewById(R.id.start)).setText(Vars.dateToRelativeString(calendar.start));
                ((TextView) row.findViewById(R.id.end)).setText(Vars.dateToRelativeString(calendar.end));
            }
            catch (Exception e) {

            }
            return row;
        }
    }
}
