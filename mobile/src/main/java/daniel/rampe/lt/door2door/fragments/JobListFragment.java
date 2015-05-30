package daniel.rampe.lt.door2door.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import daniel.rampe.lt.door2door.Door2Door;
import daniel.rampe.lt.door2door.JobListAdapter;
import daniel.rampe.lt.door2door.R;
import daniel.rampe.lt.door2door.activities.CreateJobActivity;
import daniel.rampe.lt.door2door.activities.CreateJobActivity_;

/**
 * Created by daniel on 2015-05-30.
 */
@EFragment(R.layout.fragment_jobs)
public class JobListFragment extends Fragment {

    private Firebase mFirebase;

    @ViewById(R.id.job_list)
    ListView mListView;

    @ViewById(R.id.new_job_fab)
    FloatingActionButton mCreateJobFab;

    @AfterViews
    void init() {
        Log.d("job list", "init");
        mFirebase = Door2Door.getFirebase();
        JobListAdapter adapter = new JobListAdapter(mFirebase.child("jobs").limitToFirst(100), getActivity());
        mListView.setAdapter(adapter);
    }

    @Click(R.id.new_job_fab)
    void onClickCreateJob() {
        CreateJobActivity_.intent(this).start();
    }
}
