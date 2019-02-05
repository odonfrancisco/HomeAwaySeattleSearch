package c.odonfrancisco.homeawayseattlesearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListResultsActivity extends AppCompatActivity {

    private ListView resultsListView;

    /*
        VERy VEry Bad.  Don't do this!
     */
    // Is it a good idea to make this static to access it from the main maps page?
    static ArrayList<Place> mPlaceList = new ArrayList<>();

    public static class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String ...strings){

            try{
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                Bitmap downloadedImageBitmap = BitmapFactory.decodeStream(inputStream);

                return downloadedImageBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_results);

        resultsListView = findViewById(R.id.resultsList);

        Intent intent = getIntent();

        generateListView(new ArrayList<Object>
                        (intent.getParcelableArrayListExtra("Results")));

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlaceDetailsActivity.class);
                intent.putExtra("position", position);

                startActivity(intent);

            }
        });

    }

    public ArrayList<Place> generateListView(ArrayList<Object> resultsList){
        PlaceListAdapter adapter;

        for(int i=0; i<resultsList.size(); i++){
            // Should I be putting all the code inside the try block or shou
            // I only have new JSONObject inside it?
            try {
                ImageDownloader imageDownloader = new ImageDownloader();

                JSONObject currentObj = new JSONObject(resultsList.get(i).toString());
                JSONObject location = currentObj.getJSONObject("location");
                JSONObject category = currentObj.getJSONArray("categories")
                        .getJSONObject(0);

                String id = currentObj.getString("id");
                String name = currentObj.getString("name");
                String categoryName = category.getString("name");
                String address = location.getJSONArray("formattedAddress")
                        .getString(0);
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                String iconPrefix = category.getJSONObject("icon").getString("prefix");
                String iconSuffix = category.getJSONObject("icon").getString("suffix");
                String iconString = iconPrefix + "bg_88" + iconSuffix;
                Bitmap icon = imageDownloader.execute(iconString).get();
//                System.out.println("URI " + icon);

                int distanceInMeters = location.getInt("distance");
                String distance = new DecimalFormat("#.##")
                        .format(distanceInMeters/1609.344);
                boolean favorited = false;

//                Place currentPlace = new Place(id, name, categoryName,
//                        address, lat, lng, icon, distance, favorited);

                System.out.println("CURRENTOBJ");
                System.out.println(currentObj);

//                mPlaceList.add(currentPlace);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        adapter = new PlaceListAdapter(this, mPlaceList);

        return mPlaceList;
//        resultsListView.setAdapter(adapter);
    }

    public void clickFloatingActionButton(View view){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//        intent.putParcelableArrayListExtra("placesList", mPlaceList);
//        intent.putExtra("placesList", mPlaceList.toString());

        startActivity(intent);
    }
}
