package doordash.eric.com.myapplication.network;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * Created by Eric on 3/16/2018.
 */

public class RestaurantNetworkManager implements IRestaurantNetwork {
    private final RestaurantService restaurantNetworkService;

    @Inject
    public RestaurantNetworkManager(RestaurantService restaurantNetworkService) {
        this.restaurantNetworkService = restaurantNetworkService;
    }

    @Override
    public Call<List<Restaurant>> getRestaurants(float lat, float lng) {
        Call<List<Restaurant>> call = restaurantNetworkService.getListRestaurant(lat, lng);
        return call;
    }

    @Override
    public Call<Restaurant> getRestaurant(String restaurantId) {
        return restaurantNetworkService.getSingleRestaraunt(restaurantId);
    }
}
