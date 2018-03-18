package doordash.eric.com.myapplication.singlerestaurant;

import javax.inject.Inject;

import doordash.eric.com.myapplication.common.BasePresenter;
import doordash.eric.com.myapplication.common.ILifeCycleAware;
import doordash.eric.com.myapplication.network.IInternetConnectivity;
import doordash.eric.com.myapplication.network.IRestaurantNetwork;
import doordash.eric.com.myapplication.network.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Eric on 3/17/2018.
 */

public class SingleRestaurantPresenter extends BasePresenter<SingleRestaurantView> {
    private final Callback<Restaurant> getRestaurantCallback;

    @Inject
    public SingleRestaurantPresenter(IInternetConnectivity internetConnectivity, IRestaurantNetwork restaurantNetwork) {
        super(internetConnectivity, restaurantNetwork);

        getRestaurantCallback = new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (isValidResponse(response)) {
                    updateViewWithResult(response.body());
                }
                currentGetNetworkCall = null;
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                networkErrorOccurred(t);
            }
        };
    }

    private void updateViewWithResult(Restaurant restaurant) {
        if (restaurant != null) {
            view.setFoodDescription(restaurant.description);
            view.setRestaurantImage(restaurant.cover_img_url);
            view.setRestaurantName(restaurant.name);
            view.setStatus(restaurant.status);
        }
    }

    public void bindView(SingleRestaurantView restaurantView, String restaurantId) {
        super.bindView(restaurantView);

        retrieveRestaurantData(restaurantId);
    }

    private void retrieveRestaurantData(String restaurantId) {
        if (canMakeNetworkRequest()) {
            currentGetNetworkCall = restaurantNetwork.getRestaurant(restaurantId);
            currentGetNetworkCall.enqueue(getRestaurantCallback);
        }
    }
}
