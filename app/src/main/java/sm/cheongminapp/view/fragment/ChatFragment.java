package sm.cheongminapp.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.view.adapter.ChatRoomAdapter;
import sm.cheongminapp.ChatActivity;
import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatRoom;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    @BindView(R.id.fragment_chat_list)
    ListView listView;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, view);

        final ChatRoomAdapter adapter = new ChatRoomAdapter();
        adapter.addItem(getContext().getDrawable(R.drawable.ic_account), "김농인", "안녕하세요");
        adapter.addItem(getContext().getDrawable(R.drawable.ic_account), "농옹", "안녕하세요");

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatRoom chatRoom = (ChatRoom)adapter.getItem(position);

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
