package com.wwp.QA.Login;

import com.wwp.QA.Utils.PostJSON;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QALoginAPI {

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
    // BASE_URL is replaced entirely if Endpoint is starting with //
    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("//{webaddress}/pms/api/processcontroller/")
    Call<List<IsLoginResponse>> getQAOperator(
            @Path("webaddress") String webadress// 192.168.11.100/pms/api/
            , @Body PostJSON postjson
    ); // method which must be implemented into Repository


}
