package sm.cheongminapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.data.Reservation;
import sm.cheongminapp.utility.DateHelper;

public class ReserveInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.reserve_info_toolbar)
    Toolbar toolbar;

    @BindView(R.id.reserve_info_center)
    TextView tvCenterName;

    @BindView(R.id.reserve_info_reason)
    TextView tvReason;

    @BindView(R.id.reserve_info_date)
    TextView tvDate;

    @BindView(R.id.reserve_info_time)
    TextView tvTime;

    @BindView(R.id.reserve_info_location)
    TextView tvLocation;

    @BindView(R.id.reserve_info_location_detail)
    TextView tvLocationDetail;

    private GoogleMap mMap;

    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_info);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reservation = (Reservation)getIntent().getSerializableExtra("Reservation");

        tvCenterName.setText(reservation.CenterName + " 수화 통역 센터");
        tvReason.setText(reservation.Reason);

        Date localDate = DateHelper.getUTCStringToLocalDate(reservation.Date);
        String localDateText = new SimpleDateFormat("yyyy년 MM월 dd일").format(localDate).toString();

        tvDate.setText(localDateText);
        tvTime.setText(reservation.getTimeRangeText());

        if(reservation.Location != null) {
            String[] location = reservation.Location.split("\n");
            if(location.length > 1)
            {
                tvLocation.setText(location[0]);
                tvLocationDetail.setText(location[1]);
            }
        }

        setUpMapIfNeeded();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.reserve_info_map);
            mapFragment.getMapAsync(this);

            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LatLng latLng = new LatLng(reservation.Lat, reservation.Lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

        if(reservation.Location == null || reservation.Location.contains("\n") == false)
            return;

        String[] locations = reservation.Location.split("\n");
        mMap.addMarker(new MarkerOptions().position(latLng).title(locations[0]));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        setUpMap();
    }
}
