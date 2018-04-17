package sample.example.com.proxitask.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import sample.example.com.proxitask.model.APIResponse;
import sample.example.com.proxitask.model.APISingleResponse;
import sample.example.com.proxitask.model.UserTask;

public interface TaskService {
    @GET("task/mycreated")
    Call<APIResponse> getAllTasks(@Header("idToken") String authToken);

    @POST("task/add")
    Call<APISingleResponse> addTask(@Header("idToken") String authToken, @Body UserTask userTask);


    @FormUrlEncoded
    @POST("task/apply")
    Call<APISingleResponse> applyTask(
            @Header("idToken") String authToken,
            @Field("_id") String taskId
    );

}