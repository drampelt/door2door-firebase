package daniel.rampe.lt.door2door.models;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import daniel.rampe.lt.door2door.Door2Door;

/**
 * Created by siva on 30/05/15.
 */
public class Job {
    private static final String LOG_TAG = "Job Model";
    private Firebase firebaseRef;

    private String type;
    private String location;
    private float payout;

    private String creatorId;
    private String acceptorId;

    public Job(String creatorId) {
        firebaseRef = Door2Door.getFirebase().child("jobs").push();
        firebaseRef.child("creatorId").setValue(creatorId);
        firebaseRef.child("acceptorId").setValue(false);

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = (String) dataSnapshot.child("type").getValue();
                location = (String) dataSnapshot.child("location").getValue();
                payout = (float) dataSnapshot.child("payout").getValue();
                acceptorId = (String) dataSnapshot.child("acceptorId").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        firebaseRef.child("type").setValue(type);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        firebaseRef.child("location").setValue(location);
    }

    public float getPayout() {
        return payout;
    }

    public void setPayout(float payout) {
        firebaseRef.child("payout").setValue(payout);
    }

    public String getAcceptorId() {
        return acceptorId;
    }

    public void setAcceptorId(String acceptorId) {
        firebaseRef.child("acceptorId").setValue(acceptorId);
    }
}
