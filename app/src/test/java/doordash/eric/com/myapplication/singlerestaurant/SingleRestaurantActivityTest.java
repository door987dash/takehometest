package doordash.eric.com.myapplication.singlerestaurant;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import doordash.eric.com.myapplication.R;
import doordash.eric.com.myapplication.di.DoorDashLiteTestApplication;
import doordash.eric.com.myapplication.di.component.DoorDashLiteTestComponent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 3/17/2018.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = DoorDashLiteTestApplication.class)
public class SingleRestaurantActivityTest {
    private SingleRestaurantActivity activity;

    private ActivityController<SingleRestaurantActivity> activityController;

    @Before
        public void setUp() {
        Intent intent = new Intent();
        intent.putExtra(SingleRestaurantActivity.EXTRA_RESTAURANT_ID, "1");
        activityController = Robolectric.buildActivity(SingleRestaurantActivity.class, intent);
            activity = activityController.create().resume().postResume().get();
        }

    @Test
    public void verifyWeCalledBind() {
        // 1 is from the intent passed in
        verify(getSingleRestaurantPresenter(), times(1)).bindView(activity, "1");
    }

    @Test
    public void testSettingName() {
        activity.setRestaurantName("name");
        assertThat(activity.name.getText().toString(), is(getString(R.string.single_restaurant_name, "name")));
    }

    @Test
    public void testSettingFoodDescription() {
        activity.setFoodDescription("food");
        assertThat(activity.foodDescription.getText().toString(), is(getString(R.string.single_restaurant_food_description, "food")));
    }

    @Test
    public void testSettingStatus() {
        activity.setStatus("status");
        assertThat(activity.status.getText().toString(), is(getString(R.string.single_restaurant_status, "status")));
    }

    // not testing glide, can be tested if I wrapped glide and verify a call was made to glide

    private String getString(int resourceId, String additionalValue) {
        return RuntimeEnvironment.application.getString(resourceId, additionalValue);
    }

    @Test
    public void testDestroyCallsPresenter() {
            activityController.pause().destroy();
        DoorDashLiteTestApplication application = (DoorDashLiteTestApplication) RuntimeEnvironment.application;
        verify(getSingleRestaurantPresenter(), times(1)).onViewPaused();
    }

    @Test
    public void testPauseResumeCallsPresenter() {
        // once from creation in setup
        verify(getSingleRestaurantPresenter(), times(1)).onViewResumed(activity);
        activityController.pause();
        verify(getSingleRestaurantPresenter(), times(1)).onViewPaused();
        activityController.resume();
        verify(getSingleRestaurantPresenter(), times(2)).onViewResumed(activity);
    }

    private SingleRestaurantPresenter getSingleRestaurantPresenter() {
        DoorDashLiteTestApplication application = (DoorDashLiteTestApplication) RuntimeEnvironment.application;
        return ((DoorDashLiteTestComponent) application.component())
                .getSingleRestaurantPresenter();
    }
}
