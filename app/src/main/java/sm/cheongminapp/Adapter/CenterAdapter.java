package sm.cheongminapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sm.cheongminapp.data.Center;
import sm.cheongminapp.R;

/**
 * Created by user on 2017. 5. 14..
 */
public class CenterAdapter extends AbstractAdapter<Center> {
    public CenterAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_center, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvListItemCenterName = (TextView) view.findViewById(R.id.center_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final Center info = adapterList.get(i);
        if (info == null)
            return view;

        viewHolder.tvListItemCenterName.setText(info.name);
        return view;
    }

    private class ViewHolder {
        TextView tvListItemCenterName;
    }
}
