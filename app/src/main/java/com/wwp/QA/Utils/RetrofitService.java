package com.wwp.QA.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    // base URL without GET or POST parameters
    // http://192.168.11.100//pms//api//processcontroller
    // Note:
    //  Endpoint values may be a full URL.
    //  Values which have a host replace the host of baseUrl and values also with a scheme replace the scheme of baseUrl.
    //  Base URL: http://example.com/
    //  Endpoint: https://github.com/square/retrofit/
    //  Result: https://github.com/square/retrofit/
    //
    //  Base URL: http://example.com
    //  Endpoint: //github.com/square/retrofit/
    //  Result: http://github.com/square/retrofit/ (note the scheme stays 'http')
    //
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.1/") // must end with / . This Base URL will be ignored if Endpoint defined into API will start with //
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
