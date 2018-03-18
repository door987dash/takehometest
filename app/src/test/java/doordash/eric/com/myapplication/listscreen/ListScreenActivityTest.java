package doordash.eric.com.myapplication.listscreen;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;

import doordash.eric.com.myapplication.di.DoorDashLiteTestApplication;
import doordash.eric.com.myapplication.di.component.DoorDashLiteTestComponent;
import doordash.eric.com.myapplication.network.Restaurant;
import doordash.eric.com.myapplication.singlerestaurant.SingleRestaurantActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Eric on 3/17/2018.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = DoorDashLiteTestApplication.class)
public class ListScreenActivityTest {
    private ListScreenActivity activity;
    private ActivityController<ListScreenActivity> activityController;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(ListScreenActivity.class);
        activity = activityController.create().resume().postResume().get();
    }

    @Test
    public void verifyWeCalledBind() {
        verify(getListScreenPresenter(), times(1)).bindView(activity);
    }

    private ListScreenPresenter getListScreenPresenter() {
        DoorDashLiteTestApplication application = (DoorDashLiteTestApplication) RuntimeEnvironment.application;
        return ((DoorDashLiteTestComponent) application.component())
                .getListScreenPresenter();
    }

    @Test
    public void testLaunchOpenRestaurantWithId() {
        activity.openRestaurant("id");
        ShadowApplication application = shadowOf(RuntimeEnvironment.application);

        Intent nextStartedActivity = application.getNextStartedActivity();
        assertThat(nextStartedActivity.getStringExtra(SingleRestaurantActivity.EXTRA_RESTAURANT_ID), is("id"));
        assertThat(nextStartedActivity, is(notNullValue()));
    }

    @Test
    public void testDestroyCallsPresenter() {
        activityController.pause().destroy();
        verify(getListScreenPresenter(), times(1)).onViewPaused();
    }

    @Test
    public void testPauseResumeCallsPresenter() {
        // once from creation
        verify(getListScreenPresenter(), times(1)).onViewResumed(activity);
        activityController.pause();
        verify(getListScreenPresenter(), times(1)).onViewPaused();
        activityController.resume();
        verify(getListScreenPresenter(), times(2)).onViewResumed(activity);
    }

    @Test
    public void testInitRecyclerViewWithNull() {
        // should not crash anything
        activity.initRecyclerView(null);
    }

    @Test
    public void testInitWithEmptyData() {
        ArrayList<Restaurant> list = new ArrayList();
        activity.initRecyclerView(list);
        assertThat(activity.restaurantList.getAdapter().getItemCount(), is(0));
    }

    @Test
    public void testInitWithOneItemInList() {
        ArrayList<Restaurant> list = new ArrayList();
        list.add(mock(Restaurant.class));

        activity.initRecyclerView(list);

        assertThat(activity.restaurantList.getAdapter().getItemCount(), is(1));
    }

    @Test
    public void testInit3PiecesOfData() {
        ArrayList<Restaurant> list = new ArrayList();
        for (int i = 0; i < 3; i++) {
            list.add(mock(Restaurant.class));
        }
        activity.initRecyclerView(list);
        assertThat(activity.restaurantList.getAdapter().getItemCount(), is(3));
    }

    @Test
    public void testShowNoConnectivity() {
        activity.swipeRefreshLayout.setRefreshing(true);
        activity.showNoConnectivity();
        assertThat(activity.swipeRefreshLayout.isRefreshing(), is(false));
        // TODO: could use shadow toast to verify a toast shows
    }

    @Test
    public void testShowErrorOccurredWhenRefreshing() {
        activity.swipeRefreshLayout.setRefreshing(true);
        activity.errorOccurredWhenRefreshing();
        assertThat(activity.swipeRefreshLayout.isRefreshing(), is(false));
        // TODO: could use shadow toast to verify a toast shows
    }

    @Test
    public void testNewDataPassedIn() {
        testInitWithEmptyData();
        activity.swipeRefreshLayout.setRefreshing(true);
        ArrayList<Restaurant> list = new ArrayList();
        list.add(mock(Restaurant.class));
        list.add(mock(Restaurant.class));
        activity.refreshListWithData(list);
        assertThat(activity.restaurantList.getAdapter().getItemCount(), is(2));
        assertThat(activity.swipeRefreshLayout.isRefreshing(), is(false));

        // pass in null should leave it alone and keep count at 2
        activity.refreshListWithData(null);
        assertThat(activity.restaurantList.getAdapter().getItemCount(), is(2));
    }

    @Test
    public void testNullPassedIn() {
        testInitWithEmptyData();
        activity.swipeRefreshLayout.setRefreshing(true);
        activity.refreshListWithData(null);
        assertThat(activity.restaurantList.getAdapter().getItemCount(), is(0));
        assertThat(activity.swipeRefreshLayout.isRefreshing(), is(false));
    }
}
