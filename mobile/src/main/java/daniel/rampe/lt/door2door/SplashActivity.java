package daniel.rampe.lt.door2door;

import android.content.Intent;
import android.content.IntentSender;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.io.IOException;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();
    private Firebase mFirebase;

    private GoogleApiClient mGoogleApiClient;
    private boolean mGoogleIntentInProgress = false;
    private ConnectionResult mGoogleConnectionResult;
    private boolean mGoogleLoginClicked = false;
    public static final int RC_GOOGLE_LOGIN = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_GOOGLE_LOGIN) {
            if(resultCode != RESULT_OK) {
                Log.d(LOG_TAG, "something bad happened");
            }
            mGoogleIntentInProgress = false;
            if(!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @AfterViews
    void checkLogin() {
        mFirebase = Door2Door.getFirebase()
        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
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
            if(!mGoogleIntentInProgress) {
                Intent recover = ex.getIntent();
                startActivityForResult(recover, RC_GOOGLE_LOGIN);
                mGoogleIntentInProgress = true;
            }
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

    private void resolveSignInError() {
        if(mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        googleLogin();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(!mGoogleIntentInProgress) {
            mGoogleConnectionResult = connectionResult;

            if(mGoogleLoginClicked) {
                resolveSignInError();
            } else {
                Log.e(LOG_TAG, connectionResult.toString());
            }
        }
    }
}
