package daniel.rampe.lt.door2door.fragments;

import android.app.Fragment;
import android.widget.ListView;

import com.firebase.client.Firebase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import daniel.rampe.lt.door2door.Door2Door;
import daniel.rampe.lt.door2door.adapters.JobListAdapter;
import daniel.rampe.lt.door2door.R;

@EFragment(R.layout.fragment_created_job_list)
public class CreatedJobListFragment extends Fragment {

    private Firebase mFirebase;

    @ViewById(R.id.created_job_list)
    ListView mListView;

    @AfterViews
    void init() {
        mFirebase = Door2Door.getFirebase();
        JobListAdapter adapter =
                new JobListAdapter(mFirebase.child("jobs")
                    .orderByChild("creatorId")
                    .equalTo(Door2Door.getUser().getUid()), getActivity());

        mListView.setAdapter(adapter);
    }

}
