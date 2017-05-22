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

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.fragment.CenterFragment;
import sm.cheongminapp.fragment.MainFragment;
import sm.cheongminapp.utility.PreferenceData;

public class MainActivity extends AppCompatActivity {

    public static String id = "admin1";

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView bottomNavigation;

    MainFragment mainFragment = new MainFragment();
    CenterFragment centerFragment = new CenterFragment();

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
        replaceFragment(new MainFragment());

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        replaceFragment(mainFragment);

                        return true;
                    case R.id.navigation_dashboard:
                        replaceFragment(centerFragment);
                        return true;
                }

                return false;
            }
        });
        if(!pref.getValue("regid", "").equals("")) {
            // regid 갱신하기(서버에 api만들어서)
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

    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        transition.replace(R.id.main_frame_layout, fragment);
        transition.addToBackStack(null);
        transition.commit();
    }
}
