package sample.example.com.proxitask.model;

import java.util.List;

public class APIMyTasksResponse {

    private List<Task> data;

    public List<Task> getTaskList() {
        return data;
    }

    public void setTaskList(List<Task> data) {
        this.data = data;
    }

}
