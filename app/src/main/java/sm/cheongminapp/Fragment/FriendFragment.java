package sm.cheongminapp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import sm.cheongminapp.FriendListAdapter;
import sm.cheongminapp.R;

public class FriendFragment extends Fragment {

    ListView friendList;

    public FriendFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        FriendListAdapter adapter = new FriendListAdapter();

        friendList = (ListView)view.findViewById(R.id.friend_list);
        friendList.setAdapter(adapter);

        for(int i = 0; i < 100; i++)
        {
            adapter.addItem(getContext().getDrawable(R.drawable.ic_account), "이름" + i, "상태 메세지");
        }

        return view;
    }
}
