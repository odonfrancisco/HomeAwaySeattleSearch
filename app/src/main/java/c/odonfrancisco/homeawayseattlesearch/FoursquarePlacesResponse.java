package c.odonfrancisco.homeawayseattlesearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FoursquarePlacesResponse {
    @SerializedName("venues")
    List<Place> places;

    public FoursquarePlacesResponse(){
        places = new ArrayList<Place>();
    }

    public static FoursquarePlacesResponse parseJSON(String response){
        Gson gson = new GsonBuilder().create();
        FoursquarePlacesResponse foursquarePlacesResponse = gson.fromJson(response, FoursquarePlacesResponse.class);
        return foursquarePlacesResponse;
    }
}
