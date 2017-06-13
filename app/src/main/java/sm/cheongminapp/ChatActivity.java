package sm.cheongminapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Toast;

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
import sm.cheongminapp.data.SignData;
import sm.cheongminapp.database.DBHelper;
import sm.cheongminapp.model.ResultModel;
import sm.cheongminapp.model.data.EmptyData;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.repository.SignVideoRepository;
import sm.cheongminapp.view.adapter.ChatMessageAdapter;
import sm.cheongminapp.view.adapter.HotKeyAdapter;

/*
    TODO: 채팅 과정 구현
    친구 프로필 페이지에서 대화방 생성 -> /chat/room (POST)로 요청함.
    리턴값은 Room객체(추가해야 함). 응답이 오는 즉시 ChatActivity로 이동함(인텐트로 방 번호, 방 이름을 전달)

    대화 목록창을 누르면 생성된 방들을 모두 가져옴. -> /chat/room (GET)으로 요청.
    리스트 중 하나를 누르면 ChatActivity로 이동함(인텐트로 방 번호, 방 이름을 전달)

    방 생성 시 중복체크 할 때가 조금 귀찮은데
    Primary key 값을 자동 증가값으로 하지 말고, 차라리 두 사용자의 아이디값으로 지정하는것이 어떤가?
 */

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_toolbar)
    Toolbar toolbar;

    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.chat_message)
    EditText editText;

    @BindView(R.id.chat_send)
    Button button;

    private String[] navItems = {"Brown", "Cadet Blue", "Dark Olive Green",
            "Dark Orange", "Golden Rod"};

    @BindView(R.id.list_shortcuts)
    ListView lvHotkeyList;

    @BindView(R.id.fl_activity_chat_container)
    FrameLayout flContainer;

    @BindView(R.id.dl_activity_chat_drawer)
    DrawerLayout dlDrawer;

    ActionBarDrawerToggle dtToggle;

    HotKeyAdapter hotKeyAdapter;

    DBHelper dbHelper;
    ChatMessageAdapter adapter;

    ChatRoom chatRoom;

    // 현재 방 번호 (나중에 인텐트 등으로 값 가져와야 함)
    public int currentRoomId = 1;

    // 채팅 액티비티를 활성화하고 있을 때만 호출됨.
    BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("chat")) {
                int room_id = intent.getIntExtra("room_id", -1); // 방 번호
                if(currentRoomId != room_id) return; // (현재 방과 번호가 다른 경우 무시)

                String contents = intent.getStringExtra("contents"); // 상대방 대화 내용

                adapter.addResponseInput(contents);
                adapter.notifyDataSetChanged();

                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
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
        chatRoom = (ChatRoom)getIntent().getSerializableExtra("ChatRoom");
        currentRoomId = chatRoom.room_id;

        getSupportActionBar().setTitle(chatRoom.room_name);

        // 네비게이션 메뉴 설정
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer,
                R.drawable.ic_drawer, R.string.open_drawer, R.string.close_drawer){

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
        adapter = new ChatMessageAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // 이전 채팅 내용 불러오기
        loadChatLog();

        // 단축키 불러오기
        setupHotkeyList();

        // 리시버 등록
        setupReceiver();
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


    public void setupHotkeyList() {
        hotKeyAdapter = new HotKeyAdapter(this);
        lvHotkeyList.setAdapter(hotKeyAdapter);

        HotKey hotKey1 = new HotKey();
        hotKey1.Name = "핫키1";
        hotKey1.Content = "내용1";

        hotKeyAdapter.addItem(hotKey1);
    }

    public void loadChatLog() {
        // 로컬 DB에서 채팅 기록 가져옴
        dbHelper = new DBHelper(getApplicationContext(), "Chat.db", null, 1);

        List<ChatObject> chatList = dbHelper.getResultsByRoomId(currentRoomId);
        for(int i=0; i<chatList.size(); i++) {
            ChatObject chat = chatList.get(i);
            switch(chat.getType())
            {
                case ChatObject.INPUT_OBJECT:
                    // 입력한 채팅
                    adapter.addChatInput(chat.getText());
                    break;
                case ChatObject.RESPONSE_OBJECT:
                    // 받은 채팅
                    adapter.addResponseInput(chat.getText());
                    break;
                case ChatObject.SIGN_IMAGE_OBJECT:
                    // 수화 영상
                    break;
            }
        }
    }

    public void setupReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("chat");

        registerReceiver(chatReceiver, filter);
    }

    @OnClick(R.id.chat_send)
    public void clickSend() {
        String sendText = editText.getText().toString();

        IApiService apiService = ApiService.getInstance().getService();
        if(MainActivity.mode == 1) {
            apiService.sendMessageOnKorean(MainActivity.id, currentRoomId, sendText).enqueue(new Callback<ResultModel<EmptyData>>() {
                @Override
                public void onResponse(Call<ResultModel<EmptyData>> call, Response<ResultModel<EmptyData>> response) {

                }

                @Override
                public void onFailure(Call<ResultModel<EmptyData>> call, Throwable t) {

                }
            });
        } else { // 농
            apiService.sendMessageOnSign(MainActivity.id, currentRoomId, sendText).enqueue(new Callback<ResultModel<EmptyData>>() {
                        @Override
                        public void onResponse(Call<ResultModel<EmptyData>> call, Response<ResultModel<EmptyData>> response) {

                        }

                        @Override
                        public void onFailure(Call<ResultModel<EmptyData>> call, Throwable t) {

                        }
                    });
        }

        // DB에 저장
        dbHelper.insert(currentRoomId, 0, sendText);

        adapter.addChatInput(sendText);

        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        editText.setText("");
    }

    @OnItemClick(R.id.list_shortcuts)
    public void onItemClick(AdapterView<?> parent, int position) {
        HotKey hotKey = (HotKey)hotKeyAdapter.getItem(position);

        editText.setText(hotKey.Content);
        button.callOnClick();
    }

    @OnClick(R.id.chat_add_hotkey)
    public void clickAddHotkey() {
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

                hotKeyAdapter.addItem(hotkey);
                hotKeyAdapter.notifyDataSetChanged();
            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position,
                                long id) {
            switch (position) {
                case 0:
                    flContainer.setBackgroundColor(Color.parseColor("#A52A2A"));
                    break;
                case 1:
                    flContainer.setBackgroundColor(Color.parseColor("#5F9EA0"));
                    break;
                case 2:
                    flContainer.setBackgroundColor(Color.parseColor("#556B2F"));
                    break;
                case 3:
                    flContainer.setBackgroundColor(Color.parseColor("#FF8C00"));
                    break;
                case 4:
                    flContainer.setBackgroundColor(Color.parseColor("#DAA520"));
                    break;

            }

        }
    }
}
