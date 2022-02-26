package com.wwp.QA.Machine;

import com.wwp.QA.Utils.PostJSON;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MachineApi {

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
    @POST("//{webaddress}/pms/api/machine/")
    Call<MachineResponse> getMachineList(
            @Path("webaddress") String webadress// 192.168.11.100/pms/api/
            , @Body PostJSON postJSON
    ); // method which must be implemented into ProcesscontrollerRepository
}
