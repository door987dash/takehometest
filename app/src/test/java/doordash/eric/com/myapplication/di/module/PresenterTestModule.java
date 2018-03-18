package doordash.eric.com.myapplication.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import doordash.eric.com.myapplication.listscreen.ListScreenPresenter;
import doordash.eric.com.myapplication.singlerestaurant.SingleRestaurantPresenter;

import static org.mockito.Mockito.mock;

/**
 * Created by Eric on 3/17/2018.
 */
@Module
public class PresenterTestModule {
    public PresenterTestModule() {
    }

    @Provides
    @Singleton
    ListScreenPresenter provideListScreenPresenter() {
        return mock(ListScreenPresenter.class);
    }

    @Provides
    @Singleton
    SingleRestaurantPresenter provideSingleRestaurantPresenter() {
        return mock(SingleRestaurantPresenter.class);
    }
}
