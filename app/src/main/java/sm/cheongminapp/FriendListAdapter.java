package sm.cheongminapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Raye on 2017-05-16.
 */

public class FriendListAdapter extends BaseAdapter {
    private ArrayList<FriendListItem> itemArrayList = new ArrayList<FriendListItem>() ;

    public FriendListAdapter() {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_list_layout, parent, false);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView) ;
        TextView nameTextView = (TextView) convertView.findViewById(R.id.textName) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.textDescription) ;

        FriendListItem item = itemArrayList.get(position);

        iconImageView.setImageDrawable(item.getIconDrawable());
        nameTextView.setText(item.getName());
        descTextView.setText(item.getDescription());

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

    public void addItem(Drawable icon, String name, String desc) {
        FriendListItem item = new FriendListItem();

        item.setIconDrawable(icon);
        item.setName(name);
        item.setDescription(desc);

        itemArrayList.add(item);
    }
}
