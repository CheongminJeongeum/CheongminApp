package sm.cheongminapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import sm.cheongminapp.R;
import sm.cheongminapp.ReserveInfoActivity;
import sm.cheongminapp.data.Reservation;
import sm.cheongminapp.data.ReservationData;
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

    ResponseAdapter adapter;
    ArrayList<Reservation> reservationsList = new ArrayList<>();

    Context ctx;

    public RequestFragment(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        ButterKnife.bind(this, view);

        adapter = new ResponseAdapter(ctx);
        adapter.addOrderList(reservationsList);

        lvRequestList.setAdapter(adapter);

        // 사용자의 예약 목록 요청
        IApiService apiService = ApiService.getInstance().getService();
        apiService.getMyReservations(MainActivity.id).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                // 요청 실패 (errorBody를 통해 정보를 얻어 올 수 있음)
                if(response.isSuccessful() == false) {
                    return;
                }

                adapter.clear();

                for (int i = 0; i < response.body().size(); i++) {
                    Reservation reservation = response.body().get(i);
                    adapter.addItem(reservation);
                }

                adapter.notifyDataSetChanged();
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
        Reservation reservation = (Reservation)adapter.getItem(position);

        Intent intent = new Intent(getActivity(), ReserveInfoActivity.class);
        intent.putExtra("Reservation", reservation);

        startActivity(intent);
    }


    @OnClick(R.id.fragment_request_request_button)
    void onRequestButton() {
        //센터 검색 액티비티 시작
        Intent intent = new Intent(getActivity(), CenterActivity.class);
        startActivity(intent);
    }
}
