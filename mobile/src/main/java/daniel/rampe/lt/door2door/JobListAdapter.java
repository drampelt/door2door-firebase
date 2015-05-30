package daniel.rampe.lt.door2door;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import java.text.NumberFormat;

import daniel.rampe.lt.door2door.activities.JobDetailActivity_;
import daniel.rampe.lt.door2door.models.Job;

/**
 * Created by daniel on 2015-05-30.
 */
public class JobListAdapter extends FirebaseListAdapter<Job> {
    private Activity mActivity;

    private NumberFormat mNumberFormat;

    public JobListAdapter(Query mRef, Activity activity) {
        super(mRef, Job.class, R.layout.job, activity);
        mActivity = activity;
        mNumberFormat = NumberFormat.getCurrencyInstance();
    }

    @Override
    protected void populateView(View v, final Job model) {
        TextView type = (TextView) v.findViewById(R.id.type);
        type.setText(model.getType());

        TextView distance = (TextView) v.findViewById(R.id.distance);
        distance.setText("ayy lmao");

        TextView payout = (TextView) v.findViewById(R.id.payout);
        payout.setText(mNumberFormat.format(model.getPayout()));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobDetailActivity_.intent(mActivity).mJob(model).start();
            }
        });
    }
}
