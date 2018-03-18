package doordash.eric.com.myapplication.di.component;

import javax.inject.Singleton;

import dagger.Component;
import doordash.eric.com.myapplication.di.module.DoorDashLiteModule;
import doordash.eric.com.myapplication.di.module.NetworkModule;
import doordash.eric.com.myapplication.listscreen.ListScreenActivity;
import doordash.eric.com.myapplication.singlerestaurant.SingleRestaurantActivity;

/**
 * Created by Eric on 3/16/2018.
 */
@Singleton
@Component(modules = {DoorDashLiteModule.class, NetworkModule.class})
public interface DoorDashLiteComponent {
    void inject(ListScreenActivity listScreenActivity);

    void inject(SingleRestaurantActivity singleRestaurantActivity);
}
