package daniel.rampe.lt.door2door.models;

import android.util.Log;

import com.firebase.client.Firebase;

import daniel.rampe.lt.door2door.Door2Door;

/**
 * Created by daniel on 2015-05-30.
 */
public abstract class BaseModel {

    private Firebase mFirebaseRef;
    private String mType;

    protected String uid;

    public BaseModel(String type) {
        mType = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Firebase getFirebaseRef() {
        return Door2Door.getFirebase().child(mType + "/" + uid);
    }
}
