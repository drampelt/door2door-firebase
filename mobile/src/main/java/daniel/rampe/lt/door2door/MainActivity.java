package daniel.rampe.lt.door2door;

import android.os.Bundle;

import daniel.rampe.lt.door2door.activities.CurrentUserActivity_;
import daniel.rampe.lt.door2door.fragments.JobListFragment_;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class MainActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle bundle) {
        MaterialSection jobs = newSection("Jobs", JobListFragment_.builder().build());
        addSection(jobs);

        MaterialSection currentUser = newSection("Current User", CurrentUserActivity_.intent(this).get());
        addSection(currentUser);

        setDrawerHeaderImage(R.drawable.banner);
    }
}
