package c.odonfrancisco.homeawayseattlesearch;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

//try to implement parcelable
@Parcel
public class Place {
    String id;
    String name;
    String category;
    String address;
    double lat;
    double lng;
    String icon;
    String distance;
    boolean favorited;
    FourSqLocation location;

    public Place(){}

    // Constructor
    public Place(String id, String name, String category, String address,
          double lat, double lng, String icon, String distance, boolean favorited){
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
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
