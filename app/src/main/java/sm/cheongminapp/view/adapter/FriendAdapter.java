package sm.cheongminapp.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import de.hdodenhof.circleimageview.CircleImageView;
import sm.cheongminapp.data.Friend;
import sm.cheongminapp.R;
import sm.cheongminapp.network.ApiHelper;
import sm.cheongminapp.network.ApiService;

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

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Friend item = adapterList.get(position);

        Picasso.with(convertView.getContext())
                .load(ApiHelper.GetProfileImageUrl(item.ID))
                .placeholder(R.drawable.profile)
                .into(viewHolder.ivProfile);

        viewHolder.tvName.setText(item.Name);

        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.layout_friend_profile)
        CircleImageView ivProfile;

        @BindView(R.id.layout_friend_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}