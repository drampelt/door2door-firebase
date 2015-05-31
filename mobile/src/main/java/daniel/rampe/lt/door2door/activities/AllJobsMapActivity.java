package daniel.rampe.lt.door2door.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import daniel.rampe.lt.door2door.Door2Door;
import daniel.rampe.lt.door2door.R;

/**
 * Created by daniel on 2015-05-30.
 */
@EActivity(R.layout.activity_all_jobs)
public class AllJobsMapActivity extends AppCompatActivity {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Firebase mFirebase;

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    @UiThread
    void setUpMap() {
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        mMap.setBuildingsEnabled(true);
        mFirebase = Door2Door.getFirebase();
        mFirebase.child("jobs").limitToFirst(100).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        mMarker = mMap.addMarker(new MarkerOptions()
//                .position(mPosition)
//                .title(mJob.getType())
//                .snippet(mJob.getAddress()));
//        mMarker.showInfoWindow();
//        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(mPosition));
    }

    private class PopupAdapter implements GoogleMap.InfoWindowAdapter {
        private View mPopup = null;
        private LayoutInflater mInflater = null;

        public PopupAdapter(LayoutInflater inflater) {
            mInflater = inflater;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if(mPopup == null) {
                mPopup = mInflater.inflate(R.layout.map_popup, null);
            }

            TextView title = (TextView) mPopup.findViewById(R.id.popup_title);
            title.setText(marker.getTitle());

            TextView snippet = (TextView) mPopup.findViewById(R.id.popup_snippet);
            snippet.setText(marker.getSnippet());

            return mPopup;
        }
    }
}
