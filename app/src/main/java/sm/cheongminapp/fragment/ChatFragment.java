package sm.cheongminapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.MainActivity;
import sm.cheongminapp.model.Result;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.view.adapter.ChatRoomAdapter;
import sm.cheongminapp.ChatActivity;
import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatRoom;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    @BindView(R.id.fragment_chat_list)
    ListView lvChatList;

    ChatRoomAdapter chatRoomAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, view);

        chatRoomAdapter = new ChatRoomAdapter(getActivity());

        lvChatList.setAdapter(chatRoomAdapter);
        lvChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatRoom chatRoom = (ChatRoom)chatRoomAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("ChatRoom", chatRoom);
                startActivity(intent);
            }
        });

        // 방 목록 받아오기
        IApiService apiService = ApiService.getInstance().getService();
        apiService.getChatRooms(MainActivity.id).enqueue(new Callback<Result<List<ChatRoom>>>() {
            @Override
            public void onResponse(Call<Result<List<ChatRoom>>> call, Response<Result<List<ChatRoom>>> response) {
                if(response.isSuccessful() == false) {
                    Toast.makeText(getActivity(), "채팅방 목록 요청 실패", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<ChatRoom> chatRoomList = response.body().Data;

                if(chatRoomList.size() <= 0) {
                    Toast.makeText(getActivity(), "채팅방 목록 없음", Toast.LENGTH_SHORT).show();
                    return;
                }

                chatRoomAdapter.clear();

                for(int i = 0; i < chatRoomList.size(); i++) {
                    chatRoomAdapter.addItem(chatRoomList.get(i));
                }

                chatRoomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Result<List<ChatRoom>>> call, Throwable t) {
                Toast.makeText(getActivity(), "채팅방 목록 요청 실패", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
