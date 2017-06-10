package sm.cheongminapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

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

    @BindView(R.id.center_search_text)
    EditText etSearchText;

    @BindView(R.id.center_list_view)
    ListView lvCenterList;

    private CenterAdapter centerAdapter;
    private ArrayList<CenterModel> recentCenterModelList = new ArrayList<>();

    private ArrayList<CenterModel> centerModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // DB로 부터 최근 요청한 센터 목록을 받아옴
        CenterModel centerModel = new CenterModel();
        centerModel.Name = "최근 통역 센터1";
        centerModel.ID = 0;

        recentCenterModelList.add(centerModel);

        // 어댑터 설정
        centerAdapter = new CenterAdapter(this);
        //centerAdapter.addOrderList(recentCenterModelList);

        lvCenterList.setAdapter(centerAdapter);

        // 센터 요청
        IApiService apiService = ApiService.getInstance().getService();
        apiService.getCenters().enqueue(new Callback<ResultModel<List<CenterModel>>>() {
            @Override
            public void onResponse(Call<ResultModel<List<CenterModel>>> call, Response<ResultModel<List<CenterModel>>> response) {
                if(response.isSuccessful() == false)
                {
                    return;
                }

                List<CenterModel> centerModelList = response.body().Data;
                for(int i = 0; i < centerModelList.size(); i++) {
                    centerModelList.add(centerModelList.get(i));
                }
            }

            @Override
            public void onFailure(Call<ResultModel<List<CenterModel>>> call, Throwable t) {

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
        //intent.putExtra("centerId", centerModel.center_id);

        startActivityForResult(intent, requestCode);
        finish();
    }
}
