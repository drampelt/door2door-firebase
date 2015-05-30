package daniel.rampe.lt.door2door;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.Plus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.io.IOException;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();
    private Firebase mFirebase;

    @AfterViews
    void checkLogin() {
        mFirebase = Door2Door.getFirebase();
        if(mFirebase.getAuth() != null) {
            Log.d(LOG_TAG, "We have stuff");
        } else {
            Log.d(LOG_TAG, "We don't have stuff");
            googleLogin();
        }
    }

    @Background
    void googleLogin() {
        String token = null;
        try {
            String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
            token = GoogleAuthUtil.getToken(this, Plus.AccountApi.getAccountName(null), scope);
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error", ex);
        } catch (UserRecoverableAuthException ex) {
            Log.w(LOG_TAG, "Recoverable error" + ex.toString());
            // TODO: check if we are doing this already
            Intent recover = ex.getIntent();
            startActivityForResult(recover, 1); // TODO stuff
        } catch (GoogleAuthException ex) {
            Log.e(LOG_TAG, "Error authenticating " + ex.toString());
        }

        if(token != null) {
            mFirebase.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    MainActivity_.intent(SplashActivity.this).start();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    Log.e(LOG_TAG, "Authentication error:  google login failed.");
                }
            });
        }
    }

}
