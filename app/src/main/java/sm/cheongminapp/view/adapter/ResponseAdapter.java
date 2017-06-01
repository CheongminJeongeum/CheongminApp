package sm.cheongminapp.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.R;
import sm.cheongminapp.data.Reservation;
import sm.cheongminapp.data.ReservationData;
import sm.cheongminapp.utility.GPSModule;

public class ResponseAdapter extends AbstractAdapter<Reservation> {
    public ResponseAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_request, viewGroup, false);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Reservation reservation = adapterList.get(i);
        if (reservation == null)
            return view;

        //GPSModule gpsModule = new GPSModule(view.getContext());
        //String location = gpsModule.findAddress(reservation.Lat, reservation.Lng);

        viewHolder.tvLocation.setText("주소1");
        viewHolder.tvLocationDetail.setText("상세 주소1");

        viewHolder.tvTimeMonth.setText("1월");
        viewHolder.tvTimeDay.setText("1일");
        viewHolder.tvTimeDayText.setText("1요일");

        viewHolder.tvTimeText.setText(reservation.getTimeRangeText());

        viewHolder.tvResult.setText("결과1");

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