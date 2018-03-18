package doordash.eric.com.myapplication.singlerestaurant;

import doordash.eric.com.myapplication.common.BaseView;

/**
 * Created by Eric on 3/17/2018.
 */

public interface SingleRestaurantView extends BaseView {
    void setRestaurantName(String restaurantName);

    void setStatus(String restaurantStatus);

    void setFoodDescription(String restaurantFoodDescription);

    void setRestaurantImage(String url);
}
