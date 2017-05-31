package sm.cheongminapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.MainActivity;
import sm.cheongminapp.R;
import sm.cheongminapp.RequestActivity;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        //ButterKnife.bind(getActivity());
        //
        ButterKnife.bind(this, view);

        adapter = new ResponseAdapter(ctx);
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

                    Log.d("ㅅㅂ", rl.time);
                    GPSModule gps = new GPSModule(ctx);

                    rl.location = gps.findAddress(reservation.lat, reservation.lng);

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

            }
        });

        return view;
    }

    @OnClick(R.id.fragment_request_request_button)
    void onRequestButton() {
        Intent intent = new Intent(getActivity(), RequestActivity.class);
        startActivity(intent);
    }
}
