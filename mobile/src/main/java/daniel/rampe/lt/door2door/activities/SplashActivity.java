package daniel.rampe.lt.door2door.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.Firebase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import daniel.rampe.lt.door2door.Door2Door;
import daniel.rampe.lt.door2door.R;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();

    private SharedPreferences mPreferences;

    private Firebase mFirebase;

    @AfterViews
    void init() {
        mFirebase = Door2Door.getFirebase();
        mPreferences = getSharedPreferences("door2door", Context.MODE_PRIVATE);
        if(mPreferences.contains("email") && mPreferences.contains("password")) {
            Log.d(LOG_TAG, "TODO auto redirect");
        }
    }

    @Click(R.id.login_button)
    void onClickLogin() {
        LoginActivity_.intent(this).start();
    }

    @Click(R.id.register_button)
    void onClickRegister() {
        RegisterActivity_.intent(this).start();
    }

}
