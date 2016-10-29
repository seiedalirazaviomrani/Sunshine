package com.ali_razavi.sunshine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

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
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
            fetchWeatherTask.execute("Mashahd");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listItemsView = inflater.inflate(R.layout.fragment_main, container, false);

        /*
        * Create fake data for Sunshine app
        * */
        String forecastArray[] = {
                "Today - Sunny - 24/35",
                "Tomorrow - Foggy - 17/21",
                "Weds - Rainy - 18/22",
                "Thurs - Sunny - 26/28",
                "Fri - Sunny - 27/29",
                "Sat - Rainy - 19/22",
                "Sun - Rainy - 20/24"
        };

        /*
        * Create ArrayAdapter from string fake data.
        * */
        ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, forecastArray);

        /*
        * Find ListView and attach forecastAdapter to it.
        * */
        ListView forecastList = (ListView) listItemsView.findViewById(R.id.listview_forecast);
        forecastList.setAdapter(forecastAdapter);

        return listItemsView;
    }

    /*
    * My API code:
    * http://api.openweathermap.org/data/2.5/forecast/daily?q=Mashhad&mode=json&units=metric&cnt=7&APPID=8ae76d3dfb9411b936a9575bf4179888
    *
    * */
}
