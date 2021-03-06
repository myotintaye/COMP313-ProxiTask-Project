package sample.example.com.proxitask.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import sample.example.com.proxitask.R;
import sample.example.com.proxitask.model.Task;

public class TaskToDoAdapter extends RecyclerView.Adapter<TaskToDoAdapter.MyViewHolder> {

    private Context context;
    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView date, title, owner, address, points;
        public Button btnNavigate, btnCall, btnDone;

        public MyViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.tv_task_date);
            title = (TextView) view.findViewById(R.id.tv_task_title);
            owner = (TextView) view.findViewById(R.id.tv_task_owner);
            address = (TextView) view.findViewById(R.id.tv_task_address);
            points = (TextView) view.findViewById(R.id.tv_task_points);

            btnNavigate = (Button) view.findViewById(R.id.btn_navigate);
            btnCall = (Button) view.findViewById(R.id.btn_call);
            btnDone = (Button) view.findViewById(R.id.btn_done);
        }
    }

    public TaskToDoAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_tasks_to_do, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        /* Binding task information */
        Task task = taskList.get(position);
        if (task != null) {

            if (task.getDate() != null) {
                holder.date.setText(task.getDate());
            } else {
                holder.date.setText("Apr 25, 2018 (F)");
            }

            if (task.getTitle() != null) {
                holder.title.setText(task.getTitle());
            }

            if (task.getAddress() != null) {
                holder.address.setText(task.getAddress());
            }

            holder.points.setText("Coins: " + String.valueOf(task.getPrice()));

            /* Only to show Done button if the task is today, to be improved later */
            holder.btnDone.setVisibility(View.INVISIBLE);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date dateAfterFormatting = new Date();

            String dateStr = task.getDate();
            try {
                dateAfterFormatting = formatter.parse(dateStr);
                System.out.println(dateAfterFormatting);
                System.out.println("time zone : " + TimeZone.getDefault().getID());
                System.out.println(formatter.format(dateAfterFormatting));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date today = new Date(System.currentTimeMillis());

            if (dateAfterFormatting != null && dateAfterFormatting.compareTo(today) < 0 ){
                holder.btnDone.setVisibility(View.VISIBLE);
            }


            holder.btnNavigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /* call Google Map */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String origin = "Scarboroug+Town+Centre";
                            String target = task.getAddress().replace(" ", "+");
                            String uri = "https://www.google.com/maps/dir/?api=1&origin=" + origin + "&destination=" + target + "&travelmode=driving";

                            Uri gmmIntentUri = Uri.parse(uri);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            context.startActivity(mapIntent);
                        }
                    }, 1000);
                }
            });

            holder.btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /* call phone call */

                    String number = "6471234567";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    context.startActivity(intent);
                }
            });

            holder.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /* call complete API */


                    /* reset style */
                    Toast.makeText(context,"Task completed, thank you!",Toast.LENGTH_LONG).show();

                    /* Disable the color */
                    holder.btnDone.setBackgroundColor(Color.parseColor("#D2D2D2"));
                    holder.btnDone.setEnabled(false);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
