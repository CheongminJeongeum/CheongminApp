package sm.cheongminapp.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
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

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final ReservationList info = adapterList.get(i);
        if (info == null)
            return view;

        viewHolder.tvLocation.setText(info.Location);
        viewHolder.tvLocationDetail.setText(info.LocationDetail);

        viewHolder.tvTimeText.setText(info.Time);

        return view;
    }

    class ViewHolder {
        @BindView(R.id.layout_request_time_month)
        TextView tvTimeMonth;

        @BindView(R.id.layout_request_time_day)
        TextView tvTimeDay;

        @BindView(R.id.layout_request_time_day_text)
        TextView tvTimeDayText;

        @BindView(R.id.layout_request_location)
        TextView tvLocation;

        @BindView(R.id.layout_request_location_detail)
        TextView tvLocationDetail;

        @BindView(R.id.layout_request_time_text)
        TextView tvTimeText;

        @BindView(R.id.layout_request_result)
        TextView tvResult;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}