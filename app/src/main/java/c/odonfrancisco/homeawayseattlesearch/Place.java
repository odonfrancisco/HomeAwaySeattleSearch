package c.odonfrancisco.homeawayseattlesearch;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private String id;
    private String name;
    private String category;
    private String address;
    private double lat;
    private double lng;
    private Bitmap icon;
    private String distance;
    private boolean favorited;

    // Constructor
    Place(String id, String name, String category, String address,
          double lat, double lng, Bitmap icon, String distance, boolean favorited){
        this.id = id;
        this.name = name;
        this.category = category;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.icon = icon;
        this.distance = distance;
        this.favorited = favorited;
    }

    // Setter, getter


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public double getLat(){
        return lat;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public double getLng(){
        return lng;
    }

    public void setLng(double lng){
        this.lng = lng;
    }

    public LatLng getLatLng(){
        LatLng latLng = new LatLng(lat, lng);
        return latLng;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
