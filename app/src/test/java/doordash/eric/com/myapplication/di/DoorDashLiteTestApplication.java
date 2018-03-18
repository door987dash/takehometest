package doordash.eric.com.myapplication.di;

import doordash.eric.com.myapplication.DoordashLiteApplication;
import doordash.eric.com.myapplication.di.component.DaggerDoorDashLiteTestComponent;
import doordash.eric.com.myapplication.di.component.DoorDashLiteComponent;
import doordash.eric.com.myapplication.di.component.DoorDashLiteTestComponent;
import doordash.eric.com.myapplication.di.module.DoorDashLiteModule;

/**
 * Created by Eric on 3/17/2018.
 */

public class DoorDashLiteTestApplication extends DoordashLiteApplication {
    private DoorDashLiteTestComponent doorDashLiteComponent;

    @Override
    public void onCreate() {
        super.onCreate();
////        // Dagger%COMPONENT_NAME%
        doorDashLiteComponent = DaggerDoorDashLiteTestComponent.builder()
                // list of modules that are part of this component need to be created here too
                .doorDashLiteModule(new DoorDashLiteModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .build();
    }

    @Override
    public DoorDashLiteComponent component() {
        return doorDashLiteComponent;
    }

}
