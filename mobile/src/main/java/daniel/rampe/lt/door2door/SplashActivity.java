package daniel.rampe.lt.door2door;

import android.content.Intent;
import android.content.IntentSender;
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

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();
    private Firebase mFirebase;

    private GoogleApiClient mGoogleApiClient;
    private boolean mGoogleIntentInProgress = false;
    private ConnectionResult mGoogleConnectionResult;
    private boolean mGoogleLoginClicked = false;
    public static final int RC_GOOGLE_LOGIN = 1;

    @ViewById(R.id.google_login_button)
    SignInButton mGoogleLoginButton;

    @AfterViews
    void checkLogin() {
        mFirebase = Door2Door.getFirebase();
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
//            googleLogin();
        }
    }

    @Click(R.id.google_login_button)
    void onClickGoogleSigninButton() {
        mGoogleLoginClicked = true;
        if(!mGoogleApiClient.isConnecting()) {
            if(mGoogleConnectionResult != null) {
                resolveSignInError();
            } else if(mGoogleApiClient.isConnected()) {
                googleLogin();
            } else {
                Log.d(LOG_TAG, "Trying to connect to Google API");
                mGoogleApiClient.connect();
            }
        }
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

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

    @Background
    void googleLogin() {
        String token = null;
        try {
            String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
            token = GoogleAuthUtil.getToken(this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
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
        Log.d(LOG_TAG, "connected");
        googleLogin();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "connection failed");
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
