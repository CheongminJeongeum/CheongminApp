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
import sm.cheongminapp.ProfileActivity;
import sm.cheongminapp.data.Friend;
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.view.adapter.FriendAdapter;
import sm.cheongminapp.R;

public class FriendFragment extends Fragment {

    public final static int REQ_CODE_REFRESH_FRIENDS = 102;

    @BindView(R.id.fragment_friend_list_view)
    ListView lvFriend;

    FriendAdapter freindAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        ButterKnife.bind(this, view);

        freindAdapter = new FriendAdapter(getContext());

        lvFriend.setAdapter(freindAdapter);
        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = (Friend)freindAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("Friend", friend);
                getActivity().startActivityForResult(intent, REQ_CODE_REFRESH_FRIENDS);
            }
        });

        updateFriendList();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_REFRESH_FRIENDS:
                updateFriendList();
                break;
        }
    }

    public void updateFriendList() {
        IApiService service = ApiService.getInstance().getService();
        service.getFriends().enqueue(new Callback<ResultModel<List<Friend>>>() {
            @Override
            public void onResponse(Call<ResultModel<List<Friend>>> call, Response<ResultModel<List<Friend>>> response) {
                if(response.isSuccessful() == false) {
                    Toast.makeText(getActivity(), "친구목록 요청 실패", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Friend> responseData = response.body().Data;
                if(responseData.size() <= 0) {
                    Toast.makeText(getActivity(), "친구가 없음", Toast.LENGTH_SHORT).show();
                    return;
                }

                freindAdapter.clear();
                for(int i = 0; i < responseData.size(); i++) {
                    freindAdapter.addItem(responseData.get(i));
                }
                freindAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResultModel<List<Friend>>> call, Throwable t) {
                Toast.makeText(getActivity(), "친구목록 요청 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
