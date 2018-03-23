package sample.example.com.proxitask.model;

import java.util.List;

/**
 * Created by Myo on 3/23/2018.
 */

public class APIResponse {
    private List<UserTask> data;

    public List<UserTask> getData() {
        return data;
    }

    public void setData(List<UserTask> data) {
        this.data = data;
    }
}
