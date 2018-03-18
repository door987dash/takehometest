package doordash.eric.com.myapplication.listscreen;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import doordash.eric.com.myapplication.network.Restaurant;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Eric on 3/17/2018.
 */
@RunWith(RobolectricTestRunner.class)
public class RestaurantListAdapterTest {
    @Mock
    private ListScreenPresenter onclickListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNullParamsOnAdapter() {
        RestaurantListAdapter adapter = new RestaurantListAdapter(null, null);
        // should not break anything
    }
    @Test
    public void testReplaceChangesTheSize() {
        ArrayList<Restaurant> listOfData = new ArrayList();
        listOfData.add(mock(Restaurant.class));
        RestaurantListAdapter adapter = new RestaurantListAdapter(listOfData, onclickListener);
        assertThat(adapter.getItemCount(), is(1));

        // test sending null does nothing
        adapter.replaceAll(null);
        assertThat(adapter.getItemCount(), is(1));

        // test sending empty data sets it
        List<Restaurant> newListOfData = new ArrayList<>();
        adapter.replaceAll(newListOfData);
        assertThat(adapter.getItemCount(), is(0));
    }
}
