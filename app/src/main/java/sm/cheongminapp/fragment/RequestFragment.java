package sm.cheongminapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.view.adapter.ReservationAdapter;

/**
 * Created by user on 2017. 5. 31..
 */
public class RequestFragment extends Fragment {

    @BindView(R.id.fragment_request_list_view)
    ListView lvRequestList;

    ReservationAdapter reservationAdapter;

    Context ctx;

    public RequestFragment(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        ButterKnife.bind(this, view);

        reservationAdapter = new ReservationAdapter(ctx);

        lvRequestList.setAdapter(reservationAdapter);

        // 예약 목록
        IApiService apiService = ApiService.getInstance().getService();
        apiService.getMyReservations(MainActivity.id).enqueue(new Callback<ResultModel<List<Reservation>>>() {
            @Override
            public void onResponse(Call<ResultModel<List<Reservation>>> call, Response<ResultModel<List<Reservation>>> response) {
                // 요청 실패 (errorBody를 통해 정보를 얻어 올 수 있음)
                if(response.isSuccessful() == false) {
                    Toast.makeText(ctx, "요청 실패", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Reservation> reservationList = response.body().Data;

                reservationAdapter.clear();

                for (int i = 0; i < reservationList.size(); i++) {
                    Reservation reservation = reservationList.get(i);
                    reservationAdapter.addItem(reservation);
                }

                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResultModel<List<Reservation>>> call, Throwable t) {
                Toast.makeText(ctx, "요청 실패", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @OnItemClick(R.id.fragment_request_list_view)
    void onItemClick(AdapterView<?> parent, int position) {
        Reservation reservation = (Reservation)reservationAdapter.getItem(position);

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
