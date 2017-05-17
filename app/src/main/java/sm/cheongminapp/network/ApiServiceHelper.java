package sm.cheongminapp.network;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Raye on 2017-05-02.
 */

public class ApiServiceHelper {
    private static ApiServiceHelper instance =  new ApiServiceHelper();

    public IApiService ApiService;
    private Retrofit retrofit;

    public ApiServiceHelper()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://183.97.108.182:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService = retrofit.create(IApiService.class);
    }

    public static ApiServiceHelper getInstance() {
        return instance;
    }


}
