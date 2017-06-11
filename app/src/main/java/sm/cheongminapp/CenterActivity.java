package sm.cheongminapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.model.CenterModel;
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.view.adapter.CenterAdapter;

public class CenterActivity extends AppCompatActivity {

    public static final int requestCode = 400;

    @BindView(R.id.center_toolbar)
    Toolbar toolbar;

    @BindView(R.id.center_header_text)
    TextView tvHeaderText;

    @BindView(R.id.center_search_text)
    EditText etSearchText;

    @BindView(R.id.center_list_view)
    ListView lvCenterList;

    private CenterAdapter centerAdapter;

    private ArrayList<CenterModel> recentCenterModelList = new ArrayList<>();
    private ArrayList<CenterModel> centerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // 어댑터 설정
        centerAdapter = new CenterAdapter(this);
        lvCenterList.setAdapter(centerAdapter);

        // 미리 센터를 모두 받아와서 검색할때 검색된 것만 보여줍니다
        IApiService apiService = ApiService.getInstance().getService();
        apiService.getCenters().enqueue(new Callback<List<CenterModel>>() {
            @Override
            public void onResponse(Call<List<CenterModel>> call, Response<List<CenterModel>> response) {
                if(response.isSuccessful() == false)
                {
                    return;
                }

                List<CenterModel> centerModelList = response.body();

                for(int i = 0; i < centerModelList.size(); i++) {
                    centerList.add(centerModelList.get(i));
                }
            }

            @Override
            public void onFailure(Call<List<CenterModel>> call, Throwable t) {

            }
        });


        // 이전에 이용한 센터
        CenterModel center = new CenterModel();
        center.ID = 0;
        center.Name = "강남구";

        recentCenterModelList.add(center);

        tvHeaderText.setText("최근 요청한 통역 센터");

        centerAdapter.clear();
        centerAdapter.addOrderList(recentCenterModelList);
        centerAdapter.notifyDataSetChanged();

        etSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        String searchText = etSearchText.getText().toString();
                        ArrayList<CenterModel> searchedCenterList =new ArrayList<>();

                        for(CenterModel center: centerList) {
                            if(center.Name.contains(searchText)) {
                                searchedCenterList.add(center);
                            }
                        }

                        tvHeaderText.setText("검색 결과");

                        centerAdapter.clear();
                        centerAdapter.addOrderList(searchedCenterList);
                        centerAdapter.notifyDataSetChanged();

                        break;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @OnItemClick(R.id.center_list_view)
    public void onItemClick(AdapterView<?> parent, int position) {
        CenterModel centerModel = (CenterModel)centerAdapter.getItem(position);

        Intent intent = new Intent(CenterActivity.this, MapsActivity.class);
        intent.putExtra("centerId", centerModel.ID);

        startActivityForResult(intent, requestCode);
        finish();
    }
}
