package doordash.eric.com.myapplication.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Eric on 3/16/2018.
 */

public interface RestaurantService {
    String LAT = "lat";

    String LNG = "lng";

    String RESTAURANT_ID = "restaurant_id";

    @GET("v2/restaurant/")
    Call<List<Restaurant>> getListRestaurant(@Query(LAT) float lat, @Query(LNG) float lng);

    @GET("v2/restaurant/{restaurant_id}/")
    Call<Restaurant> getSingleRestaraunt(@Path(RESTAURANT_ID) String id);
}
