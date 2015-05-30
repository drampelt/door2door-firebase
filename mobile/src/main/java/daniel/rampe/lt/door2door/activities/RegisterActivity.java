package daniel.rampe.lt.door2door.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
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

@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();

    private Firebase mFirebase;
    private SharedPreferences mPreferences;

    @ViewById(R.id.register_name_input)
    EditText mName;

    @ViewById(R.id.register_email_input)
    EditText mEmail;

    @ViewById(R.id.register_password_input)
    EditText mPassword;

    @ViewById(R.id.register_confirm_password_input)
    EditText mPasswordConfirm;

    @ViewById(R.id.register_submit_button)
    Button mSubmit;

    @AfterViews
    void init() {
        mFirebase = Door2Door.getFirebase();
        mPreferences = getSharedPreferences("door2door", Context.MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Click(R.id.register_submit_button)
    void onClickRegister() {
        Log.d(LOG_TAG, "Clicked register submit button");
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String passwordConfirm = mPasswordConfirm.getText().toString();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_LONG).show();
        } else if(!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Password doesn't match confirmation", Toast.LENGTH_LONG).show();
        } else {
            register(name, email, password);
        }
    }

    private void register(final String name, final String email, final String password) {
        mFirebase.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                mPreferences.edit().putString("email", email).putString("password", password).apply();
                mFirebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Firebase userRef = mFirebase.child("users/" + authData.getUid());
                        userRef.child("name").setValue(name);
                        userRef.child("email").setValue(email);
                        userRef.child("reputation").setValue(0);

                        User user = new User(authData.getUid());
                        Door2Door.setUser(user);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.e(LOG_TAG, "Error logging in after user signs up.");
                    }
                });

                finish();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.e(LOG_TAG, "User creation failed.");
            }
        });
    }
}
