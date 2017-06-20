package sm.cheongminapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.data.ChatObject;
import sm.cheongminapp.data.ChatRoom;
import sm.cheongminapp.data.ChatSignData;
import sm.cheongminapp.data.HotKey;
import sm.cheongminapp.database.DBHelper;
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.model.data.EmptyData;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.repository.SignVideoRepository;
import sm.cheongminapp.view.adapter.ChatMessageAdapter;
import sm.cheongminapp.view.adapter.HotKeyAdapter;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_toolbar)
    public Toolbar toolbar;

    @BindView(R.id.chat_recycler_view)
    public RecyclerView messageView;

    @BindView(R.id.chat_message)
    public EditText etMessage;

    @BindView(R.id.chat_send)
    public Button btnSend;

    @BindView(R.id.list_shortcuts)
    public ListView lvHotKeyList;

    @BindView(R.id.fl_activity_chat_container)
    public FrameLayout flContainer;

    @BindView(R.id.dl_activity_chat_drawer)
    public DrawerLayout dlDrawer;

    private ActionBarDrawerToggle dtToggle;

    private HotKeyAdapter hotKeyAdapter;

    private DBHelper dbHelper;
    private ChatMessageAdapter messageAdapter;

    private ChatRoom currentRoom;
    private int currentRoomId = 1;

    private Gson gson = new Gson();

    // 채팅 액티비티를 활성화하고 있을 때만 호출됨.
    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("chat")) {
                if(currentRoomId != intent.getIntExtra("room_id", -1))
                    return;

                String message = intent.getStringExtra("contents");
                String time = parseDateTime(intent.getStringExtra("time"));

                Log.d("time", time);
                Log.d("message", message);

                Log.d("mode", String.valueOf(MainActivity.mode));

                onReceiveMessage(message, time);
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

        // 채팅방 정보 설정
        currentRoom = (ChatRoom) getIntent().getSerializableExtra("ChatRoom");
        currentRoomId = currentRoom.room_id;

        getSupportActionBar().setTitle(currentRoom.room_name);

        // 네비게이션 메뉴 설정
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer,
                R.drawable.ic_drawer, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };
        dlDrawer.setDrawerListener(dtToggle);

        // 채팅 메세지 어댑터 설정
        messageAdapter = new ChatMessageAdapter();

        messageView.setAdapter(messageAdapter);
        messageView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        messageView.setItemAnimator(new DefaultItemAnimator());

        // 단축 메세지 등록
        hotKeyAdapter = new HotKeyAdapter(this);
        lvHotKeyList.setAdapter(hotKeyAdapter);

        // 메세지 리시버 등록
        IntentFilter filter = new IntentFilter();
        filter.addAction("chat");

        registerReceiver(chatReceiver, filter);

        // 수화 리시버 등록
        IntentFilter signFilter = new IntentFilter();
        filter.addAction("sign");

        registerReceiver(signReceiver, signFilter);

        // 이전 채팅 내용 불러오기
        loadChatLog();

        // 단축 메세지 불러오기
        loadHotKeyList();

        //onReceiveMessage("[\"대기\", \"고맙\", \"기다려주셔서 감사합니다\"]", "오전 11:0");
        //onReceiveMessage("[\"안녕하세요\", \"안녕하세요\"]", "오전 11:0");

        ChatSignData chatSignData = new ChatSignData();
        chatSignData.setSignDataList(SignVideoRepository.getInstance(getApplicationContext()).getSignDataList(Arrays.asList("대기", "고맙")));

        messageAdapter.addSign(chatSignData);
        //messageAdapter.addResponseMessage("기다려주셔서 감사합니다", "오전 11시 40분");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_chat_open_menu:
                dlDrawer.openDrawer(Gravity.RIGHT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.chat_send)
    public void onClickSend() {
        String message = etMessage.getText().toString();
        handleInputMessage(message);
    }

    @OnClick(R.id.chat_add_hotkey)
    public void onClickAddHotKey() {
        View dialogView = View.inflate(ChatActivity.this, R.layout.dialog_hotkey, null);

        final EditText etName = (EditText) dialogView.findViewById(R.id.dialog_hotkey_name);
        final EditText etContent = (EditText) dialogView.findViewById(R.id.dialog_hotkey_content);

        AlertDialog.Builder dlg = new AlertDialog.Builder(ChatActivity.this);
        dlg.setView(dialogView);
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HotKey hotkey = new HotKey();
                hotkey.Name = etName.getText().toString();
                hotkey.Content = etContent.getText().toString();

                Log.d("key", hotkey.Content);

                hotKeyAdapter.addItem(hotkey);
                hotKeyAdapter.notifyDataSetChanged();
            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();
    }

    @OnItemClick(R.id.list_shortcuts)
    public void onHotKeyItemClick(AdapterView<?> parent, int position) {
        HotKey hotKey = (HotKey)hotKeyAdapter.getItem(position);

        etMessage.setText(hotKey.Content);
        btnSend.callOnClick();
    }


    private void loadChatLog() {
        dbHelper = new DBHelper(getApplicationContext(), "Chat.db", null, 1);

        List<ChatObject> chatList = dbHelper.getResultsByRoomId(currentRoomId);
        for(int i=0; i<chatList.size(); i++) {
            ChatObject chat = chatList.get(i);

            String messageText = chat.getText();
            String timeText = parseDateTime(chat.getTime());

            switch(chat.getType())
            {
                case ChatObject.INPUT_OBJECT:
                    messageAdapter.addInputMessage(messageText, timeText);
                    break;
                case ChatObject.RESPONSE_OBJECT:
                    messageAdapter.addResponseMessage(messageText, timeText);
                    break;
            }
        }
    }

    private void loadHotKeyList() {
    }


    private void onReceiveMessage(String message, String time) {
        if(MainActivity.mode == 0) {
            List<String> signTextList = parseSignText(message);

            if(signTextList.size() <= 2) {
                messageAdapter.addResponseMessage(message, time);
                return;
            }

            ChatSignData signData = parseSignData(signTextList.subList(0, signTextList.size() - 1));
            messageAdapter.addSign(signData);

            String messageText = signTextList.get(signTextList.size() - 1);
            messageAdapter.addResponseMessage(messageText, time);

        } else {
            messageAdapter.addResponseMessage(message, time);
        }

        messageView.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void handleInputMessage(String message) {
        String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));

        messageAdapter.addInputMessage(message, parseDateTime(time));
        messageView.scrollToPosition(messageAdapter.getItemCount() - 1);
        etMessage.setText("");

        // DB에 저장
        dbHelper.insert(currentRoomId, MainActivity.id, message, time);

        // API 호출
        IApiService apiService = ApiService.getInstance().getService();
        if(MainActivity.mode == 1) {
            apiService.sendMessageOnKorean(MainActivity.id, currentRoomId, message).enqueue(new Callback<ResultModel<EmptyData>>() {
                @Override
                public void onResponse(Call<ResultModel<EmptyData>> call, Response<ResultModel<EmptyData>> response) {

                }

                @Override
                public void onFailure(Call<ResultModel<EmptyData>> call, Throwable t) {

                }
            });
        } else {
            apiService.sendMessageOnSign(MainActivity.id, currentRoomId, message).enqueue(new Callback<ResultModel<EmptyData>>() {
                @Override
                public void onResponse(Call<ResultModel<EmptyData>> call, Response<ResultModel<EmptyData>> response) {

                }

                @Override
                public void onFailure(Call<ResultModel<EmptyData>> call, Throwable t) {

                }
            });
        }
    }


    private List<String> parseSignText(String signMessage) {
        return Arrays.asList(gson.fromJson(signMessage, String[].class));
    }

    private ChatSignData parseSignData(List<String> signTextList) {
        // ["가","나","다", "가나다"]
        ChatSignData signData = new ChatSignData();
        signData.setSignDataList(SignVideoRepository
                .getInstance(getApplicationContext())
                .getSignDataList(signTextList));

        return signData;
    }

    private String parseDateTime(String time) {
        String result = "";

        String[] dateTime = time.split(" ");
        String[] hhmmss = dateTime[1].split(":");
        int hour = Integer.parseInt(hhmmss[0]);
        if(hour >= 12) {
            if(hour != 12) {
                hour-=12;
            }
            result += "오후 "+hour;
        } else {
            result += "오전 "+hour;
        }
        result += ":"+hhmmss[1];
        return result;
    }

    BroadcastReceiver signReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("sign")) {
                etMessage.append(intent.getStringExtra("msg")+" ");
            }
        }
    };

}
