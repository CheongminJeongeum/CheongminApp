package sm.cheongminapp.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sm.cheongminapp.R;
import sm.cheongminapp.data.ReservationList;

public class ResponseAdapter extends AbstractAdapter<ReservationList> {
    public ResponseAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_request, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvListItemLocation = (TextView) view.findViewById(R.id.location);
            viewHolder.tvListItemDate = (TextView) view.findViewById(R.id.date);
            viewHolder.tvListItemTime = (TextView) view.findViewById(R.id.time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final ReservationList info = adapterList.get(i);
        if (info == null)
            return view;

        viewHolder.tvListItemLocation.setText(info.location);
        viewHolder.tvListItemDate.setText(info.date);
        viewHolder.tvListItemTime.setText(info.time);
        return view;
    }

    private class ViewHolder {
        TextView tvListItemLocation;
        TextView tvListItemDate;
        TextView tvListItemTime;

    }
}