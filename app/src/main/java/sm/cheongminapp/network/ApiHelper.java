package sm.cheongminapp.network;

/**
 * Created by Raye on 2017-06-13.
 */

public class ApiHelper {
    public static String GetProfileImageUrl(String userID) {
        return ApiService.getInstance().getUrl() + "profiles/images/" + userID;
    }
}
