package daniel.rampe.lt.door2door.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;

import java.util.List;

import daniel.rampe.lt.door2door.R;
import daniel.rampe.lt.door2door.models.Job;

@EActivity(R.layout.activity_job_detail)
public class JobDetailActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker mMarker;
    private LatLng mPosition;

    @Extra
    Job mJob;

    @AfterViews
    void init() {
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    @Background
    void setUpMap() {
        mPosition = getLocationFromAddress(mJob.getLocation());
        setUpMapUi();
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    @UiThread
    void setUpMapUi() {
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(getLocationFromAddress("172 Harvard Rd Waterloo Ontario"))
                .title(mJob.getType())
                .snippet(mJob.getLocation()));
        mMarker.showInfoWindow();
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
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
