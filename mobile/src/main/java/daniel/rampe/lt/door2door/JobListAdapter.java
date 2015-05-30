package daniel.rampe.lt.door2door;

import android.app.Activity;
import android.view.View;

import com.firebase.client.Query;

import daniel.rampe.lt.door2door.models.Job;

/**
 * Created by daniel on 2015-05-30.
 */
public class JobListAdapter extends FirebaseListAdapter<Job> {
    public JobListAdapter(Query mRef, Activity activity) {
        super(mRef, Job.class, R.layout.job, activity);
    }

    @Override
    protected void populateView(View v, Job model) {

    }
}
