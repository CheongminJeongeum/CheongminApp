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

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.R;
import sm.cheongminapp.data.ChatRoom;

/**
 * Created by Raye on 2017-05-18.
 */

public class ChatRoomAdapter extends AbstractAdapter<ChatRoom> {

    public ChatRoomAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.layout_chat_room_item, viewGroup, false);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final ChatRoom chatRoom = adapterList.get(i);
        if (chatRoom == null)
            return view;

        viewHolder.tvName.setText(chatRoom.Name);

        return view;
    }

    class ViewHolder {

        @BindView(R.id.layout_chat_room_icon)
        ImageView ivIcon;

        @BindView(R.id.layout_chat_room_name)
        TextView tvName;

        @BindView(R.id.layout_chat_room_message)
        TextView tvMessage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
