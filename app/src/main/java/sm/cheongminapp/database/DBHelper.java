package sm.cheongminapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import sm.cheongminapp.data.ChatInput;
import sm.cheongminapp.data.ChatObject;
import sm.cheongminapp.data.ChatResponse;

/**
 * Created by user on 2017. 5. 22..
 */
public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE CHATLOG (room_id INTEGER PRIMARY KEY, who INTEGER, contents TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 0 : 나
    // 1 : 상대방
    public void insert(int room_id, int who, String contents) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO CHATLOG VALUES(" + room_id + ", " + who + ", '" + contents + "');");
        db.close();
    }

    public void update() {

    }

    public void delete() {

    }

    public List<ChatObject> getResults() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        List<ChatObject> chatLogList = new ArrayList<ChatObject>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM CHATLOG", null);
        while (cursor.moveToNext()) {
            ChatObject obj = null;
            if(cursor.getInt(1) == 0) {
                obj = new ChatInput();
            } else {
                obj = new ChatResponse();
            }
            obj.setText(cursor.getString(2));
            chatLogList.add(obj);
        }

        return chatLogList;
    }
}
