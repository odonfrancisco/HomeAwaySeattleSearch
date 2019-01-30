package c.odonfrancisco.homeawayseattlesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        System.out.println(ListResultsActivity.mPlaceList);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng seattleCenter = Constants.seattleCenterll;
        mMap.addMarker(new MarkerOptions().position(seattleCenter).title("Center of Seattle"));

        addMapMarkers(ListResultsActivity.mPlaceList);
        setOnMapMarkerClickListener(mMap);
    }

    private void addMapMarkers(ArrayList<Place> placesList){
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        for(int i=0; i<placesList.size(); i++){
            Place currentPlace = placesList.get(i);

            LatLng currentLatLng = currentPlace.getLatLng();

            Marker mMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title(currentPlace.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            mMarker.setTag(i);

            bounds.include(mMarker.getPosition());
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 40));
    }

    private void setOnMapMarkerClickListener(GoogleMap map){
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker.getTag() != null) {

                    Intent intent = new Intent(getApplicationContext(), PlaceDetailsActivity.class);
                    intent.putExtra("position", Integer.parseInt(marker.getTag().toString()));

                    startActivity(intent);

                }
            }
        });
    }
}
