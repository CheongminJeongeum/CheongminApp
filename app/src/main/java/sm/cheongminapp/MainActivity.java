package sm.cheongminapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.fragment.CenterFragment;
import sm.cheongminapp.fragment.HomeFragment;
import sm.cheongminapp.fragment.RequestFragment;
import sm.cheongminapp.model.Profile;
import sm.cheongminapp.model.Result;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.utility.PreferenceData;

public class MainActivity extends AppCompatActivity {

    public static String id = "admin1";
    public static int mode = 1; // 0 : 농, 1 : 청

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView bottomNavigation;

    HomeFragment homeFragment = new HomeFragment();
    RequestFragment requestFragment = new RequestFragment(this);

    PreferenceData pref = new PreferenceData(this);

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

        Intent infoIntent = getIntent();
        id = infoIntent.getStringExtra("id");
        getAndSetMode();

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
                    .enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {

                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {

                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(MainActivity.this, Integer.toString(requestCode), Toast.LENGTH_SHORT).show();
        if (requestCode == CenterFragment.reqCode) {
            if(resultCode == Activity.RESULT_OK){
                String location = data.getStringExtra("location");
                int centerId = data.getIntExtra("centerId", -1);
                Intent intent = new Intent(MainActivity.this, RequestActivity.class);
                intent.putExtra("centerId", centerId);
                intent.putExtra("location", location);
                startActivity(intent);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
    }

    private void getAndSetMode() {
        IApiService apiService = ApiService.getInstance().getService();
        apiService.GetProfile(id).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                mode = response.body().option;
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

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
}
