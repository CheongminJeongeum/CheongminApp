package sm.cheongminapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.blutooth.BTConnector;
import sm.cheongminapp.fragment.FriendFragment;
import sm.cheongminapp.fragment.HomeFragment;
import sm.cheongminapp.fragment.RequestFragment;
import sm.cheongminapp.model.ProfileModel;
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.model.data.EmptyData;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.utility.PreferenceData;

public class MainActivity extends AppCompatActivity {

    public static String id = "admin1";
    public static int mode = 1; // 0 : 농, 1 : 청
    public static Context context = null;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView bottomNavigation;

    HomeFragment homeFragment = new HomeFragment();
    RequestFragment requestFragment = new RequestFragment(this);

    PreferenceData pref = new PreferenceData(this);
    BTConnector btc;

    String[][] signFileNames = {{"action.csv", "goreum.csv", "healthy.csv"}};
    String[] fingerFileNames = {"상황_병원.csv", "상황_회사.csv"};
    String[][] vocaList = {{"가래", "간호사", "건강", "고름", "곪다", "기침", "몸살", "문병", "발목",
     "복통", "빈혈", "설사", "수술", "심장마비", "아프다", "암", "약국", "열", "의사", "임신", "입원",
      "저혈압", "주사", "퇴원", "폐렴"}, {"경력", "근무", "대리", "대리(직급)", "부장", "사원", "사장", "서류",
       "야근", "월급", "출근", "출근", "출장", "취업", "퇴근", "회사원", "회의", "휴가"}};
    // 레퍼런스 자이로 데이터
    List<List<List<List<Float>>>> zairoReferenceData = new ArrayList<List<List<List<Float>>>>();
    // 인풋 자이로 데이터
    List<List<Float>> zairoInputData = new ArrayList<List<Float>>();
    // 레퍼런스 손가락 데이터
    List<List<List<List<Integer>>>> fingerReferenceData = new ArrayList<List<List<List<Integer>>>>();
    // 인풋 손가락 데이터
    List<List<Integer>> fingerInputData = new ArrayList<List<Integer>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버터나이프 바인딩
        ButterKnife.bind(this);

        // 툴바 설정
        setSupportActionBar(toolbar);

        // 기본 페이지 설정
        replaceFragment(homeFragment);

        context = this;
        Intent infoIntent = getIntent();
        id = infoIntent.getStringExtra("id");
        getAndSetMode();

        //loadReferenceFingersData();


        btc = new BTConnector(this);

        //if(mode == 0) {
        btc.checkBluetooth();
        //}



        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        replaceFragment(homeFragment);

                        return true;
                    case R.id.navigation_dashboard:
                        replaceFragment(requestFragment);
                        return true;
                }

                return false;
            }
        });

        // regid 갱신하기(서버에 api만들어서)
        if(!pref.getValue("regid", "").equals("")) {
            IApiService apiService = ApiService.getInstance().getService();
            apiService.RegId(MainActivity.id, FirebaseInstanceId.getInstance().getToken())
                    .enqueue(new Callback<ResultModel<EmptyData>>() {
                        @Override
                        public void onResponse(Call<ResultModel<EmptyData>> call, Response<ResultModel<EmptyData>> response) {
                        }

                        @Override
                        public void onFailure(Call<ResultModel<EmptyData>> call, Throwable t) {

                        }
                    });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO:디버그용 토스트 메세지 삭제
        Toast.makeText(MainActivity.this, Integer.toString(requestCode), Toast.LENGTH_SHORT).show();

        if(requestCode == BTConnector.REQUEST_ENABLE_BT) {
            if(resultCode == RESULT_OK) { // 블루투스 활성화 상태
                btc.selectDevice();
            }
            else if(resultCode == RESULT_CANCELED) { // 블루투스 비활성화 상태 (종료)
                Toast.makeText(getApplicationContext(), "블루투스를 사용할 수 없어 프로그램을 종료합니다",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        } else if(requestCode == RequestFragment.REQ_CODE_REFRESH_REQUESTS) {
            requestFragment.onActivityResult(requestCode, resultCode, data);
        } else if(requestCode == FriendFragment.REQ_CODE_REFRESH_FRIENDS) {
            homeFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getAndSetMode() {
        IApiService apiService = ApiService.getInstance().getService();
        apiService.getProfile(id).enqueue(new Callback<ResultModel<ProfileModel>>() {
            @Override
            public void onResponse(Call<ResultModel<ProfileModel>> call, Response<ResultModel<ProfileModel>> response) {
                if (response.isSuccessful() == false) {
                    Log.e("error", String.valueOf(response.code()));
                    return;
                }
                mode = response.body().Data.Option;
                Log.d("UserMode", String.valueOf(response.body().Data.Option));
            }

            @Override
            public void onFailure(Call<ResultModel<ProfileModel>> call, Throwable t) {

            }
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        transition.replace(R.id.main_frame_layout, fragment);
        transition.addToBackStack(null);
        transition.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //TODO: 블루투스 작업시 주석 해제
        /*
        //if(mode == 0) {
            unbindService(btc.conn);
        //}
        try{
            unregisterReceiver(btc.mBackgroundReceiver);
        }catch(Exception e){}
        */
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            finish();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }


    public void loadReferenceZairoData() {
        for(int outer = 0; outer<signFileNames.length; outer++) {
            List<List<List<Float>>> zairoData = new ArrayList<List<List<Float>>>();
            for (int i = 0; i < signFileNames[outer].length; i++) {
                try {
                    CSVReader reader = new CSVReader(
                            new InputStreamReader(getAssets().open(signFileNames[outer][i])));
                    String[] lines;
                    List<List<Float>> entireCsvData = new ArrayList<List<Float>>();

                    if (reader.readNext() == null) continue; // 첫줄의 레이블(x1, x2, x3...제거)
                    while ((lines = reader.readNext()) != null) {
                        List<Float> lineData = new ArrayList<Float>();
                        for (int j = 0; j < lines.length; j++) {
                            Log.d("line", lines[j]);
                            lineData.add(Float.parseFloat(lines[j]));
                        }
                        entireCsvData.add(lineData);
                    }
                    zairoData.add(entireCsvData);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            zairoReferenceData.add(zairoData);
        }
    }

    public void loadReferenceFingersData() {
        // 최대 3번의 손가락 형태 변화가 있을 수 있으므로
        for(int outer=0; outer<fingerFileNames.length; outer++) {
            fingerReferenceData.add(new ArrayList<List<List<Integer>>>());
            for (int i = 0; i < 3; i++) {
                fingerReferenceData.get(outer).add(new ArrayList<List<Integer>>());
            }

            try {
                CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open(fingerFileNames[outer])));
                String[] lines;

                while ((lines = reader.readNext()) != null) {
                    for (int j = 1; j < lines.length; j++) {
                        Log.d("finger", lines[j]);
                        List<Integer> lineData = new ArrayList<Integer>();
                        for (int k = 0; k < lines[j].length(); k++) {
                            if (lines[j].charAt(k) == '-') {
                                lineData.add(-1);
                            } else {
                                lineData.add(lines[j].charAt(k) - '0');
                            }
                        }
                        fingerReferenceData.get(outer).get(j-1).add(lineData);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
