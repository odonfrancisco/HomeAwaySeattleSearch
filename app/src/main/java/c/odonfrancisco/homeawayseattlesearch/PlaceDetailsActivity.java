package c.odonfrancisco.homeawayseattlesearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.parceler.Parcels;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = PlaceDetailsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Place currentPlace;
    private SharedPreferences sharedPreferences;
    private ImageView starImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolBarLayout);
        sharedPreferences = this.getSharedPreferences("c.odonfrancisco.homeawayseattlesearch", Context.MODE_PRIVATE);

        Gson gson = new Gson();

        currentPlace = Parcels.unwrap(getIntent().getParcelableExtra("currentPlace"));

        Log.d(TAG, gson.toJson(currentPlace));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.placeDetailsMap);
        mapFragment.getMapAsync(this);

        toolbarLayout.setTitle(currentPlace.getName());
        setDetailsText(currentPlace);

//          Should I be using Parcelable instead of gson?
//            yes
//        currentPlace = gson.fromJson(getIntent().getSerializableExtra("currentPlace").toString(), Place.class);

        //very bad. do not do this, ever
//        int position = getIntent().getIntExtra("position", -1);
//        currentPlace = ListResultsActivity.mPlaceList.get(position);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng currentLatLng = new LatLng(
                Double.parseDouble(currentPlace.location.lat),
                Double.parseDouble(currentPlace.location.lng)
        );
        LatLng centralSeattle = Constants.seattleCenterll;

        setMapMarkers(mMap, currentLatLng, centralSeattle);
        setMapBounds(mMap, currentLatLng, centralSeattle);
    }

    private void setMapMarkers(GoogleMap map, LatLng latLng1, LatLng latLng2){
        map.addMarker(new MarkerOptions().position(latLng1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        map.addMarker(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

    }

    private void setMapBounds(GoogleMap map, LatLng latLng1, LatLng latLng2){
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        bounds.include(latLng1);
        bounds.include(latLng2);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), displayMetrics.widthPixels, 250, 0));
    }
//    Should I be taking a layout file from my resources and
//        populating it here or is what Im doing correct?

    private void setDetailsText(Place place){
        TextView placeAddressText = findViewById(R.id.place_address);
        TextView placeCategoryText = findViewById(R.id.place_category);
        TextView placeDistanceText = findViewById(R.id.place_distance);
        starImage = findViewById(R.id.star_image);

        placeAddressText.setText(place.getAddress());
        placeCategoryText.setText(place.getCategory());
        placeDistanceText.setText(place.getDistance());

        Boolean currentIsFavorited = sharedPreferences.getBoolean(currentPlace.getId(), false);

        if(currentIsFavorited){
            starImage.setImageResource(R.drawable.ic_star_yellow_24dp);
        } else {
            starImage.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

    }

    public void toggleFavorite(View view){

        ImageView starImage = (ImageView) view;

        Boolean currentIsFavorited = sharedPreferences.getBoolean(currentPlace.getId(), false);

        if(currentIsFavorited){
            sharedPreferences.edit().putBoolean(currentPlace.getId(), false).apply();
            starImage.setImageResource(R.drawable.ic_star_border_black_24dp);
        } else {
            sharedPreferences.edit().putBoolean(currentPlace.getId(), true).apply();
            starImage.setImageResource(R.drawable.ic_star_yellow_24dp);
        }


    }

}
