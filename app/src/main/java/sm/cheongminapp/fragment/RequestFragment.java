package sm.cheongminapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public static final int REQ_CODE_REFRESH_REQUESTS = 101;

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

        updateRequestList();

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
        getActivity().startActivityForResult(intent, REQ_CODE_REFRESH_REQUESTS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_REFRESH_REQUESTS:
                updateRequestList();
                break;
        }
    }

    public void updateRequestList() {
        final ArrayList<Reservation> reservationList = new ArrayList<>();

        IApiService apiService = ApiService.getInstance().getService();
        Callback<ResultModel<List<Reservation>>> callback = new Callback<ResultModel<List<Reservation>>>() {
            @Override
            public void onResponse(Call<ResultModel<List<Reservation>>> call, Response<ResultModel<List<Reservation>>> response) {
                if(response.isSuccessful() == false || response.body() == null) {
                    Toast.makeText(ctx, "getReservations() : Fail", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(Reservation reservation : response.body().Data) {
                    reservationList.add(reservation);
                }

                Collections.sort(reservationList, new Comparator<Reservation>() {
                    @Override
                    public int compare(Reservation o1, Reservation o2) {
                        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);

                        Date date1 = format.parse(o1.Date, new ParsePosition(0));
                        long dateTime1 = date1.getTime();

                        Date date2 = format.parse(o2.Date, new ParsePosition(0));
                        long dateTime2 = date2.getTime();

                        return dateTime1 < dateTime2 ? 1 : -1;
                    }
                });

                reservationAdapter.clear();
                reservationAdapter.addOrderList(reservationList);
                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResultModel<List<Reservation>>> call, Throwable t) {
                // List<Reservation> 형태로 변환하지 못하면 onFailure가 호출되는 듯함
                // 즉, Json에 데이터가 비어있을때에도 호출됨
            }
        };

        apiService.getMyReservations(MainActivity.id).enqueue(callback);
        apiService.getMyRequests(MainActivity.id).enqueue(callback);
    }
}
