package sm.cheongminapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.data.ChatRoom;
import sm.cheongminapp.data.Friend;
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.model.data.EmptyData;
import sm.cheongminapp.network.ApiHelper;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQ_CODE_READ_STROAGE = 100;
    private static final int REQ_CODE_PICK_IMAGE = 200;

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_READ_STROAGE: {
                // 동의시 이미지 선택으로 넘어갑니다
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickProfileImage();
                } else {
                    Toast.makeText(this, "권한사용을 동의해주셔야 이용이 가능합니다." , Toast.LENGTH_LONG ).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQ_CODE_PICK_IMAGE:
                if(resultCode == Activity.RESULT_OK) {
                    uploadProfileImage(data.getData());
                }
                break;
        }
    }

    @OnClick(R.id.profile_open_chat_btn)
    void onClickOpenChat() {
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

    @OnClick(R.id.profile_image)
    void onClickProfile() {
        // 권한 확인
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CODE_READ_STROAGE);
        } else {
            pickProfileImage();
        }
    }

    void pickProfileImage() {
        Intent intent =new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
    }

    void uploadProfileImage(Uri imageUrl) {
        File file = new File(getPathFromUri(this, imageUrl));

        RequestBody body = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUrl)), file);
        MultipartBody.Part multipart = MultipartBody.Part.createFormData("file", file.getName(), body);

        IApiService apiService = ApiService.getInstance().getService();
        apiService.uploadProfileImage(MainActivity.id, multipart).enqueue(new Callback<ResultModel<EmptyData>>() {
            @Override
            public void onResponse(Call<ResultModel<EmptyData>> call, Response<ResultModel<EmptyData>> response) {
                Picasso.with(getApplicationContext())
                        .load(ApiHelper.GetProfileImageUrl(friend.ID))
                        .placeholder(R.drawable.profile)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(ivProfile);
            }

            @Override
            public void onFailure(Call<ResultModel<EmptyData>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String getPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
