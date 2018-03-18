package doordash.eric.com.myapplication.listscreen;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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
 * Created by Eric on 3/16/2018.
 */

public class ListScreenPresenter extends BasePresenter<ListScreenView> implements SwipeRefreshLayout.OnRefreshListener {
    public static final float TEMP_LAT = 37.422740f;

    public static final float TEMP_LNG = -122.139956f;

    private Callback<List<Restaurant>> getListOfRestaurantsCallback;

    private List<Restaurant> listOfRestaurants;

    @Inject
    public ListScreenPresenter(IInternetConnectivity internetConnectivity, IRestaurantNetwork restaurantNetwork) {
        super(internetConnectivity, restaurantNetwork);

        getListOfRestaurantsCallback = new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (isValidResponse(response)) {
                    listOfRestaurants = response.body();
                    view.refreshListWithData(listOfRestaurants);
                }
                currentGetNetworkCall = null;
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                networkErrorOccurred(t);
            }
        };
    }

    @Override
    public void bindView(final ListScreenView view) {
        super.bindView(view);
        if (view != null) {
            view.initRecyclerView(new ArrayList<Restaurant>());
            onRefresh();
        }
    }

    // for now there is no ui to set coordinates, so swipe to refresh will use the hardcode coordinates
    @Override
    public void onRefresh() {
        if (canMakeNetworkRequest()) {
            currentGetNetworkCall = restaurantNetwork.getRestaurants(TEMP_LAT, TEMP_LNG);
            currentGetNetworkCall.enqueue(getListOfRestaurantsCallback);
        }
    }

    public void bindViewHolder(RestaurantListAdapter.ViewHolder holder, int position) {
        if (listOfRestaurants != null && listOfRestaurants.size() > position) {
            final Restaurant restaurant = listOfRestaurants.get(position);
            holder.setStatus(restaurant.status);
            holder.setName(restaurant.name);
            holder.setFoodType(restaurant.description);
            holder.setRestaurantImage(restaurant.cover_img_url);
            holder.setOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (view != null) {
                        view.openRestaurant(restaurant.id);
                    }
                }
            });
        }
    }
}
