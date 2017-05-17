package sm.cheongminapp.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatRoom;

/**
 * Created by Raye on 2017-05-18.
 */

public class ChatRoomAdapter extends BaseAdapter {
    private ArrayList<ChatRoom> itemArrayList = new ArrayList<ChatRoom>() ;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_chat_room_item, parent, false);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.layout_chat_room_icon);
        TextView tvName = (TextView) convertView.findViewById(R.id.layout_chat_room_name);
        TextView tvLastChat = (TextView) convertView.findViewById(R.id.layout_chat_room_message);

        ChatRoom item = itemArrayList.get(position);

        imgIcon.setImageDrawable(item.getIconDrawable());
        tvName.setText(item.getName());
        tvLastChat.setText(item.getLastChat());

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return itemArrayList.get(position) ;
    }
    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public int getCount() {
        return itemArrayList.size() ;
    }

    public void addItem(Drawable icon, String name, String lastChat) {
        ChatRoom item = new ChatRoom();

        item.setIconDrawable(icon);
        item.setName(name);
        item.setLastChat(lastChat);

        itemArrayList.add(item);
    }
}
