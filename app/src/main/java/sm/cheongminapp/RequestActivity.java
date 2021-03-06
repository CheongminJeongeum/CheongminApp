package sm.cheongminapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.fragment.RequestFragment;
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.model.data.EmptyData;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;

public class RequestActivity extends AppCompatActivity {

    @BindView(R.id.request_toolbar)
    Toolbar request_toolbar;

    @BindView(R.id.request_location_text)
    EditText eLocation;

    @BindView(R.id.request_location_detail_text)
    EditText eLocationDetail;

    @BindView(R.id.request_date_text)
    EditText eDate;

    @BindView(R.id.request_start_time_text)
    EditText eStartTime;

    @BindView(R.id.request_end_time_text)
    EditText eEndTime;

    @BindView(R.id.request_reason_text)
    EditText eGoal;

    DatePicker dpPicker;

    private int centerId;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        ButterKnife.bind(this);

        setSupportActionBar(request_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // 지도 액티비티로부터 예약 위치 정보를 받아옴
        Intent intent = getIntent();
        centerId = intent.getIntExtra("centerId", -1);
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
        String location = intent.getStringExtra("location");

        eLocation.setText(location);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.request_date_pick_button)
    void DatePick() {
        View dialogView = View.inflate(RequestActivity.this, R.layout.dialog_datepicker, null);
        dpPicker = (DatePicker)dialogView.findViewById(R.id.date_picker);

        AlertDialog.Builder dlg = new AlertDialog.Builder(RequestActivity.this);
        dlg.setView(dialogView);
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String retDate = dpPicker.getYear() + "-" + (dpPicker.getMonth() +1 ) + "-" + dpPicker.getDayOfMonth();
                eDate.setText(retDate);
            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();
    }

    @OnClick(R.id.request_submit_button)
    void Submit() {
        IApiService apiService = ApiService.getInstance().getService();
        apiService.requestReservation(
                centerId,
                MainActivity.id,
                eDate.getText().toString(),
                Integer.parseInt(eStartTime.getText().toString()),
                Integer.parseInt(eEndTime.getText().toString()),
                eGoal.getText().toString(),
                eLocation.getText().toString() + "\n" + eLocationDetail.getText().toString(),
                lat, lng).enqueue(new Callback<ResultModel<EmptyData>>() {
            @Override
            public void onResponse(Call<ResultModel<EmptyData>> call, Response<ResultModel<EmptyData>> response) {
                if(response.isSuccessful() == false || response.body().IsSuccessful == false)
                {
                    Toast.makeText(getApplicationContext(), "요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                Toast.makeText(getApplicationContext(), "요청에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<ResultModel<EmptyData>> call, Throwable t) {
            }
        });
    }
}
