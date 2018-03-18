package doordash.eric.com.myapplication.listscreen;

import java.util.List;

import doordash.eric.com.myapplication.common.BaseView;
import doordash.eric.com.myapplication.network.Restaurant;

/**
 * Created by Eric on 3/16/2018.
 */

public interface ListScreenView extends BaseView {
    void initRecyclerView(List<Restaurant> initialData);

    void refreshListWithData(List<Restaurant> restaurantList);

    void openRestaurant(String restaurantId);
}
