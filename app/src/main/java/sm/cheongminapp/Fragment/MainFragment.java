package sm.cheongminapp.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import sm.cheongminapp.Adapter.ViewPagerAdapter;
import sm.cheongminapp.R;

public class MainFragment extends Fragment {


    @BindView(R.id.fragment_main_tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.fragment_main_view_pager)
    ViewPager viewPager;

    Unbinder unbinder;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // 버터나이프 바인딩
        unbinder = ButterKnife.bind(this, view);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment("친구", new FriendFragment());

        viewPager.setAdapter(adapter);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
