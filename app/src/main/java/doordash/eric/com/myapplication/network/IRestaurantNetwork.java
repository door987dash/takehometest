package doordash.eric.com.myapplication.network;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Eric on 3/16/2018.
 */

public interface IRestaurantNetwork {
    /**
     * Returns the call object to execute network request for specified latitude and longitude
     * @param lat - latitude to use
     * @param lon - longitude to use
     * @return call object
     */
    Call<List<Restaurant>> getRestaurants(float lat, float lon);

    /**
     * Returns the call object to execute network request for specified restaurant id
     * @param restaurantId - the id of the restaurant interested in
     * @return call object
     */
    Call<Restaurant> getRestaurant(String restaurantId);
}
