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
                Friend friend = (Friend)adapter.getItem(position);

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("Friend", friend);
                startActivity(intent);
            }
        });


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

                adapter.clear();
                for(int i = 0; i < responseData.size(); i++) {
                    adapter.addItem(responseData.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResultModel<List<Friend>>> call, Throwable t) {
                Toast.makeText(getActivity(), "친구목록 요청 실패", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
