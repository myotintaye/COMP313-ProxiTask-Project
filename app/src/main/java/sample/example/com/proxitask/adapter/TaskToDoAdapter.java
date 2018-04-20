package sample.example.com.proxitask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


import sample.example.com.proxitask.R;
import sample.example.com.proxitask.model.Task;

public class TaskToDoAdapter extends RecyclerView.Adapter<TaskToDoAdapter.MyViewHolder> {

    private Context context;
    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView date, title, owner, address, points;
        public Button btnNavigate, btnCall;

        public MyViewHolder(View view){
            super(view);

            date = (TextView) view.findViewById(R.id.tv_task_date);
            title = (TextView) view.findViewById(R.id.tv_task_title);
            owner = (TextView) view.findViewById(R.id.tv_task_owner);
            address = (TextView) view.findViewById(R.id.tv_task_address);
            points = (TextView) view.findViewById(R.id.tv_task_points);

            btnNavigate = (Button) view.findViewById(R.id.btn_navigate);
            btnCall = (Button) view.findViewById(R.id.btn_call);

        }
    }

    public TaskToDoAdapter(Context context, List<Task> taskList){
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_tasks_to_do, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position){

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

            holder.points.setText("Coins: " + String.valueOf(task.getPrice()));

            holder.btnNavigate.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    /* call Google Map */
                }
            });

            holder.btnCall.setOnClickListener(new View.OnClickListener(){
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
