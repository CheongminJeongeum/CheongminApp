package sm.cheongminapp.utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 2017. 5. 29..
 */
public class GPSModule {
    Context ctx = null;

    public GPSModule(Context ctx) {
        this.ctx = ctx;
    }

    public String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this.ctx, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAddressLine(0);

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress).append("#");
                    bf.append(lat).append("#");
                    bf.append(lng);

                    return currentLocationAddress;
                }
            }

        } catch (IOException e) {
            Toast.makeText(ctx, "주소취득 실패"
                    , Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return bf.toString();
    }
}
