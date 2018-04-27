package sample.example.com.proxitask.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import sample.example.com.proxitask.model.APIUserResponse;

public interface UserService {

    /* After log in */
    @GET("user/login")
    Call<APIUserResponse> getLogin(@Header("idToken") String authToken);

    /* Get current user */
    @GET("user")
    Call<APIUserResponse> getUser(@Header("idToken") String authToken);

    /* Get user by id */
    @GET("user/{user_id}")
    Call<APIUserResponse> getUserById(
            @Header("idToken") String authToken,
            @Path("user_id") String userId
    );



}
