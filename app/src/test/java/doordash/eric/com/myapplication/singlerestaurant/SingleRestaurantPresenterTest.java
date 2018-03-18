package doordash.eric.com.myapplication.singlerestaurant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import doordash.eric.com.myapplication.network.IInternetConnectivity;
import doordash.eric.com.myapplication.network.IRestaurantNetwork;
import doordash.eric.com.myapplication.network.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Eric on 3/17/2018.
 */

public class SingleRestaurantPresenterTest {
    @Mock
    private IInternetConnectivity internetConnectivity;

    @Mock
    private IRestaurantNetwork restaurantNetwork;

    @Mock
    private SingleRestaurantView singleRestaurantView;

    @Mock
    private Call<List<Restaurant>> currentGetNetworkCall;

    private SingleRestaurantPresenter singleRestaurantPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        singleRestaurantPresenter = new SingleRestaurantPresenter(internetConnectivity, restaurantNetwork);
    }

    @After
    public void destroy() {
        verifyNoMoreInteractions(internetConnectivity);
        verifyNoMoreInteractions(singleRestaurantView);
        verifyNoMoreInteractions(restaurantNetwork);
    }

    @Test
    public void testInitsViewAfterBindingWithNoData() {
        singleRestaurantPresenter.bindView(singleRestaurantView, "1");
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        // test tries to refresh
        when(internetConnectivity.isConnected()).thenReturn(false);
        verify(internetConnectivity, times(1)).isConnected();
        verify(singleRestaurantView, times(1)).showNoConnectivity();
    }

    private void setupNetworkCall() {
        when(internetConnectivity.isConnected()).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return currentGetNetworkCall;
            }
        }).when(restaurantNetwork).getRestaurant(anyString());
    }

    @Test
    public void testReturnSuccessWithDataAfterBinding() {
        setupNetworkCall();
        // now bound the view
        singleRestaurantPresenter.bindView(singleRestaurantView, "1");
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurant("1");

        // mock the response
        Restaurant restaurant = new Restaurant();
        restaurant.name = "name";
        restaurant.cover_img_url = "image";
        restaurant.id = "id";
        restaurant.description = "description";
        restaurant.status = "status";

        mockSuccessResponse(restaurant);

        // verify we sent the response to the view
        verify(singleRestaurantView, times(1)).setStatus("status");
        verify(singleRestaurantView, times(1)).setRestaurantName("name");
        verify(singleRestaurantView, times(1)).setFoodDescription("description");
        verify(singleRestaurantView, times(1)).setRestaurantImage("image");
    }

    @Test
    public void testReturnSuccessWithNullDataAfterBinding() {
        setupNetworkCall();
        // now bound the view
        singleRestaurantPresenter.bindView(singleRestaurantView, "6");
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurant("6");

        // mock the response
        mockSuccessResponse(null);

        // verify we never sent response
        verify(singleRestaurantView, never()).setRestaurantName(anyString());
    }

    @Test
    public void testReturnSuccessWithDataWithNullView() {
        setupNetworkCall();
        // now bound the view
        singleRestaurantPresenter.bindView(null, "900");

        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurant("900");

        // mock the response
        mockSuccessResponse(null);

        verifyZeroInteractions(singleRestaurantView);
    }

    @Test
    public void testDestroyViewCancels() {
        setupNetworkCall();

        singleRestaurantPresenter.bindView(singleRestaurantView, "99");
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurant("99");
        singleRestaurantPresenter.onViewPaused();

        verify(currentGetNetworkCall, times(1)).cancel();

        // should not happen but if we get a response should not crash anything
        mockSuccessResponse(mock(Restaurant.class));
    }

    @Test
    public void testNetworkErrorAfterViewDestroyed() {
        setupNetworkCall();

        singleRestaurantPresenter.bindView(singleRestaurantView, "4");
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurant("4");
        singleRestaurantPresenter.onViewPaused();

        verify(currentGetNetworkCall, times(1)).cancel();

        // network error
        ArgumentCaptor<Callback> captor = ArgumentCaptor.forClass(Callback.class);
        verify(currentGetNetworkCall, times(1)).enqueue(captor.capture());
        captor.getValue().onFailure(mock(Call.class), mock(Throwable.class));

        verify(singleRestaurantView, never()).errorOccurredWhenRefreshing();
    }

    @Test
    public void testNetworkErrorWithViewBound() {
        setupNetworkCall();

        singleRestaurantPresenter.bindView(singleRestaurantView, "0");
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurant("0");
        // network error
        ArgumentCaptor<Callback> captor = ArgumentCaptor.forClass(Callback.class);
        verify(currentGetNetworkCall, times(1)).enqueue(captor.capture());
        captor.getValue().onFailure(mock(Call.class), mock(Throwable.class));

        verify(singleRestaurantView, times(1)).errorOccurredWhenRefreshing();
    }

    private Restaurant mockSuccessResponse(Restaurant restaurant) {
        ArgumentCaptor<Callback> captor = ArgumentCaptor.forClass(Callback.class);
        verify(currentGetNetworkCall, times(1)).enqueue(captor.capture());
        Response<Restaurant> response = Response.success(restaurant);
        captor.getValue().onResponse(mock(Call.class), response);
        return restaurant;
    }

}
