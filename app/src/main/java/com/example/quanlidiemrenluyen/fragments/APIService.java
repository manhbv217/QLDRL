package com.example.quanlidiemrenluyen.fragments;

import com.example.quanlidiemrenluyen.Notification.MyResponse;
import com.example.quanlidiemrenluyen.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=AAAAmRtmURU:APA91bHrd_XdzecHd1UroEZSGIEPWByURtOoA-Y6OMg_NxtjEH2PY-vwLIIUZSpekiO07Uxhox4HrgntqhCjWj3CzNJUcFm3q5-AF8RLDAGTN8w3GmCczlWlC_br-Uyev1xXaN4soFs6"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification (@Body Sender body);
}
