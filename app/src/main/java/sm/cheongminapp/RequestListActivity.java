package sm.cheongminapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import sm.cheongminapp.view.adapter.AbstractAdapter;
import sm.cheongminapp.view.adapter.RequestAdapter;
import sm.cheongminapp.data.RequestList;

public class RequestListActivity extends AppCompatActivity {
    ListView listView;
    AbstractAdapter<RequestList> adapter;
    ArrayList<RequestList> reqList = new ArrayList<RequestList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        listView = (ListView) findViewById(R.id.list_request);

        adapter = new RequestAdapter(this);

        RequestList l1 = new RequestList();
        RequestList l2 = new RequestList();
        l1.location = "ㅎㅎㅈㄷㄱ";
        l1.centerName = "wfew";
        l2.location = "ㅎfewㄱ";
        l2.centerName = "vssew";
        reqList.add(l1);
        reqList.add(l2);
        adapter.addOrderList(reqList);
        listView.setAdapter(adapter);
    }
}
