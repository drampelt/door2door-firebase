package daniel.rampe.lt.door2door;

import android.app.Application;

import com.firebase.client.Firebase;

import daniel.rampe.lt.door2door.models.User;

/**
 * Created by daniel on 2015-05-30.
 */
public class Door2Door extends Application {

    private static Firebase sFirebase;

    public static User getUser() {
        return currentUser;
    }

    public static void setUser(User currentUser) {
        Door2Door.currentUser = currentUser;
    }

    private static User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    public static final Firebase getFirebase() {
        if(sFirebase == null) {
            sFirebase = new Firebase("https://door2door.firebaseio.com");
        }
        return sFirebase;
    }
}
