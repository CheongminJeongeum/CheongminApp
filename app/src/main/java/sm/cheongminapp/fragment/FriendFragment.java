package sm.cheongminapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import sm.cheongminapp.ProfileActivity;
import sm.cheongminapp.data.Friend;
import sm.cheongminapp.view.adapter.FriendAdapter;
import sm.cheongminapp.R;

public class FriendFragment extends Fragment {

    ListView friendList;

    public FriendFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        FriendAdapter adapter = new FriendAdapter(getActivity());

        friendList = (ListView)view.findViewById(R.id.friend_list);
        friendList.setAdapter(adapter);

        Friend friend1 = new Friend();
        friend1.setIconDrawable(getContext().getDrawable(R.drawable.ic_account));
        friend1.setName("김농인");

        adapter.addItem(friend1);

        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);

        return view;
    }
}
