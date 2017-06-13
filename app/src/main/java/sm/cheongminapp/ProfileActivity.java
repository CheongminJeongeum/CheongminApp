package sm.cheongminapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.data.ChatRoom;
import sm.cheongminapp.data.Friend;
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.network.ApiHelper;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_name_text)
    TextView tvName;

    @BindView(R.id.profile_image)
    ImageView ivProfile;

    Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        friend = (Friend) getIntent().getSerializableExtra("Friend");

        Picasso.with(getApplicationContext())
                .load(ApiHelper.GetProfileImageUrl(friend.ID))
                .placeholder(R.drawable.profile)
                .into(ivProfile);

        tvName.setText(friend.Name);

        // TODO: 프로필 이미지 설정
    }

    @OnClick(R.id.profile_open_chat_btn)
    void openChat() {
        IApiService apiService = ApiService.getInstance().getService();
        apiService.createChatRoom(MainActivity.id, friend.ID).enqueue(new Callback<ResultModel<ChatRoom>>() {
            @Override
            public void onResponse(Call<ResultModel<ChatRoom>> call, Response<ResultModel<ChatRoom>> response) {
                if(response.isSuccessful() == false) {
                    Toast.makeText(ProfileActivity.this, "방 생성 요청 실패", Toast.LENGTH_SHORT).show();
                    return;
                }

                ChatRoom room = response.body().Data;

                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                intent.putExtra("ChatRoom", room);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResultModel<ChatRoom>> call, Throwable t) {

            }
        });
    }

}
