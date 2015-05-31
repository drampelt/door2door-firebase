package daniel.rampe.lt.door2door.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import daniel.rampe.lt.door2door.R;
import daniel.rampe.lt.door2door.adapters.CurrentUserPagerAdapter;

@EActivity(R.layout.activity_current_user)
public class CurrentUserActivity extends AppCompatActivity {

    @ViewById(R.id.current_user_pager)
    ViewPager mViewPager;

    @AfterViews
    void init() {
        mViewPager.setAdapter(new CurrentUserPagerAdapter(getSupportFragmentManager()));
    }

}
