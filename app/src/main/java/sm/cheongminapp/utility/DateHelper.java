package sm.cheongminapp.utility;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;

import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Raye on 2017-06-12.
 */

public class DateHelper {
    public static Date getUTCStringToLocalDate(String string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
        Date date = dateFormat.parse(string, new ParsePosition(0));

        TimeZone timeZone = TimeZone.getDefault();

        long utcTime = date.getTime();
        int timeOffset = timeZone.getOffset(date.getTime());

        long localTime = date.getTime() + timeOffset;

        return new Date(localTime);
    }

    public static String getDayStringForDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String dayText = "";
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) dayText = "일요일";
        else if (day == 2) dayText = "월요일";
        else if (day == 3) dayText = "화요일";
        else if (day == 4) dayText = "수요일";
        else if (day == 5) dayText = "목요일";
        else if (day == 6) dayText = "금요일";
        else if (day == 7) dayText = "토요일";

        return dayText;
    }
}
