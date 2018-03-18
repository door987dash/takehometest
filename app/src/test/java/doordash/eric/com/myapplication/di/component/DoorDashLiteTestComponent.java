package doordash.eric.com.myapplication.di.component;

import javax.inject.Singleton;

import dagger.Component;
import doordash.eric.com.myapplication.di.module.DoorDashLiteModule;
import doordash.eric.com.myapplication.di.module.PresenterTestModule;
import doordash.eric.com.myapplication.listscreen.ListScreenActivity;
import doordash.eric.com.myapplication.listscreen.ListScreenPresenter;
import doordash.eric.com.myapplication.singlerestaurant.SingleRestaurantPresenter;

/**
 * Created by Eric on 3/17/2018.
 */
@Singleton
@Component(modules = { PresenterTestModule.class, DoorDashLiteModule.class })
public interface DoorDashLiteTestComponent extends DoorDashLiteComponent{
    void inject(ListScreenActivity listScreenActivity);

    ListScreenPresenter getListScreenPresenter();

    SingleRestaurantPresenter getSingleRestaurantPresenter();
}
