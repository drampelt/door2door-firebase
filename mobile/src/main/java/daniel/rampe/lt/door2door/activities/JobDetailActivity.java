package daniel.rampe.lt.door2door.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.NumberFormat;
import java.util.List;

import daniel.rampe.lt.door2door.Door2Door;
import daniel.rampe.lt.door2door.R;
import daniel.rampe.lt.door2door.models.Job;
import daniel.rampe.lt.door2door.models.User;

@EActivity(R.layout.activity_job_detail)
public class JobDetailActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker mMarker;
    private LatLng mPosition;

    private Firebase mFirebase;

    private NumberFormat mNumberFormat;

    @Extra
    Job mJob;

    User mCreator, mAcceptor;

    @ViewById(R.id.type)
    TextView mType;

    @ViewById(R.id.payout)
    TextView mPayout;

    @ViewById(R.id.name)
    TextView mName;

    @ViewById(R.id.description)
    TextView mDescription;

    @ViewById(R.id.claim_button)
    Button mClaimButton;

    @AfterViews
    void init() {
        mNumberFormat = NumberFormat.getCurrencyInstance();
        setUpMapIfNeeded();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mType.setText(mJob.getType());
        mPayout.setText(mNumberFormat.format(mJob.getPayout()));
        mDescription.setText(mJob.getDescription());
        Door2Door.getFirebase().child("users/" + mJob.getCreatorId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCreator = dataSnapshot.getValue(User.class);
                mName.setText("Job posted by " + mCreator.getName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        if(mJob.getAcceptorId() != null && !mJob.getAcceptorId().isEmpty()) {
            Door2Door.getFirebase().child("users/" + mJob.getAcceptorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mAcceptor = dataSnapshot.getValue(User.class);
                    mAcceptor.setUid(dataSnapshot.getKey());
                    if(mJob.isCompleted()) {
                        mClaimButton.setText("Completed");
                        mClaimButton.setEnabled(false);
                    } else if(mAcceptor != null) {
                        if(mJob.getCreatorId().equals(Door2Door.getUser().getUid())) {
                            mClaimButton.setText("Claimed by " + mAcceptor.getName() + ". Mark complete?");
                            mClaimButton.setEnabled(true);
                        } else if(mAcceptor.getUid().equals(Door2Door.getUser().getUid())) {
                            mClaimButton.setText("Claimed by you. Unclaim?");
                            mClaimButton.setEnabled(true);
                        } else {
                            mClaimButton.setText("Claimed");
                            mClaimButton.setEnabled(false);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } else {
            mClaimButton.setEnabled(true);
        }
    }

    @Click(R.id.claim_button)
    void onClaim() {
        if(mAcceptor == null) {
            // claim as current user
            mJob.getFirebaseRef().child("acceptorId").setValue(Door2Door.getUser().getUid());
            mClaimButton.setText("Claimed by you. Unclaim?");
            mAcceptor = Door2Door.getUser();
        } else {
            if(mJob.getCreatorId().equals(Door2Door.getUser().getUid())) {
                // mark complete
                mJob.getFirebaseRef().child("completed").setValue(true);
                mClaimButton.setText("Completed");
                mClaimButton.setEnabled(false);
            } else {
                // unclaim
                mJob.getFirebaseRef().child("acceptorId").setValue("");
                mClaimButton.setText("Claim");
                mAcceptor = null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
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
        mPosition = new LatLng(mJob.getLatitude(), mJob.getLongitude());
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
                .position(mPosition)
                .title(mJob.getType())
                .snippet(mJob.getAddress()));
        mMarker.showInfoWindow();
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        mMap.setBuildingsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mPosition));
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
