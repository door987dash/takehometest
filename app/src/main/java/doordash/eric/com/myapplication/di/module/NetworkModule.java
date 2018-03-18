/*
 * Copyright © 2017 All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package doordash.eric.com.myapplication.di.module;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import doordash.eric.com.myapplication.network.IInternetConnectivity;
import doordash.eric.com.myapplication.network.IRestaurantNetwork;
import doordash.eric.com.myapplication.network.InternetConnectivityManager;
import doordash.eric.com.myapplication.network.RestaurantNetworkManager;
import doordash.eric.com.myapplication.network.RestaurantService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eric on 10/8/2017.
 */
@Module
public abstract class NetworkModule {
    public NetworkModule() {
    }

    @Provides
    @Singleton
    public static RestaurantService provideRestaurantService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.doordash.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RestaurantService.class);
    }

    @Singleton
    @Binds
    public abstract IRestaurantNetwork provideRestaurantNetwork(RestaurantNetworkManager restaurantNetworkManager);

    @Reusable
    @Binds
    public abstract IInternetConnectivity provideInternetConnectivity(InternetConnectivityManager internetConnectivityManager);
}
