package daniel.rampe.lt.door2door.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CurrentUserPagerAdapter extends FragmentPagerAdapter {

    public CurrentUserPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        if(position == 1) return new AcceptedJobsFragment();
//        else return new CreatedJobsFragment();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 1) return "Accepted Jobs";
        else return "Created Jobs";
    }

}