package com.wwp.QA.Login;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class QARetrofitService {


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
    // BASE_URL Will be replaced entirely in API Interface if Endpoint will start with //
    public static final String BASE_URL = "http://192.168.1.1/"; // must end with /
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if (retrofit == null)
            Log.e("QARS", "retrofit.build() -> null");
        return retrofit;
    }

}
