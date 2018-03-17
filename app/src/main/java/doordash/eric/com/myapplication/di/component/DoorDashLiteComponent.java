package doordash.eric.com.myapplication.di.component;

import javax.inject.Singleton;

import dagger.Component;
import doordash.eric.com.myapplication.di.module.DoorDashLiteModule;

/**
 * Created by Eric on 3/16/2018.
 */
@Singleton
@Component(modules = {DoorDashLiteModule.class})
public interface DoorDashLiteComponent {
}
