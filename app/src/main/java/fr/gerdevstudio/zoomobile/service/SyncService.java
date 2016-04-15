package fr.gerdevstudio.zoomobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * An {@link IntentService}
 * Class to sync animals from database in webservice
 * <p/>
 * helper methods.
 */
public class SyncService extends IntentService {
    // IntentService can perform
    public static final String ACTION_SYNC_ANIMALS_FROM_WEBSERVICE = "fr.gerdevstudio.zoomobile.action.SYNCANIMALS";

    private static final String EXTRA_SYNC_DELAY = "fr.gerdevstudio.zoomobile.extra.SYNCDELAY";

    private final static String BASE_URL_FOR_WEBAPP = "http://10.0.0.2:8080/ZooBuild/";


    // delay between 2 refresh in ms. default is 30sec.
    private final static int DEFAULT_SYNC_DELAY = 30000;
    private int syncDelay;

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // notifying user that service started
        Toast.makeText(this,"SyncService launched",Toast.LENGTH_SHORT).show();

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SYNC_ANIMALS_FROM_WEBSERVICE.equals(action)) {
                syncDelay = intent.getIntExtra(EXTRA_SYNC_DELAY, DEFAULT_SYNC_DELAY);

                //Here we run the method syncData every syncDelay sec (default 30sec)
                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        syncData();
                        h.postDelayed(this, syncDelay);
                    }
                }, syncDelay);
            }
        }
    }

    // main method to perform data synchronisation
    private void syncData() {
        // creating the request
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL_FOR_WEBAPP).build();
        ZooService service = retrofit.create(ZooService.class);
        Call<List<AnimalModelWebService>> call = service.listAnimals();

        // listing animals in a toast
        try {
            Response<List<AnimalModelWebService>> response = call.execute();
            for (AnimalModelWebService animal : response.body()) {
                Toast.makeText(this, animal.getName(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // retrofit interface for http calls
    private interface ZooService {
        @GET("rest/animals")
        Call<List<AnimalModelWebService>> listAnimals();
    }

    private class AnimalModelWebService {
        private int id;
        private String name;
        private String espece;
        private String description;
        private String photo;

        public AnimalModelWebService() {

        }

        public AnimalModelWebService(String name, String espece) {
            this.name = name;
            this.espece = espece;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEspece() {
            return espece;
        }

        public void setEspece(String espece) {
            this.espece = espece;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

}
