package daniel.rampe.lt.door2door.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.Firebase;

public class Job extends BaseModel implements Parcelable {
    private static final String LOG_TAG = "Job Model";
    private Firebase firebaseRef;

    private String type;
    private String address;
    private Double latitude;
    private Double longitude;
    private double payout;
    private String description;
    private boolean completed;

    private String creatorId;
    private String acceptorId;

    public Job() {
        super("jobs");
    }

    private Job(Parcel in) {
        super("jobs");
        uid = in.readString();
        type = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        payout = in.readDouble();
        acceptorId = in.readString();
        creatorId = in.readString();
        description = in.readString();
        completed = in.readByte() == 1;
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public double getPayout() {
        return payout;
    }

    public String getAcceptorId() {
        return acceptorId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(type);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(payout);
        dest.writeString(acceptorId);
        dest.writeString(creatorId);
        dest.writeString(description);
        byte b = 0;
        if(completed) b = 1;
        dest.writeByte(b);
    }
}
