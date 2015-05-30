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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        firebaseRef.child("email").setValue(email);
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
        firebaseRef.child("reputation").setValue(reputation);
    }

    private String email;
    private int reputation;

    public User(String Uid) {
        firebaseRef = Door2Door.getFirebase().child("users/" + Uid);
        this.Uid = Uid;

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = (String) dataSnapshot.child("email").getValue();
                reputation = (int) dataSnapshot.child("reputation").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(LOG_TAG, "firebase event cancelled");
            }
        });
    }
}