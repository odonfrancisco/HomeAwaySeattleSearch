package c.odonfrancisco.homeawayseattlesearch;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Place currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolBarLayout);

        int position = getIntent().getIntExtra("position", -1);

        currentPlace = ListResultsActivity.mPlaceList.get(position);

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
        LatLng centralSeattle = MainActivity.seattleCenterll;

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
