package doordash.eric.com.myapplication.common;

import doordash.eric.com.myapplication.listscreen.ListScreenActivity;

/**
 * Created by Eric on 3/18/2018.
 */

public interface ILifeCycleAware<E> {
    void onViewPaused();

    void onViewResumed(E listScreenActivity);
}
