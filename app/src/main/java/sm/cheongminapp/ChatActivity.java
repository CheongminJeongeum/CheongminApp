package sm.cheongminapp;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.data.ChatObject;
import sm.cheongminapp.data.ChatSignData;
import sm.cheongminapp.data.SignData;
import sm.cheongminapp.database.DBHelper;
import sm.cheongminapp.model.Result;
import sm.cheongminapp.network.ApiService;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.repository.SignVideoRepository;
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

    private String[] navItems = {"Brown", "Cadet Blue", "Dark Olive Green",
            "Dark Orange", "Golden Rod"};
    private ListView lvNavList;
    private FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    DBHelper dbHelper;
    ChatMessageAdapter adapter;

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
                Log.d("ㅆㅂ", "ㅅㅂ");
                Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_SHORT).show();
                adapter.addResponseInput(contents);
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

        //lvNavList = (ListView)findViewById(R.id.lv_activity_chat_nav_list);
        flContainer = (FrameLayout)findViewById(R.id.fl_activity_chat_container);

        //lvNavList.setAdapter(
                //new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        //lvNavList.setOnItemClickListener(new DrawerItemClickListener());
        dlDrawer = (DrawerLayout)findViewById(R.id.dl_activity_chat_drawer);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 메세지 어댑터
        adapter = new ChatMessageAdapter(new ArrayList<ChatObject>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<String> list = new ArrayList<String>();
        list.add("dfsdss");
        list.add("fkfkww");
        list.add("dfsdss");
        list.add("fkfkww");
        list.add("dfsdss");
        list.add("fkfkww");
        list.add("dfsdss");
        list.add("fkfkww");
        list.add("dfsdss");
        list.add("fkfkww");
        lvNavList = (ListView) findViewById(R.id.list_shortcuts);
        ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lvNavList.setAdapter(tempAdapter);

        // 내가 보낸 메세지
        adapter.addChatInput("안녕하세요!");

        // 받은 메세지

        // 받은 메세지를 수어로 변환합니다
        // 괜찮습니다. 고맙습니다. -> 괜찮다/고맙다
        // SignVideoRepository에 Map에 따라 괜찮다의 비디오를 가져옵니다

        ChatSignData chatSignData = new ChatSignData();
        chatSignData.getSignDataList().add(new SignData("괜찮다", SignVideoRepository.getInstance().getSignVideo("괜찮다")));
        chatSignData.getSignDataList().add(new SignData("고맙다", SignVideoRepository.getInstance().getSignVideo("고맙다")));

        adapter.addSign(chatSignData);
        adapter.addResponseInput("괜찮아 고마워");

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("이모티콘", editText.getText().toString());

                if(MainActivity.mode == 1) { // 청
                    IApiService apiService = ApiService.getInstance().getService();
                    apiService.sendMessageOnKorean(MainActivity.id, currentRoomId,
                            editText.getText().toString())
                            .enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {

                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {

                                }
                            });
                } else { // 농
                    IApiService apiService = ApiService.getInstance().getService();
                    apiService.sendMessageOnSign(MainActivity.id, currentRoomId,
                            editText.getText().toString())
                            .enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {

                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {

                                }
                            });
                }

                dbHelper.insert(currentRoomId, 0, editText.getText().toString());
                adapter.addChatInput(editText.getText().toString());
            }
        });

        // 리시버 등록
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
