package sample.example.com.proxitask.adapter;

import android.content.Context;
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
import sample.example.com.proxitask.model.APIUserResponse;
import sample.example.com.proxitask.model.User;
import sample.example.com.proxitask.network.TokenStore;
import sample.example.com.proxitask.network.UserService;

public class TaskCandidatesAdapter extends RecyclerView.Adapter<TaskCandidatesAdapter.MyViewHolder> {

    private Context context;
    private String[] candidates;

    private UserService userService;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView username, completed;
        public Button btnAssign;

        public MyViewHolder(View view){
            super(view);

            username = (TextView) view.findViewById(R.id.tv_username);
            completed = (TextView) view.findViewById(R.id.tv_num_of_task_completed);

            btnAssign = (Button) view.findViewById(R.id.btn_assign);

        }
    }


    public TaskCandidatesAdapter(Context context, String[] candidates){
        this.context = context;
        this.candidates = candidates;
    }

    @Override
    public TaskCandidatesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_tasks_posted, parent, false);

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

                    holder.username.setText(user.getUserName());

                    holder.completed.setText(String.valueOf(user.getTaskCompleted().length));

                    holder.btnAssign.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){

                            /* Hire the candidate */

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<APIUserResponse> call, Throwable t) {
                Toast.makeText(context,"Having troubles in pulling user data. Please try again later.",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return candidates.length;
    }


}
