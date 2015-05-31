package daniel.rampe.lt.door2door.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import daniel.rampe.lt.door2door.Door2Door;
import daniel.rampe.lt.door2door.adapters.JobListAdapter;
import daniel.rampe.lt.door2door.R;
import daniel.rampe.lt.door2door.activities.CreateJobActivity_;

/**
 * Created by daniel on 2015-05-30.
 */
@EFragment(R.layout.fragment_jobs)
@OptionsMenu(R.menu.menu_job_list)
public class JobListFragment extends Fragment {

    private Firebase mFirebase;

    private String mFilter;

    @ViewById(R.id.job_list)
    ListView mListView;

    @ViewById(R.id.new_job_fab)
    FloatingActionButton mCreateJobFab;

    @AfterViews
    void init() {
        Log.d("job list", "init");
        mFirebase = Door2Door.getFirebase();
        updateList();
    }

    void updateList() {
        Log.d("Filter", "filtering with " + mFilter);
        Query query = mFirebase.child("jobs").limitToFirst(100);
        if(mFilter != null) query = query.orderByChild("type").equalTo(mFilter);
        JobListAdapter adapter = new JobListAdapter(query, getActivity());
        mListView.setAdapter(adapter);
    }

    @Click(R.id.new_job_fab)
    void onClickCreateJob() {
        CreateJobActivity_.intent(this).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().toString().equals("All")) {
            mFilter = null;
        } else {
            mFilter = item.getTitle().toString();
        }
        updateList();
        return true;
    }
}
