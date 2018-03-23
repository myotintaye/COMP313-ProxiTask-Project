package sample.example.com.proxitask.network;

import sample.example.com.proxitask.model.APIResponse;
import sample.example.com.proxitask.model.UserTask;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskService {
    @GET("task/mycreated")
    Call<APIResponse> getAllTasks();
}