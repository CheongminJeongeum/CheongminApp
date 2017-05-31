package sm.cheongminapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.CenterActivity;
import sm.cheongminapp.MainActivity;
import sm.cheongminapp.MapsActivity;
import sm.cheongminapp.R;
import sm.cheongminapp.RequestActivity;
import sm.cheongminapp.ReserveInfoActivity;
import sm.cheongminapp.data.Reservation;
import sm.cheongminapp.data.ReservationList;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.utility.GPSModule;
import sm.cheongminapp.view.adapter.AbstractAdapter;
import sm.cheongminapp.view.adapter.ResponseAdapter;

/**
 * Created by user on 2017. 5. 31..
 */
public class RequestFragment extends Fragment {

    @BindView(R.id.fragment_request_list_view)
    ListView lvRequestList;

    AbstractAdapter<ReservationList> adapter;
    ArrayList<ReservationList> reqList = new ArrayList<>();

    Context ctx;

    public RequestFragment(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        ButterKnife.bind(this, view);

        adapter = new ResponseAdapter(ctx);
        adapter.addOrderList(reqList);

        lvRequestList.setAdapter(adapter);

        // 샘플 예제
        ReservationList sampleItem = new ReservationList();
        sampleItem.CenterName = "센터 이름?";
        sampleItem.Reason = "왜 안사유";
        sampleItem.Time = "1시~3시";
        sampleItem.Date = "2017년 5월 1일";
        sampleItem.Location = "서울특별시 어딘가 어딘가";
        sampleItem.LocationDetail = "몇층 몇동?";

        adapter.addItem(sampleItem);

        IApiService apiService = ApiService.getInstance().getService();
        apiService.getMyReservations(MainActivity.id).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                // 요청 실패 (errorBody를 통해 정보를 얻어 올 수 있음)
                if(response.isSuccessful() == false) {
                    return;
                }

                for (int i = 0; i < response.body().size(); i++) {
                    Reservation reservation = response.body().get(i);
                    ReservationList rl = new ReservationList();
                    rl.Date = reservation.day;
                    rl.Time = reservation.start_time + " ~ " + reservation.end_time + "시";

                    GPSModule gps = new GPSModule(ctx);
                    rl.Location = gps.findAddress(reservation.lat, reservation.lng);

                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);
                    reqList.add(rl);

                    adapter.clear();
                    adapter.addOrderList(reqList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e("onFailure", "getMyReservations");
            }
        });

        return view;
    }

    @OnItemClick(R.id.fragment_request_list_view)
    void onItemClick(AdapterView<?> parent, int position) {
        ReservationList reserve = (ReservationList)adapter.getItem(position);

        Intent intent = new Intent(getActivity(), ReserveInfoActivity.class);
        intent.putExtra("ReservationList", reserve);

        startActivity(intent);
    }


    @OnClick(R.id.fragment_request_request_button)
    void onRequestButton() {
        //센터 검색 액티비티 시작
        Intent intent = new Intent(getActivity(), CenterActivity.class);
        startActivity(intent);
    }
}
