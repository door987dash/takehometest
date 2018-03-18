package doordash.eric.com.myapplication.common;

import java.util.ArrayList;

import doordash.eric.com.myapplication.listscreen.ListScreenView;
import doordash.eric.com.myapplication.network.IInternetConnectivity;
import doordash.eric.com.myapplication.network.IRestaurantNetwork;
import doordash.eric.com.myapplication.network.Restaurant;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Eric on 3/18/2018.
 */

public abstract class BasePresenter<E extends BaseView> implements ILifeCycleAware<E> {
    protected final IRestaurantNetwork restaurantNetwork;

    protected final IInternetConnectivity internetConnectivity;

    protected Call currentGetNetworkCall;

    protected E view;

    public BasePresenter(IInternetConnectivity internetConnectivity, IRestaurantNetwork restaurantNetwork) {
        this.internetConnectivity = internetConnectivity;
        this.restaurantNetwork = restaurantNetwork;
    }

    public void bindView(E view) {
        this.view = view;
    }

    public void onViewPaused() {
        if (currentGetNetworkCall != null) {
            currentGetNetworkCall.cancel();
        }
        view = null;
    }

    public void onViewResumed(E view) {
        this.view = view;
    }

    protected void networkErrorOccurred(Throwable t) {
        if (view != null) {
            view.errorOccurredWhenRefreshing();
        }
    }

    protected boolean canMakeNetworkRequest() {
        if (!internetConnectivity.isConnected()) {
            view.showNoConnectivity();
            return false;
        }
        // only ever have one network call out at a time
        if (currentGetNetworkCall != null) {
            currentGetNetworkCall.cancel();
        }
        return true;
    }

    protected boolean isValidResponse(Response response) {
        return view != null && response != null && response.body() != null;
    }
}
