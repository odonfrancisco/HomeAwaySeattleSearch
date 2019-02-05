package c.odonfrancisco.homeawayseattlesearch;

import android.graphics.Bitmap;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class PlaceDeserializer implements JsonDeserializer<Place> {
    @Override
    public Place deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObj = json.getAsJsonObject();

        ListResultsActivity.ImageDownloader imageDownloader = new ListResultsActivity.ImageDownloader();

        JsonElement location = jsonObj.get("location").getAsJsonObject();
        JsonElement category = jsonObj.get("categories").getAsJsonArray()
                .get(0).getAsJsonObject();

        String iconPrefix = category.getAsJsonObject().get("icon").getAsJsonObject().get("prefix").getAsString();
        String iconSuffix = category.getAsJsonObject().get("icon").getAsJsonObject().get("suffix").getAsString();
        String iconString = iconPrefix + "bg_88" + iconSuffix;


        String id = jsonObj.get("id").getAsString();
        String name = jsonObj.get("name").getAsString();
        String categoryName = category.getAsJsonObject().get("name").getAsString();
        String address = location.getAsJsonObject().get("formattedAddress").getAsJsonArray().get(0).getAsString();
        double lat = location.getAsJsonObject().get("lat").getAsDouble();
        double lng = location.getAsJsonObject().get("lng").getAsDouble();
        Bitmap icon;


        int distanceInMeters = location.getAsJsonObject().get("distance").getAsInt();
        String distance = new DecimalFormat("#.##")
                .format(distanceInMeters/1609.344);
        boolean favorited = false;


//        String id = currentObj.getString("id");
//        String name = currentObj.getString("name");
//        String categoryName = category.getString("name");
//        String address = location.getJSONArray("formattedAddress")
//                .getString(0);
//        double lat = location.getDouble("lat");
//        double lng = location.getDouble("lng");
//
//        String iconPrefix = category.getJSONObject("icon").getString("prefix");
//        String iconSuffix = category.getJSONObject("icon").getString("suffix");
//        String iconString = iconPrefix + "bg_88" + iconSuffix;
//        Bitmap icon = imageDownloader.execute(iconString).get();
////                System.out.println("URI " + icon);
//
//        int distanceInMeters = location.getInt("distance");
//        String distance = new DecimalFormat("#.##")
//                .format(distanceInMeters/1609.344);
//        boolean favorited = false;
        Place newPlace = null;
        try {
            icon = imageDownloader.execute(iconString).get();
            newPlace = new Place(id, name, categoryName, address, lat, lng, icon, distance, favorited);
            return newPlace;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return newPlace;
    }
}
