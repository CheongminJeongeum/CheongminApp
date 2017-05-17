package sm.cheongminapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.view.adapter.ChatMessageAdapter;
import sm.cheongminapp.data.ChatObject;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_toolbar)
    Toolbar toolbar;

    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.chat_message)
    EditText editText;

    @BindView(R.id.chat_send)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("김농인님과 대화");

        ChatMessageAdapter adapter = new ChatMessageAdapter(new ArrayList<ChatObject>());
        adapter.addResponseInput("안녕하세요");

        adapter.addChatInput("안녕하세요!");

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
