package daniel.rampe.lt.door2door.models;

import com.firebase.client.Firebase;

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

    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public float getPayout() {
        return payout;
    }

    public String getAcceptorId() {
        return acceptorId;
    }
}
