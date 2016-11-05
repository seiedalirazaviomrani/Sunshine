package com.ali_razavi.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements OnTaskCompleted {


    private ArrayAdapter<String> forecastAdapter;

    public ForecastFragment() {
    }


    /**
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        * Display menu in fragment
        * */
        setHasOptionsMenu(true);
    }

    /**
     * load Menu in ForecastFragment
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_fragment, menu);

    }

    /**
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        * save selected item's ID in a variable
        * */
        int id = item.getItemId();

        if (id == R.id.actoin_refresh) {
            updateWeather();
            return true;
        }
        if (id == R.id.preferred_location) {
            openPreferredLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * updateWeather information based on user input in setting preferences
    * */
    private void updateWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String savedLocation = sp.getString(getString(R.string.location_setting_key), getString(R.string.location_default_value));
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(this, getContext());
        fetchWeatherTask.execute(savedLocation);
    }

    /*
    * open user's preferred location in an map app
    * if user's device not have map app a Toast message show up in app.
    * */
    private void openPreferredLocationInMap() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String savedLocation = sp.getString(getString(R.string.location_setting_key), getString(R.string.location_default_value));
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", savedLocation)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), getString(R.string.not_found_map_app), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when the Fragment is visible to the user.
     */
    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listItemsView = inflater.inflate(R.layout.fragment_main, container, false);

        /*
        * Create ArrayAdapter with empty ArrayList
        * */
        forecastAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, new ArrayList<String>());

        /*
        * Find ListView and attach forecastAdapter to it.
        * */
        final ListView weatherList = (ListView) listItemsView.findViewById(R.id.listview_forecast);
        weatherList.setAdapter(forecastAdapter);

        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = weatherList.getItemAtPosition(position).toString();

                /*
                * Start DetailActivity and pass selectedItem to DetailActivity
                * */
                Intent i = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, selectedItem);
                startActivity(i);
            }
        });

        return listItemsView;
    }

    @Override
    public void onTaskCompleted(String[] results) {
        forecastAdapter.clear();
        for (String dayForecast : results) {
            forecastAdapter.add(dayForecast);
        }
    }

}