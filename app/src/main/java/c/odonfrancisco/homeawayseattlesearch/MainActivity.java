package c.odonfrancisco.homeawayseattlesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static c.odonfrancisco.homeawayseattlesearch.FoursquareApi.clientId;
import static c.odonfrancisco.homeawayseattlesearch.FoursquareApi.clientSecret;
import static c.odonfrancisco.homeawayseattlesearch.FoursquareApi.v;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView resultsList;
    private Toolbar toolbar_search_input;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Parcelable> parcelablePlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultsList = findViewById(R.id.results_recyclerview);
        toolbar_search_input = findViewById(R.id.search_input);
        floatingActionButton = findViewById(R.id.floating_action_button);

        setSupportActionBar(toolbar_search_input);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void getPlaces(String query) {
        ///move this out of activity
        //look at mvvm or soemthing or mvp
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FoursquareApi foursquareApi = retrofit.create(FoursquareApi.class);

        //use modern callback with rxjava or maybe coroutines
        Call<FSResponse> call = foursquareApi.getPlaces("23",
                Constants.seattleCenterllString, query,
                clientId, clientSecret, v);

        call.enqueue(new Callback<FSResponse>() {
            @Override
            public void onResponse(Call<FSResponse> call, Response<FSResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Place> places = (ArrayList<Place>) response.body().getVenues().getVenues();
                    for (Place place : response.body().getVenues().getVenues()) {
                        Log.d(TAG, "Venue Name: " + place.name);
                    }
                    showResults(places);
                } else {
                    // handle case when not successful response
                    //toast Message
                }
            }

            @Override
            public void onFailure(Call<FSResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void showResults(ArrayList<Place> results) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), results);

        resultsList.setAdapter(adapter);
        resultsList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem mSearchMenuItem = menu.findItem(R.id.menu_toolbar_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setQueryHint("Search Seattle!");
        searchView.setOnQueryTextListener(this);
        Log.d(TAG, "onCreateOptionsMenu: mSearchMenuItem -> " + mSearchMenuItem.getActionView());

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getPlaces(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0) {
            floatingActionButton.hide();
        } else {
            floatingActionButton.show();
            getPlaces(newText);
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { }

    public void viewResultsMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putParcelableArrayListExtra("placesArray", parcelablePlaceList);

        // pass the ArrayList of places from response above.
        Place onePlace = Parcels.unwrap(parcelablePlaceList.get(0));
        Log.d(TAG, String.valueOf(onePlace.getLat()));
        startActivity(intent);
    }
}
