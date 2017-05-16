package sm.cheongminapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import sm.cheongminapp.Fragment.CenterFragment;
import sm.cheongminapp.Fragment.FriendFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 기본 페이지 설정
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new FriendFragment()).commit();

        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, new FriendFragment()).commit();

                        return true;
                    case R.id.navigation_dashboard:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, new CenterFragment()).commit();

                        return true;
                }

                return false;
            }
        });
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
}
