package sample.example.com.proxitask.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import sample.example.com.proxitask.R;
import sample.example.com.proxitask.activity.myTasks.SelectCandidatesFragment;
import sample.example.com.proxitask.model.Task;

public class TaskPostedAdapter extends RecyclerView.Adapter<TaskPostedAdapter.MyViewHolder> {

    private Context context;
    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView date, title, candidates, address, points;
        public Button btnAssign, btnEdit;

        public MyViewHolder(View view){
            super(view);

            date = (TextView) view.findViewById(R.id.tv_task_date);
            title = (TextView) view.findViewById(R.id.tv_task_title);
            candidates = (TextView) view.findViewById(R.id.tv_task_candidates);
            address = (TextView) view.findViewById(R.id.tv_task_address);
            points = (TextView) view.findViewById(R.id.tv_task_points);

            btnAssign = (Button) view.findViewById(R.id.btn_assign);
            btnEdit = (Button) view.findViewById(R.id.btn_edit);

        }
    }


    public TaskPostedAdapter(Context context, List<Task> taskList){
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public TaskPostedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_tasks_posted, parent, false);

        return new TaskPostedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskPostedAdapter.MyViewHolder holder, int position){

        /* Binding task information */
        Task task = taskList.get(position);
        if (task != null){

            if (task.getDate() != null){
                holder.date.setText(task.getDate());
            }
            else{
                holder.date.setText("Apr 25, 2018 (F)");
            }

            if (task.getTitle() != null){
                holder.title.setText(task.getTitle());
            }

            if (task.getAddress() != null){
                holder.address.setText(task.getAddress());
            }


            if (task.getCandidates() != null){
                String c;
                if (task.getCandidates().length == 1){
                   c = " candidate";
                }
                else {
                    c = " candidates";
                }
                holder.candidates.setText(String.valueOf(task.getCandidates().length) + c);
            }
            else{
                holder.candidates.setText("0 Candidates");
            }

            holder.points.setText("Coins: " + String.valueOf(task.getPrice()));

            holder.btnAssign.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    Bundle bundle=new Bundle();
                    bundle.putString("title",task.getTitle());
                    bundle.putString("desc",task.getDescription());
                    bundle.putDouble("price",task.getPrice());
                    bundle.putString("date",task.getDate());
                    bundle.putString("address",task.getAddress());
                    bundle.putInt("radius",task.getRadius());
                    bundle.putString("taskId", task.getTaskId());
                    bundle.putStringArray("candidates", task.getCandidates());

                    SelectCandidatesFragment candidatesFragment = new SelectCandidatesFragment();
                    candidatesFragment.setArguments(bundle);

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, candidatesFragment).addToBackStack(null).commit();
                }
            });




            holder.btnEdit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    /* call phone call */
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
