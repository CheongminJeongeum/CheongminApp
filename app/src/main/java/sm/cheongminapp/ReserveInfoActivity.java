package sm.cheongminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.data.ReservationList;

public class ReserveInfoActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_info);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        ReservationList reservation = (ReservationList)intent.getSerializableExtra("ReservationList");

        tvCenterName.setText(reservation.CenterName);
        tvReason.setText(reservation.Reason);
        tvDate.setText(reservation.Date);
        tvTime.setText(reservation.Time);
        tvLocation.setText(reservation.Location);
        tvLocationDetail.setText(reservation.LocationDetail);

        // TODO:: Lat, Lng를 사용해 맵에 보여줘야함
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
