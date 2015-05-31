package daniel.rampe.lt.door2door.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import daniel.rampe.lt.door2door.fragments.AcceptedJobListFragment_;
import daniel.rampe.lt.door2door.fragments.CreatedJobListFragment_;

public class CurrentUserPagerAdapter extends FragmentPagerAdapter {

    public CurrentUserPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 1) return AcceptedJobListFragment_.builder().build();
        else return CreatedJobListFragment_.builder().build();
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