package sample.example.com.proxitask.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.model.APISingleResponse;
import sample.example.com.proxitask.model.APIUserResponse;
import sample.example.com.proxitask.model.User;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TaskService;
import sample.example.com.proxitask.network.TokenStore;
import sample.example.com.proxitask.network.UserService;

public class TaskCandidatesAdapter extends RecyclerView.Adapter<TaskCandidatesAdapter.MyViewHolder> {

    private Context context;
    private String[] candidates;
    private String taskId;

    private UserService userService;
    private TaskService taskService;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView username, completed;
        public Button btnHire;

        public MyViewHolder(View view){
            super(view);

            username = (TextView) view.findViewById(R.id.tv_username);
            completed = (TextView) view.findViewById(R.id.tv_num_of_task_completed);

            btnHire = (Button) view.findViewById(R.id.btn_hire);

        }
    }


    public TaskCandidatesAdapter(Context context, String[] candidates, String taskId){
        this.context = context;
        this.candidates = candidates;
        this.taskId = taskId;

        userService = RetrofitInstance.getRetrofitInstance().create(UserService.class);
        taskService = RetrofitInstance.getRetrofitInstance().create(TaskService.class);
    }

    @Override
    public TaskCandidatesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_candidates, parent, false);

        return new TaskCandidatesAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final TaskCandidatesAdapter.MyViewHolder holder, int position){

        String userId = candidates[position];

        userService.getUserById(TokenStore.getToken(context), userId).enqueue(new Callback<APIUserResponse>() {
            @Override
            public void onResponse(Call<APIUserResponse> call, Response<APIUserResponse> response) {
                User user = response.body().getUser();

                /* Load the UI */
                if (user != null){

                    String test = user.getUserName();

                    if (user.getUserName() != null){
                        holder.username.setText(test);
                    }
                    else{
                        holder.username.setText("Test User");
                    }

                    if (user.getTaskCompleted() != null){
                        holder.completed.setText(String.valueOf(user.getTaskCompleted().length + " tasks completed"));
                    }
                    else{
                        holder.completed.setText("0 tasks completed");
                    }

                    holder.btnHire.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){

                            /* Hire the candidate */
//                            hireTask(taskId);

                            /* Disable the button */

                            holder.btnHire.setText("Hired");
                            holder.btnHire.setBackgroundColor(Color.parseColor("#D2D2D2"));
                            holder.btnHire.setEnabled(false);

                        }
                    });

                }
                else{
                    /* cannot get the user, to be revised later */

                    holder.username.setText("Test User");
                    holder.completed.setText("0 tasks completed");

                    holder.btnHire.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){

                            /* Hire the candidate */
                            hireTask(taskId);

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<APIUserResponse> call, Throwable t) {
                Toast.makeText(context,"Having troubles in pulling user data. Please try again later.",Toast.LENGTH_LONG).show();
            }

            public void hireTask(String taskId){
                taskService.applyTask(TokenStore.getToken(context), taskId).enqueue(new Callback<APISingleResponse>() {
                    @Override
                    public void onResponse(Call<APISingleResponse> call, Response<APISingleResponse> response) {
                        Toast.makeText(context,"Task applied, please wait for task owner to review.",Toast.LENGTH_LONG).show();

                        /* Disable the button */

                        holder.btnHire.setText("Hired");
                        holder.btnHire.setBackgroundColor(Color.parseColor("#D2D2D2"));
                        holder.btnHire.setEnabled(false);

                    }

                    @Override
                    public void onFailure(Call<APISingleResponse> call, Throwable t) {
                        Toast.makeText(context,"There is something wrong. Please try again later.",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }



    @Override
    public int getItemCount() {
        return candidates.length;
    }


}
