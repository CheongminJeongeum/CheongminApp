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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.blutooth.BTConnector;
import sm.cheongminapp.fragment.HomeFragment;
import sm.cheongminapp.fragment.RequestFragment;
import sm.cheongminapp.model.ProfileModel;
import sm.cheongminapp.model.ResultModel;
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

/*
        TODO: 블루투스 작업시 주석 해제
        btc = new BTConnector(this);

        //if(mode == 0) {
        btc.checkBluetooth();
        //}
*/

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
                    .enqueue(new Callback<ResultModel>() {
                        @Override
                        public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                        }

                        @Override
                        public void onFailure(Call<ResultModel> call, Throwable t) {

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
        }catch(Exception e){}*/
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            // java.lang.NullPointerException: Attempt to read from field 'android.content.ServiceConnection sm.cheongminapp.blutooth.BTConnector.conn' on a null object reference
            finish();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }
}
