package c.odonfrancisco.homeawayseattlesearch;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DecimalFormat;

public class PlaceDeserializer implements JsonDeserializer<Place> {
    @Override
    public Place deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObj = json.getAsJsonObject();


        JsonElement location = jsonObj.get("location").getAsJsonObject();

        String categoryName = "No Category for this place";

        String iconString = "https://cdn2.iconfinder.com/data/icons/metro-uinvert-dock/256/Default.png";

        if(jsonObj.get("categories").getAsJsonArray().size() > 0){

            JsonElement category = jsonObj.get("categories").getAsJsonArray()
                    .get(0).getAsJsonObject();

            String iconPrefix = category.getAsJsonObject().get("icon").getAsJsonObject().get("prefix").getAsString();
            String iconSuffix = category.getAsJsonObject().get("icon").getAsJsonObject().get("suffix").getAsString();
            categoryName = category.getAsJsonObject().get("name").getAsString();
            iconString = iconPrefix + "bg_88" + iconSuffix;
        }

        String id = jsonObj.get("id").getAsString();
        String name = jsonObj.get("name").getAsString();
        String address = location.getAsJsonObject().get("formattedAddress").getAsJsonArray().get(0).getAsString();
        double lat = location.getAsJsonObject().get("lat").getAsDouble();
        double lng = location.getAsJsonObject().get("lng").getAsDouble();

        int distanceInMeters = location.getAsJsonObject().get("distance").getAsInt();
        String distance = new DecimalFormat("#.##")
                .format(distanceInMeters/1609.344);
        boolean favorited = false;


        Place newPlace = new Place(id, name, categoryName, address, lat, lng, iconString, distance, favorited);

        return newPlace;
    }
}
