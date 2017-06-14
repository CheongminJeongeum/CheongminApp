package sm.cheongminapp;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.utility.GPSModule;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final int REQ_CODE_FIND_LOCATION = 100;

    @BindView(R.id.maps_toolbar)
    Toolbar toolbar;

    @BindView(R.id.maps_search_text)
    EditText etSearchText;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private int centerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 맵 설정
        setUpMapIfNeeded();

        // 선택한 센터 정보를 받아옴
        Intent intent = getIntent();
        centerId = intent.getIntExtra("centerId", -1);

        // 검색 설정
        etSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId)
                {
                    case EditorInfo.IME_ACTION_SEARCH:
                        onSearchMap(v.getText().toString());
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_FIND_LOCATION) {
            if (permissions.length == 1 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "현재 위치를 가져올 수 없습니다" , Toast.LENGTH_LONG ).show();
            }
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps_map_framgent);
            mapFragment.getMapAsync(this);

            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LatLng latLng = new LatLng(37.56667, 126.97806);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        setUpMap();

        // 내 위치 버튼을 활성화하기 위해 권한 확인
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, REQ_CODE_FIND_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        GPSModule gps = new GPSModule(this);
        final String address = gps.findAddress(latLng.latitude, latLng.longitude);

        AlertDialog.Builder dlg = new AlertDialog.Builder(MapsActivity.this);
        dlg.setMessage("선택하신 장소가 " + address + " 입니까?");
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MapsActivity.this, RequestActivity.class);
                intent.putExtra("centerId", centerId);
                intent.putExtra("location", address);
                intent.putExtra("lat", latLng.latitude);
                intent.putExtra("lng", latLng.longitude);

                startActivity(intent);
                finish();
            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();
    }

    public void onSearchMap(String address) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            if(addressList.size() > 0)
            {
                Address firstAddress = addressList.get(0);

                LatLng latLng = new LatLng(firstAddress.getLatitude(), firstAddress.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
