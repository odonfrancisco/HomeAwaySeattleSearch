package c.odonfrancisco.homeawayseattlesearch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareApi {

    String clientId = "D3P13HOKAGT1OJERCTPDFK3NZS4MXXDE1XW3X52XA3LOJ3IQ";
    String clientSecret = "I5T5ISW5VPCN5NH5D2R2THHXMDJ5WRZQNDA0DKWRYAFEPYJ3";

    //        String apiHttp = "https://api.foursquare.com/v2/venues/explore?"
    String apiHttp = /*Constants.BASE_URL + */ "venues/search?";
//        String apiHttp = "https://api.foursquare.com/v2/venues/suggestcompletion?";

    String v = "20180323";

    String url = apiHttp +
            "client_id=" + clientId +
            "&client_secret=" + clientSecret +
            "&v=" + v ;

    @GET(url)
    Call<ResponseBody> getPlaces(
            @Query("limit") String limit,
            @Query("ll") String ll,
            @Query("query") String query
    );
}
