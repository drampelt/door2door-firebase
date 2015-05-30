package daniel.rampe.lt.door2door;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by daniel on 2015-05-30.
 */
public class Door2Door extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
