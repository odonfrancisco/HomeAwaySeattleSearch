package c.odonfrancisco.homeawayseattlesearch;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = PlaceDetailsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Place currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolBarLayout);

        Gson gson = new Gson();

//        Should I be using Parcelable instead of gson?
        currentPlace = gson.fromJson(getIntent().getSerializableExtra("currentPlace").toString(), Place.class);

        Log.d(TAG, gson.toJson(currentPlace));

//        int position = getIntent().getIntExtra("position", -1);

        //very bad. do not do this, ever
//        currentPlace = ListResultsActivity.mPlaceList.get(position);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.placeDetailsMap);
        mapFragment.getMapAsync(this);

        toolbarLayout.setTitle(currentPlace.getName());

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng currentLatLng = currentPlace.getLatLng();
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

        System.out.println("WIDTH");
        System.out.println(displayMetrics.widthPixels);

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), displayMetrics.widthPixels, 250, 0));
    }

}
