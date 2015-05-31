package daniel.rampe.lt.door2door.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import daniel.rampe.lt.door2door.R;
import daniel.rampe.lt.door2door.adapters.CurrentUserPagerAdapter;

@EActivity(R.layout.activity_current_user)
public class CurrentUserActivity extends AppCompatActivity {

    @ViewById(R.id.current_user_pager)
    ViewPager mViewPager;

    @ViewById(R.id.current_user_tabs)
    TabLayout mTabLayout;

    @AfterViews
    void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CurrentUserPagerAdapter adapter = new CurrentUserPagerAdapter(getSupportFragmentManager());
        mTabLayout.setTabsFromPagerAdapter(adapter);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
