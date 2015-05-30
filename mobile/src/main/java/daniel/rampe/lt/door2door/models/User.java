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

    public User(final String Uid) {
        firebaseRef = Door2Door.getFirebase().child("users/" + Uid);
        this.Uid = Uid;

        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.child("name").getValue();
                email = (String) dataSnapshot.child("email").getValue();
                reputation = (long) dataSnapshot.child("reputation").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getReputation() {
        return reputation;
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }

    public Firebase getFirebaseRef() {
        return firebaseRef;
    }

    public void setFirebaseRef(Firebase firebaseRef) {
        this.firebaseRef = firebaseRef;
    }
}
