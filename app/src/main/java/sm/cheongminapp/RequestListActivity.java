package sm.cheongminapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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
    ListView listView;
    AbstractAdapter<ReservationList> adapter;
    ArrayList<ReservationList> reqList = new ArrayList<ReservationList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        listView = (ListView) findViewById(R.id.list_request);

        adapter = new ResponseAdapter(this);
        adapter.addOrderList(reqList);
        listView.setAdapter(adapter);

        IApiService apiService = ApiService.getInstance().getService();
        apiService.getMyReservations(MainActivity.id).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    Reservation reserv = response.body().get(i);
                    ReservationList rl = new ReservationList();
                    rl.date = reserv.day;
                    rl.time = reserv.start_time + " ~ " + reserv.end_time + "시";

                    GPSModule gps = new GPSModule(getApplicationContext());
                    String address = gps.findAddress(reserv.lat, reserv.lng);
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
                    listView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {

            }
        });
    }
}
