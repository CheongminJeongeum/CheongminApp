package sm.cheongminapp.network;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Raye on 2017-05-02.
 */

public class ApiService {
    private static String BASE_API_URL = "http://59.9.50.129:5000/api/";

    private IApiService service;

    public ApiService()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IApiService.class);
    }

    public IApiService getService() {
        return service;
    }

    private static ApiService instance =  new ApiService();
    public static ApiService getInstance() {
        return instance;
    }
}