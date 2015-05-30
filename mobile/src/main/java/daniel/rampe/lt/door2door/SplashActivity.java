package daniel.rampe.lt.door2door;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Map;

import daniel.rampe.lt.door2door.activities.LoginActivity_;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();

    private SharedPreferences mPreferences;

    private Firebase mFirebase;

    @AfterViews
    void init() {
        mFirebase = Door2Door.getFirebase();
        mPreferences = getPreferences(Context.MODE_PRIVATE);
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
