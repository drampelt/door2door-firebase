package daniel.rampe.lt.door2door.models;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import daniel.rampe.lt.door2door.Door2Door;

public class User {
    private static final String LOG_TAG = "User Model";
    private Firebase firebaseRef;
    private String Uid;

    private String name;
    private String email;
    private long reputation;

    private Map<String, Job> createdJobs;

    public User(final String Uid) {
        firebaseRef = Door2Door.getFirebase().child("users/" + Uid);
        this.Uid = Uid;
        createdJobs = new HashMap<>();

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.child("name").getValue();
                email = (String) dataSnapshot.child("email").getValue();
                reputation = (long) dataSnapshot.child("reputation").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(LOG_TAG, "firebase event cancelled");
            }
        });

//        firebaseRef.child("jobs").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                createdJobs.put(dataSnapshot.getKey(), new Job(Uid));
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                createdJobs.put(dataSnapshot.getKey(), new Job(Uid));
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                createdJobs.remove(dataSnapshot.getKey());
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) { }
//        });
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        firebaseRef.child("email").setValue(email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        firebaseRef.child("name").setValue(name);
    }

    public long getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        firebaseRef.child("reputation").setValue(reputation);
    }

    public Map<String, Job> getCreatedJobs() {
        return createdJobs;
    }
}
