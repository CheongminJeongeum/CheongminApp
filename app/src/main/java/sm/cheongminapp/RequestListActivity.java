package sm.cheongminapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
/*
        IApiService apiService = ApiService.getInstance().getService();
        apiService.getMyReservations(MainActivity.id).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                for(int i=0; i<response.body().size(); i++) {
                    Reservation reserv = response.body().get(i);
                    Log.i("예약 목록", reserv.reservation_info);
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {

            }
        });
*/
        adapter = new ResponseAdapter(this);

        ReservationList l1 = new ReservationList();
        ReservationList l2 = new ReservationList();
        l1.location = "ㅎㅎㅈㄷㄱ";
        l1.centerName = "wfew";
        l1.date = "2017년 8월 8일";
        l1.time = "1시 3시";
        l2.location = "ㅎfewㄱ";
        l2.centerName = "vssew";
        l2.date = "2017년 8월 8일";
        l2.time = "1시 3시";
        reqList.add(l1);
        reqList.add(l2);
        adapter.addOrderList(reqList);
        listView.setAdapter(adapter);
    }
}
