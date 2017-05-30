package sm.cheongminapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.data.Reservation;
import sm.cheongminapp.data.ReservationList;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.utility.GPSModule;
import sm.cheongminapp.view.adapter.AbstractAdapter;
import sm.cheongminapp.view.adapter.ResponseAdapter;

public class RequestListActivity extends AppCompatActivity {

    @BindView(R.id.request_list_list_view)
    ListView lvRequestList;

    @BindView(R.id.request_list_toolbar)
    Toolbar toolbar;

    AbstractAdapter<ReservationList> adapter;
    ArrayList<ReservationList> reqList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        adapter = new ResponseAdapter(this);
        adapter.addOrderList(reqList);

        lvRequestList.setAdapter(adapter);

        IApiService apiService = ApiService.getInstance().getService();
        apiService.getMyReservations(MainActivity.id).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    Reservation reservation = response.body().get(i);

                    ReservationList rl = new ReservationList();
                    rl.date = reservation.day;
                    rl.time = reservation.start_time + " ~ " + reservation.end_time + "시";

                    GPSModule gps = new GPSModule(getApplicationContext());
                    String address = gps.findAddress(reservation.lat, reservation.lng);

                    rl.location = address;

                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);

                    adapter.clear();
                    adapter.addOrderList(reqList);
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.request_list_request_button)
    void onRequestButton() {
        Intent intent = new Intent(RequestListActivity.this, RequestActivity.class);
        startActivity(intent);
    }
}
