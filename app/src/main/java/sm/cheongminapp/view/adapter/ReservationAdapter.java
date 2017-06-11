package sm.cheongminapp.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import sm.cheongminapp.R;
import sm.cheongminapp.data.Reservation;
import sm.cheongminapp.data.ReservationData;
import sm.cheongminapp.utility.GPSModule;

public class ReservationAdapter extends AbstractAdapter<Reservation> {
    public ReservationAdapter(Context ctx) {
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

        if(reservation.Location != null)
        {
            String[] location = reservation.Location.split("\n");
            if(location.length > 0) {
                viewHolder.tvLocation.setText(location[0]);
                viewHolder.tvLocationDetail.setText(location[1]);
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat ("EEE, dd MMM yyyy hh:mm:ss", Locale.ENGLISH );
        Date date = dateFormat.parse (reservation.Date,  new ParsePosition(0));
        long utcTime = date.getTime();

        TimeZone timeZone = TimeZone.getDefault();
        int timeOffset = timeZone.getOffset(date.getTime());

        long localTime = date.getTime() + timeOffset;

        Date localDate = new Date(localTime);
        SimpleDateFormat localFormat = new SimpleDateFormat("MM-dd");

        String[] localDateText = localFormat.format(localDate).split("-");
        viewHolder.tvTimeMonth.setText(localDateText[0] + "월");
        viewHolder.tvTimeDay.setText(localDateText[1] + "일");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(localDate);

        String dayText = "";
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day == 1) dayText = "일요일";
        else if(day == 2) dayText = "월요일";
        else if(day == 3) dayText = "화요일";
        else if(day == 4) dayText = "수요일";
        else if(day == 5) dayText = "목요일";
        else if(day == 6) dayText = "금요일";
        else if(day == 7) dayText = "토요일";

        viewHolder.tvTimeDayText.setText(dayText);
        viewHolder.tvTimeText.setText(reservation.getTimeRangeText());

        int result = reservation.Result;
        if(result == 0) {
            viewHolder.ivResultIcon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_event));
            viewHolder.tvResult.setText("대기중");
            viewHolder.tvResult.setTextColor(view.getResources().getColor(R.color.colorBlueGray3));
        } else {
            viewHolder.ivResultIcon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_event_available));
            viewHolder.tvResult.setText("예약됨");
            viewHolder.tvResult.setTextColor(view.getResources().getColor(R.color.colorPrimary));
        }

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

        @BindView(R.id.layout_request_result_icon)
        ImageView ivResultIcon;

        @BindView(R.id.layout_request_result)
        TextView tvResult;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}