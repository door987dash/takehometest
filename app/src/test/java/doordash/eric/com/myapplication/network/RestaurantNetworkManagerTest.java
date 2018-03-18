package doordash.eric.com.myapplication.network;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 3/17/2018.
 */

public class RestaurantNetworkManagerTest {
    @Mock
    private RestaurantService restaurantNetworkService;

    private RestaurantNetworkManager restaurantNetworkManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        restaurantNetworkManager = new RestaurantNetworkManager(restaurantNetworkService);
    }

    @Test
    public void testGetListOfRestaurants() {
        restaurantNetworkManager.getRestaurants(0,0);
        verify(restaurantNetworkService, times(1)).getListRestaurant(0, 0);

        restaurantNetworkManager.getRestaurants(200,-1);
        verify(restaurantNetworkService, times(1)).getListRestaurant(200, -1);
    }

    @Test
    public void testGetSingleRestaurants() {
        restaurantNetworkManager.getRestaurant(null);
        verify(restaurantNetworkService, times(1)).getSingleRestaraunt(null);

        restaurantNetworkManager.getRestaurant("123");
        verify(restaurantNetworkService, times(1)).getSingleRestaraunt("123");
    }
}
