package doordash.eric.com.myapplication.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

/**
 * Created by Eric on 3/16/2018.
 */

public class InternetConnectivityManager implements IInternetConnectivity {

    private final ConnectivityManager connectivityManager;

    @Inject
    public InternetConnectivityManager(Context context) {
       connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
