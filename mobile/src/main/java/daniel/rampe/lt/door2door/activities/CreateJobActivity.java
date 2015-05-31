package daniel.rampe.lt.door2door.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import daniel.rampe.lt.door2door.Door2Door;
import daniel.rampe.lt.door2door.R;
import daniel.rampe.lt.door2door.models.User;

@EActivity(R.layout.activity_create_job)
public class CreateJobActivity extends AppCompatActivity {

    private Firebase mFirebaseRef;

    @ViewById(R.id.create_job_type)
    EditText mType;

    @ViewById(R.id.create_job_location)
    EditText mLocation;

    @ViewById(R.id.create_job_payout)
    EditText mPayout;

    @ViewById(R.id.create_job_desc)
    EditText mDescription;

    @ViewById(R.id.create_job_submit)
    Button mSubmitJob;

    @AfterViews
    void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFirebaseRef = Door2Door.getFirebase();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Click(R.id.create_job_submit)
    void onClickSubmitJob() {
        String type = mType.getText().toString();
        String location = mLocation.getText().toString();
        String payout = mPayout.getText().toString();
        String description = mDescription.getText().toString();

        if(type.isEmpty() || location.isEmpty() || payout.isEmpty()) {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_LONG).show();
        } else {
            createJob(type, location, Double.parseDouble(payout), description);
        }
    }

    void createJob(final String type, final String location, final double payout, final String description) {
        final Firebase newJobRef = mFirebaseRef.child("jobs").push();

        newJobRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.child("type").setValue(type);
                mutableData.child("payout").setValue(payout);
                mutableData.child("description").setValue(description.isEmpty() ? false : description);
                mutableData.child("acceptorId").setValue("");
                mutableData.child("completed").setValue(false);

                LatLng latlong = getLocationFromAddress(location);
                mutableData.child("latitude").setValue(latlong.latitude);
                mutableData.child("longitude").setValue(latlong.longitude);
                mutableData.child("address").setValue(location);

                User currentUser = Door2Door.getUser();
                mutableData.child("creatorId").setValue(currentUser.getUid());
                currentUser.getFirebaseRef().child("jobs/" + newJobRef.getKey()).setValue(true);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                finish();
            }
        });
    }

    LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return p1;
    }

}
