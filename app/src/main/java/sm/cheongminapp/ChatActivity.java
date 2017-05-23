package sm.cheongminapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.data.ChatObject;
import sm.cheongminapp.data.ChatSignData;
import sm.cheongminapp.data.SignData;
import sm.cheongminapp.database.DBHelper;
import sm.cheongminapp.view.adapter.ChatMessageAdapter;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_toolbar)
    Toolbar toolbar;

    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.chat_message)
    EditText editText;

    @BindView(R.id.chat_send)
    Button button;

    BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("chat")) {
                int room_id = intent.getIntExtra("room_id", -1);
                String contents = intent.getStringExtra("contents");
                Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // 대화방 이름 설정
        getSupportActionBar().setTitle("김농인님과 대화");

        // 메세지 어댑터
        ChatMessageAdapter adapter = new ChatMessageAdapter(new ArrayList<ChatObject>());

        // 내가 보낸 메세지
        adapter.addChatInput("안녕하세요!");

        // 받은 메세지
        ChatSignData signData = new ChatSignData();
        signData.SignDataList.add(
                new SignData(
                        "안녕",
                        "android.resource://" + getPackageName() + "/" + R.raw.s00000001));

        adapter.addSign(signData);
        adapter.addResponseInput("안녕하세요");

        DBHelper dbHelper = new DBHelper(getApplicationContext(), "Chat.db", null, 1);
        List<ChatObject> chatList = dbHelper.getResults();
        for(int i=0; i<chatList.size(); i++) {
            /* 누구 채팅이냐에 따라 왼쪽 오른쪽 분기 */
        }

        // 설정
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        IntentFilter filter = new IntentFilter();
        filter.addAction("chat");
        registerReceiver(chatReceiver, filter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(chatReceiver);
    }
}
