package sm.cheongminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import sm.cheongminapp.data.Center;
import sm.cheongminapp.view.adapter.CenterAdapter;

public class CenterActivity extends AppCompatActivity {

    public static final int requestCode = 400;

    @BindView(R.id.center_toolbar)
    Toolbar toolbar;

    @BindView(R.id.center_search_text)
    EditText etSearchText;

    @BindView(R.id.center_list_view)
    ListView lvCenterList;

    private CenterAdapter centerAdapter;
    private ArrayList<Center> recentCenterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // DB로 부터 최근 요청한 센터 목록을 받아옴
        Center sampleCenter = new Center("임시 통역 센터", 0, 0, "정보", "010-1234-1234");
        recentCenterList.add(sampleCenter);

        // 어댑터 설정
        centerAdapter = new CenterAdapter(this);
        centerAdapter.addOrderList(recentCenterList);

        lvCenterList.setAdapter(centerAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @OnItemClick(R.id.center_list_view)
    public void onItemClick(AdapterView<?> parent, int position) {
        Center center = (Center)centerAdapter.getItem(position);

        Intent intent = new Intent(CenterActivity.this, MapsActivity.class);
        intent.putExtra("centerId", center.center_id);

        startActivityForResult(intent, requestCode);
    }
}
