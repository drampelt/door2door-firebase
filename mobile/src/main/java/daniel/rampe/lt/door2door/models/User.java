package daniel.rampe.lt.door2door.models;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import daniel.rampe.lt.door2door.Door2Door;

public class User {
    private static final String LOG_TAG = "User Model";
    private Firebase firebaseRef;
    private String Uid;

    private String name;
    private String email;
    private int reputation;

    public User(String Uid) {
        firebaseRef = Door2Door.getFirebase().child("users/" + Uid);
        this.Uid = Uid;

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.child("name").getValue();
                email = (String) dataSnapshot.child("email").getValue();
                reputation = (int) dataSnapshot.child("reputation").getValue();
                name = (String) dataSnapshot.child("name").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(LOG_TAG, "firebase event cancelled");
            }
        });
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

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        firebaseRef.child("reputation").setValue(reputation);
    }
}
