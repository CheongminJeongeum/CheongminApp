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
import sm.cheongminapp.data.Friend;
import sm.cheongminapp.R;

/**
 * Created by Raye on 2017-05-16.
 */

public class FriendAdapter extends AbstractAdapter<Friend> {

    public FriendAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflator.inflate(R.layout.layout_friend_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageProfile = (ImageView) convertView.findViewById(R.id.layout_friend_profile);
            viewHolder.textName = (TextView)convertView.findViewById(R.id.layout_friend_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Friend item = adapterList.get(position);

        viewHolder.imageProfile.setImageDrawable(item.getIconDrawable());
        viewHolder.textName.setText(item.getName());

        return convertView;
    }


    class ViewHolder {
        ImageView imageProfile;
        TextView textName;
    }
}