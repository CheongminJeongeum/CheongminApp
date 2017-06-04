package sm.cheongminapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sm.cheongminapp.data.Friend;

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

        tvName.setText(friend.Name);

        // TODO: 프로필 이미지 설정
    }

    @OnClick(R.id.profile_open_chat_btn)
    void openChat() {
        // TODO: 친구와 대화 시작
        Toast.makeText(this, "대화 시작", Toast.LENGTH_SHORT).show();
    }

}
