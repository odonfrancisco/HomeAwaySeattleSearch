package c.odonfrancisco.homeawayseattlesearch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private ArrayList<Place> placesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ArrayList<Parcelable> parcelableArrayList = getIntent().getParcelableArrayListExtra("placesArray");

        for(int i=0; i<parcelableArrayList.size(); i++){
            placesArrayList.add((Place) Parcels.unwrap(parcelableArrayList.get(0)));
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        System.out.println("PLaces array list");
        System.out.println(placesArrayList);

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

        addMapMarkers(placesArrayList);
        setOnMapMarkerClickListener(mMap);
    }

    private void addMapMarkers(ArrayList<Place> placesList){
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        for(int i=0; i<placesList.size(); i++){
            Place currentPlace = placesList.get(i);

            LatLng currentLatLng = currentPlace.getLatLng();
            if(i == 1)Log.d(TAG, currentLatLng.toString());

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
