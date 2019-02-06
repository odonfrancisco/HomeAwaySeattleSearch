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
import android.widget.EditText;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private void useRetrofit(String query){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FoursquareApi foursquareApi = retrofit.create(FoursquareApi.class);

        Call<ResponseBody> call = foursquareApi.getPlaces("23",
                Constants.seattleCenterllString, query);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, String.valueOf(response.code()));
                    Log.d(TAG, String.valueOf(response));
                    return;
                }

                Log.d(TAG, response.body().toString());
                try {
//                    response.body().close();

                    JSONArray resultsArr = new JSONObject(response.body().string())
                            .getJSONObject("response")
                            .getJSONArray("venues");

                    Log.d(TAG, resultsArr.toString());

//                    Whats the right way to do this? Should I return something to the original
//                            call of useRetrofit() and then call showResults() or
//                            is what Im currently doing ok?
                    showResults(parseData(resultsArr));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void showResults(ArrayList<Place> results){
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), results);

        resultsList.setAdapter(adapter);
        resultsList.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<Place> parseData(JSONArray results ){

        ArrayList<Place> mPlaceList = new ArrayList<>();
        parcelablePlaceList = new ArrayList<>();

        for(int i=0; i<results.length(); i++){
            // Should I be putting all the code inside the try block or should
            // I only have new JSONObject inside it?

//            Am I doing this correctly? Seems vaguely similar to what I was doing before
            GsonBuilder gsonbuilder = new GsonBuilder();
            gsonbuilder.registerTypeAdapter(Place.class, new PlaceDeserializer());
            Gson gson = gsonbuilder.create();

            Place place = null;

            try {
                place = gson.fromJson(results.get(i).toString(), Place.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//
//                JSONObject currentObj = new JSONObject(results.get(i).toString());
//                JSONObject location = currentObj.getJSONObject("location");
//                JSONObject category = currentObj.getJSONArray("categories")
//                        .getJSONObject(0);
//
//                String id = currentObj.getString("id");
//                String name = currentObj.getString("name");
//                String categoryName = category.getString("name");
//                String address = location.getJSONArray("formattedAddress")
//                        .getString(0);
//                double lat = location.getDouble("lat");
//                double lng = location.getDouble("lng");
//
//                String iconPrefix = category.getJSONObject("icon").getString("prefix");
//                String iconSuffix = category.getJSONObject("icon").getString("suffix");
//                String iconString = iconPrefix + "bg_88" + iconSuffix;
//                Bitmap icon = imageDownloader.execute(iconString).get();
////                System.out.println("URI " + icon);
//
//                int distanceInMeters = location.getInt("distance");
//                String distance = new DecimalFormat("#.##")
//                        .format(distanceInMeters/1609.344);
//                boolean favorited = false;
//
//                Place currentPlace = new Place(id, name, categoryName,
//                        address, lat, lng, icon, distance, favorited);
//
//                System.out.println("CURRENTOBJ");
//                System.out.println(currentObj);

            mPlaceList.add(place);
            Log.d(TAG, gson.toJson(place));

            parcelablePlaceList.add(Parcels.wrap(place));
            Log.d(TAG, gson.toJson(Parcels.unwrap(Parcels.wrap(place))));

        }
        return mPlaceList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
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
        useRetrofit(query);
//        What does this return do?
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length() == 0){
            floatingActionButton.hide();
        } else {
            floatingActionButton.show();
            useRetrofit(newText);
        }

//        same
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void viewResultsMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);

        intent.putParcelableArrayListExtra("placesArray", parcelablePlaceList);

        Place onePlace = Parcels.unwrap(parcelablePlaceList.get(0));

        Log.d(TAG, String.valueOf(onePlace.getLat()));

        startActivity(intent);

    }


}
