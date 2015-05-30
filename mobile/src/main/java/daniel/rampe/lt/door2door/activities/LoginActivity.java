package daniel.rampe.lt.door2door.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import daniel.rampe.lt.door2door.Door2Door;
import daniel.rampe.lt.door2door.MainActivity;
import daniel.rampe.lt.door2door.R;
import daniel.rampe.lt.door2door.models.User;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private Firebase mFirebase;
    private SharedPreferences mPreferences;

    @ViewById(R.id.email)
    EditText mEmail;

    @ViewById(R.id.password)
    EditText mPassword;


    @AfterViews
    void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFirebase = Door2Door.getFirebase();
        mPreferences = getSharedPreferences("door2door", Context.MODE_PRIVATE);
        if(mPreferences.contains("email") && mPreferences.contains("password")) {
            login(mPreferences.getString("email", null), mPreferences.getString("password", null));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Click(R.id.login_button)
    void onClickLogin() {
        Log.d(LOG_TAG, "Click login");
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_LONG).show();
        } else {
            login(email, password);
        }
    }

    private void login(final String email, final String password) {
        mFirebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d(LOG_TAG, "auth data id:" + authData.getUid() + ", email:" + authData.getProviderData().get("email"));
                Log.d(LOG_TAG, "we have authenticated");
                mPreferences.edit().putString("email", email).putString("password", password).apply();

                User user = new User(authData.getUid());
                Door2Door.setUser(user);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.e(LOG_TAG, "Error authenticating: " + firebaseError.getMessage());
                Toast.makeText(LoginActivity.this, firebaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
