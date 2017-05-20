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

import sm.cheongminapp.data.Friend;
import sm.cheongminapp.R;

/**
 * Created by Raye on 2017-05-16.
 */

public class FriendAdapter extends BaseAdapter {
    private ArrayList<Friend> itemArrayList = new ArrayList<Friend>() ;

    public FriendAdapter() {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_friend_item, parent, false);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.profile) ;
        TextView nameTextView = (TextView) convertView.findViewById(R.id.name) ;

        Friend item = itemArrayList.get(position);

        iconImageView.setImageDrawable(item.getIconDrawable());
        nameTextView.setText(item.getName());

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

    public void addItem(Drawable icon, String name) {
        Friend item = new Friend();

        item.setIconDrawable(icon);
        item.setName(name);

        itemArrayList.add(item);
    }
}