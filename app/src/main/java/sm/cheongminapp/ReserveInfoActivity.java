package sm.cheongminapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.data.Reservation;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_info);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Reservation reservation = (Reservation)intent.getSerializableExtra("Reservation");

        tvCenterName.setText("센터 이름");
        tvReason.setText(reservation.Reason);
        tvDate.setText(reservation.Date);
        tvTime.setText(reservation.getTimeRangeText());
        //tvLocation.setText(reservation.Location);
        //tvLocationDetail.setText(reservation.LocationDetail);

        // TODO:: Lat, Lng를 사용해 맵에 보여줘야함
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
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.reserve_info_map);
            mapFragment.getMapAsync(this);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // 한국으로 이동
        LatLng latLng = new LatLng(37.56667, 126.97806);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // 내 위치 버튼을 활성화하려면 위치 권한을 확인해야함
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }
}
