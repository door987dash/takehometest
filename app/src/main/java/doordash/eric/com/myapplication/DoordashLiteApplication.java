/*
 * Copyright © 2017 All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package doordash.eric.com.myapplication;

import android.app.Application;

import doordash.eric.com.myapplication.di.component.DaggerDoorDashLiteComponent;
import doordash.eric.com.myapplication.di.component.DoorDashLiteComponent;
import doordash.eric.com.myapplication.di.module.DoorDashLiteModule;

/**
 * Created by Eric on 10/8/2017.
 */

public class DoordashLiteApplication extends Application {
    private static DoorDashLiteComponent doorDashLiteComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        doorDashLiteComponent = DaggerDoorDashLiteComponent.builder()
                // list of modules that are part of this component need to be created here too
                .doorDashLiteModule(new DoorDashLiteModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .build();
    }

    public static DoorDashLiteComponent getDoorDashLiteComponent() {
        return doorDashLiteComponent;
    }
}
