package sample.example.com.proxitask.network;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import sample.example.com.proxitask.model.APINotificationResponse;
import sample.example.com.proxitask.model.MsgToken;

public interface NotificationService {
    @POST("user/msgToken")
    Call<APINotificationResponse> getNotification(@Header("idToken") String authToken, @Body MsgToken msgToken);

}
