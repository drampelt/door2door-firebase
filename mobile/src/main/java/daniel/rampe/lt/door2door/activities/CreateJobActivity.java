package daniel.rampe.lt.door2door.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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

    void createJob(String type, String location, double payout, String description) {
        Firebase newJobRef = mFirebaseRef.child("jobs").push();
        User currentUser = Door2Door.getUser();

        newJobRef.child("type").setValue(type);
        newJobRef.child("location").setValue(location);
        newJobRef.child("payout").setValue(payout);
        newJobRef.child("description").setValue(description.isEmpty() ? false : description);
        newJobRef.child("creatorId").setValue(currentUser.getUid());
        newJobRef.child("acceptorId").setValue(false);

        currentUser.getFirebaseRef().child("jobs/" + newJobRef.getKey()).setValue(true);
    }

}
