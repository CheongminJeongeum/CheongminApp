package sm.cheongminapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import sm.cheongminapp.view.adapter.AbstractAdapter;
import sm.cheongminapp.view.adapter.RequestAdapter;
import sm.cheongminapp.data.ReservationList;

public class RequestListActivity extends AppCompatActivity {
    ListView listView;
    AbstractAdapter<ReservationList> adapter;
    ArrayList<ReservationList> reqList = new ArrayList<ReservationList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        listView = (ListView) findViewById(R.id.list_request);

        adapter = new RequestAdapter(this);

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
