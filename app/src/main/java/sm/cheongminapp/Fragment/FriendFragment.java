package sm.cheongminapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import sm.cheongminapp.Adapter.FriendListAdapter;
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

        adapter.addItem(getContext().getDrawable(R.drawable.ic_account), "박청인");
        adapter.addItem(getContext().getDrawable(R.drawable.ic_account), "김농인");

        return view;
    }
}
