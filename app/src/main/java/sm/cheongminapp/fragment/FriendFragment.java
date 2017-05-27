package sm.cheongminapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.data.Friend;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.view.adapter.FriendAdapter;
import sm.cheongminapp.R;

public class FriendFragment extends Fragment {

    @BindView(R.id.fragment_friend_list_view)
    ListView lvFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        ButterKnife.bind(this, view);

        final FriendAdapter adapter = new FriendAdapter(getActivity());
        lvFriend.setAdapter(adapter);
        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.addItem(new Friend("admin1", "박통역", ""));
        adapter.addItem(new Friend("admin2", "김수화", ""));

        return view;
    }
}
