package c.odonfrancisco.homeawayseattlesearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText searchTermView;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result.append(current);
                    data = reader.read();
                }
                return result.toString();

            } catch(Exception e){
                e.printStackTrace();
                return "Failed!";
            }
        }

        @Override
        protected void onPostExecute(String result){
            try {
                JSONArray resultJSONArray = new JSONObject(result)
                                .getJSONObject("response")
                                .getJSONArray("venues");

                ArrayList<Object> venuesList = new ArrayList<>();

                for(int i=0; i<resultJSONArray.length(); i++){
                    venuesList.add(resultJSONArray.get(i).toString());
                }

                beginListActivity(venuesList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTermView = findViewById(R.id.searchInput);
    }

    public void searchButtonClicked(View view){
        callApi(searchTermView.getText().toString());
    }

    private void callApi(String query){

        DownloadTask task = new DownloadTask();

        String clientId = "D3P13HOKAGT1OJERCTPDFK3NZS4MXXDE1XW3X52XA3LOJ3IQ";
        String clientSecret = "I5T5ISW5VPCN5NH5D2R2THHXMDJ5WRZQNDA0DKWRYAFEPYJ3";

//        String apiHttp = "https://api.foursquare.com/v2/venues/explore?"
        String apiHttp = Constants.BASE_URL + "venues/search?";
//        String apiHttp = "https://api.foursquare.com/v2/venues/suggestcompletion?";

        String v = "20180323";
        String limit = "23";
        String ll = "47.6062, -122.3321";

        String url = apiHttp +
                "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&v=" + v +
                "&limit=" + limit +
                "&ll=" + ll +
                "&query=" + query;

        task.execute(url);
    }

    private void beginListActivity(ArrayList<Object> results){

        Intent intent = new Intent(this, ListResultsActivity.class);
        intent.putExtra("Results", results);

        Log.d(TAG, "YEAST");
        System.out.println(results);

        startActivity(intent);
    }
}
