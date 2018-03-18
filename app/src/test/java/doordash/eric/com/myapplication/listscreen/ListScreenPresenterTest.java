package doordash.eric.com.myapplication.listscreen;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import doordash.eric.com.myapplication.network.IInternetConnectivity;
import doordash.eric.com.myapplication.network.IRestaurantNetwork;
import doordash.eric.com.myapplication.network.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
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

public class ListScreenPresenterTest {
    @Mock
    private IInternetConnectivity internetConnectivity;

    @Mock
    private IRestaurantNetwork restaurantNetwork;

    @Mock
    private ListScreenView listScreenView;

    @Mock
    private Call<List<Restaurant>> currentGetNetworkCall;

    private ListScreenPresenter listScreenPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        listScreenPresenter = new ListScreenPresenter(internetConnectivity, restaurantNetwork);
    }

    @After
    public void destroy() {
        verifyNoMoreInteractions(internetConnectivity);
        verifyNoMoreInteractions(listScreenView);
        verifyNoMoreInteractions(restaurantNetwork);
    }

    @Test
    public void testInitsViewAfterBindingWithNoData() {
        listScreenPresenter.bindView(listScreenView);
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(listScreenView, times(1)).initRecyclerView(captor.capture());
        assertThat(captor.getValue().isEmpty(), is(true));

        // test tries to refresh
        when(internetConnectivity.isConnected()).thenReturn(false);
        verify(internetConnectivity, times(1)).isConnected();
        verify(listScreenView, times(1)).showNoConnectivity();
    }

    @Test
    public void testPullToRefreshWithInternet() {
        setupRefreshWithNetworkOn();
        listScreenPresenter.onRefresh();
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurants(ListScreenPresenter.TEMP_LAT, ListScreenPresenter.TEMP_LNG);
    }

    private void setupRefreshWithNetworkOn() {
        when(internetConnectivity.isConnected()).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return currentGetNetworkCall;
            }
        }).when(restaurantNetwork).getRestaurants(anyFloat(), anyFloat());
    }

    @Test
    public void testReturnSuccessWithDataAfterBinding() {
        setupRefreshWithNetworkOn();
        // now bound the view
        listScreenPresenter.bindView(listScreenView);
        verify(listScreenView, times(1)).initRecyclerView(any(List.class));
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurants(ListScreenPresenter.TEMP_LAT, ListScreenPresenter.TEMP_LNG);

        // mock the response
        List list = mockSuccessResponse();

        // verify we sent the response to the view
        verify(listScreenView, times(1)).refreshListWithData(list);
    }

    @Test
    public void testReturnSuccessWithNullDataAfterBinding() {
        setupRefreshWithNetworkOn();
        // now bound the view
        listScreenPresenter.bindView(listScreenView);
        verify(listScreenView, times(1)).initRecyclerView(any(List.class));
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurants(ListScreenPresenter.TEMP_LAT, ListScreenPresenter.TEMP_LNG);

        // mock the response
        ArgumentCaptor<Callback> captor = ArgumentCaptor.forClass(Callback.class);
        verify(currentGetNetworkCall, times(1)).enqueue(captor.capture());
        Response<List<Restaurant>> response = Response.success(null);
        captor.getValue().onResponse(mock(Call.class), response);

        // verify we never sent the response to the view
        verify(listScreenView, never()).refreshListWithData(any(List.class));
    }

    @Test
    public void testReturnSuccessWithDataWithNullView() {
        setupRefreshWithNetworkOn();
        // now bound the view
        listScreenPresenter.bindView(null);

        listScreenPresenter.onRefresh();
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurants(ListScreenPresenter.TEMP_LAT, ListScreenPresenter.TEMP_LNG);

        // mock the response
        mockSuccessResponse();

        verifyZeroInteractions(listScreenView);
    }

    @Test
    public void testDestroyViewCancels() {
        setupRefreshWithNetworkOn();

        listScreenPresenter.bindView(listScreenView);
        verify(listScreenView, times(1)).initRecyclerView(any(List.class));
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurants(ListScreenPresenter.TEMP_LAT, ListScreenPresenter.TEMP_LNG);

        listScreenPresenter.onViewPaused();

        verify(currentGetNetworkCall, times(1)).cancel();

        // should not happen but if we get a response should not crash anything
        mockSuccessResponse();
    }

    @Test
    public void testNetworkErrorAfterViewDestroyed() {
        setupRefreshWithNetworkOn();

        listScreenPresenter.bindView(listScreenView);
        verify(listScreenView, times(1)).initRecyclerView(any(List.class));
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurants(ListScreenPresenter.TEMP_LAT, ListScreenPresenter.TEMP_LNG);

        listScreenPresenter.onViewPaused();

        verify(currentGetNetworkCall, times(1)).cancel();

        // network error
        ArgumentCaptor<Callback> captor = ArgumentCaptor.forClass(Callback.class);
        verify(currentGetNetworkCall, times(1)).enqueue(captor.capture());
        captor.getValue().onFailure(mock(Call.class), mock(Throwable.class));

        verify(listScreenView, never()).errorOccurredWhenRefreshing();
    }

    @Test
    public void testNetworkErrorWithViewBound() {
        setupRefreshWithNetworkOn();

        listScreenPresenter.bindView(listScreenView);
        verify(listScreenView, times(1)).initRecyclerView(any(List.class));
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurants(ListScreenPresenter.TEMP_LAT, ListScreenPresenter.TEMP_LNG);

        // network error
        ArgumentCaptor<Callback> captor = ArgumentCaptor.forClass(Callback.class);
        verify(currentGetNetworkCall, times(1)).enqueue(captor.capture());
        captor.getValue().onFailure(mock(Call.class), mock(Throwable.class));

        verify(listScreenView, times(1)).errorOccurredWhenRefreshing();
    }


    @Test
    public void testListViewHolderBinding() {
        setupRefreshWithNetworkOn();
        // now bound the view
        listScreenPresenter.bindView(listScreenView);
        verify(listScreenView, times(1)).initRecyclerView(any(List.class));
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurants(ListScreenPresenter.TEMP_LAT, ListScreenPresenter.TEMP_LNG);

        // mock the response
        List<Restaurant> list = mockSuccessResponse();
        Restaurant restaurant = new Restaurant();
        restaurant.name = "name";
        restaurant.cover_img_url = "image";
        restaurant.id = "id";
        restaurant.description = "description";
        restaurant.status = "status";
        list.add(restaurant);

        // verify we sent the response to the view
        verify(listScreenView, times(1)).refreshListWithData(list);

        RestaurantListAdapter.ViewHolder holder = mock(RestaurantListAdapter.ViewHolder.class);

        listScreenPresenter.bindViewHolder(holder, 0);
        verify(holder, times(1)).setName("name");
        verify(holder, times(1)).setRestaurantImage("image");
        verify(holder, times(1)).setFoodType("description");
        verify(holder, times(1)).setStatus("status");

        // verify the onlick functionality
        ArgumentCaptor<View.OnClickListener> captor = ArgumentCaptor.forClass(View.OnClickListener.class);
        verify(holder, times(1)).setOnclickListener(captor.capture());
        captor.getValue().onClick(null);
        // should of called view to send to a page with id
        verify(listScreenView, times(1)).openRestaurant("id");

        // destroy the view
        listScreenPresenter.onViewPaused();

        captor.getValue().onClick(null);
        // should not crash and should still only be 1
        verify(listScreenView, times(1)).openRestaurant("id");

    }

    @Test
    public void testListViewHolderBindingWithEmptyData() {
        setupRefreshWithNetworkOn();
        // now bound the view
        listScreenPresenter.bindView(listScreenView);
        verify(listScreenView, times(1)).initRecyclerView(any(List.class));
        verify(internetConnectivity, times(1)).isConnected();
        verify(restaurantNetwork, times(1)).getRestaurants(ListScreenPresenter.TEMP_LAT, ListScreenPresenter.TEMP_LNG);

        // mock the response
        List<Restaurant> list = mockSuccessResponse();
        assertThat(list.isEmpty(), is(true));

        // verify we sent the response to the view
        verify(listScreenView, times(1)).refreshListWithData(list);

        RestaurantListAdapter.ViewHolder holder = mock(RestaurantListAdapter.ViewHolder.class);

        listScreenPresenter.bindViewHolder(holder, 0);
        verifyZeroInteractions(holder);
    }

    @Test
    public void testListViewHolderBindingWithNoData() {
        // there is no data should do nothing
        RestaurantListAdapter.ViewHolder holder = mock(RestaurantListAdapter.ViewHolder.class);

        listScreenPresenter.bindViewHolder(holder, 0);
        verifyZeroInteractions(holder);
    }

    private List<Restaurant> mockSuccessResponse() {
        ArgumentCaptor<Callback> captor = ArgumentCaptor.forClass(Callback.class);
        verify(currentGetNetworkCall, times(1)).enqueue(captor.capture());
        List<Restaurant> list = new ArrayList<>();
        Response<List<Restaurant>> response = Response.success(list);
        captor.getValue().onResponse(mock(Call.class), response);
        return list;
    }

}
