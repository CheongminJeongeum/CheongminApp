package sm.cheongminapp.service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

import sm.cheongminapp.database.DBHelper;

/**
 * Created by user on 2017. 5. 18..
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        DBHelper dbHelper = new DBHelper(getApplicationContext(), "Chat.db", null, 1);
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom()); // 서버 senderID이므로 아무런 쓸모 없음

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody()); // 메시지
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle()); // 방 번호

            int room_id = Integer.parseInt(remoteMessage.getNotification().getTitle());
            String contents = remoteMessage.getNotification().getBody();

            String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
            dbHelper.insert(room_id, 1, contents, time);

            Intent dataIntent = new Intent("chat");
            dataIntent.putExtra("room_id", room_id);
            dataIntent.putExtra("contents", contents);
            dataIntent.putExtra("time", time);
            sendBroadcast(dataIntent);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
