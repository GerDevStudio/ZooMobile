package fr.gerdevstudio.zoomobile.application;

import android.app.Application;
import android.content.Intent;

import com.facebook.stetho.Stetho;

import fr.gerdevstudio.zoomobile.service.SyncService;

/**
 * Created by ger on 12/04/2016.
 */
public class ZooApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //including stetho library
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        //initializing service
        Intent i = new Intent(this,SyncService.class);
        i.setAction(SyncService.ACTION_SYNC_ANIMALS_FROM_WEBSERVICE);
        startService(i);

    }
}
