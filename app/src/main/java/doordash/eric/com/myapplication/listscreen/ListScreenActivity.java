package doordash.eric.com.myapplication.listscreen;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import doordash.eric.com.myapplication.DoordashLiteApplication;
import doordash.eric.com.myapplication.R;
import doordash.eric.com.myapplication.common.BaseActivity;
import doordash.eric.com.myapplication.network.Restaurant;
import doordash.eric.com.myapplication.singlerestaurant.SingleRestaurantActivity;

public class ListScreenActivity extends BaseActivity implements ListScreenView {

    @BindView(R.id.restaurant_list)
    RecyclerView restaurantList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    ListScreenPresenter listScreenPresenter;

    private LinearLayoutManager layoutManager;

    private RestaurantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        ButterKnife.bind(this);

        ((DoordashLiteApplication) getApplication()).component().inject(this);
        swipeRefreshLayout.setOnRefreshListener(listScreenPresenter);

        listScreenPresenter.bindView(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        listScreenPresenter.onViewResumed(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        listScreenPresenter.onViewPaused();
    }

    @Override
    public void initRecyclerView(List<Restaurant> initialData) {
        restaurantList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        restaurantList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(restaurantList.getContext(),
                layoutManager.getOrientation());
        restaurantList.addItemDecoration(dividerItemDecoration);

        adapter = new RestaurantListAdapter(initialData, listScreenPresenter);
        restaurantList.setAdapter(adapter);
    }

    @Override
    public void showNoConnectivity() {
        super.showNoConnectivity();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshListWithData(List<Restaurant> restaurantList) {
        if (restaurantList != null) {
            adapter.replaceAll(restaurantList);
            adapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void errorOccurredWhenRefreshing() {
        super.errorOccurredWhenRefreshing();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void openRestaurant(String restaurantId) {
        startActivity(SingleRestaurantActivity.startSingleRestaurant(this, restaurantId));
    }
}
