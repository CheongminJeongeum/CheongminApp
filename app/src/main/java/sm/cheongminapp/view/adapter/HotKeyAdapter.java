package sm.cheongminapp.view.adapter;

import android.content.Context;
import android.support.annotation.BinderThread;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.R;
import sm.cheongminapp.data.HotKey;

/**
 * Created by Raye on 2017-06-06.
 */

public class HotKeyAdapter extends AbstractAdapter<HotKey> {

    public HotKeyAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflator.inflate(R.layout.layout_hotkey_item, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        HotKey hotKey = adapterList.get(position);

        viewHolder.tvIndex.setText(String.valueOf(position + 1));
        viewHolder.tvName.setText(hotKey.Name);

        Picasso.with(convertView.getContext())
                .load(hotKey.Icon)
                .into(viewHolder.icIcon);

        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.layout_hotkey_index)
        TextView tvIndex;

        @BindView(R.id.layout_hotkey_icon)
        ImageView icIcon;

        @BindView(R.id.layout_hotkey_item_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
