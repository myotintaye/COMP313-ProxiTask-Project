package sample.example.com.proxitask.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sample.example.com.proxitask.model.APISingleResponse;
import sample.example.com.proxitask.model.APIUserResponse;
import sample.example.com.proxitask.model.User;
import sample.example.com.proxitask.model.UserTask;

public interface UserService {

    /* After log in */
    @GET("user/login")
    Call<APIUserResponse> getLogin(@Header("idToken") String authToken);

    /* Get current user */
    @GET("user")
    Call<APIUserResponse> getUser(@Header("idToken") String authToken);

    /* Get user by id */
    @FormUrlEncoded
    @GET("user/{user_id}")
    Call<APIUserResponse> getUserById(
            @Header("idToken") String authToken,
            @Path("user_id") String userId
    );


    //update user information
    @POST("user/update")
    Call<APIUserResponse> updateUser(@Header("idToken") String authToken, @Body User user);


}
