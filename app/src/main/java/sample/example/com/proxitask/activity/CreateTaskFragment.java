package sample.example.com.proxitask.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.model.APISingleResponse;
import sample.example.com.proxitask.model.UserTask;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TaskService;
import sample.example.com.proxitask.network.TokenStore;


public class CreateTaskFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btnCreateTask;
    private TaskService taskService;

    EditText title,desc,rewards,date,address,km;

    private OnFragmentInteractionListener mListener;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    public static CreateTaskFragment newInstance(String param1, String param2) {
        CreateTaskFragment fragment = new CreateTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_create_task, container, false);

        title = v.findViewById(R.id.edt_task_title);
        desc = v.findViewById(R.id.edt_task_decrription);
        rewards = v.findViewById(R.id.edt_reward);
        date = v.findViewById(R.id.edt_date);
        address = v.findViewById(R.id.edt_address);
//        km = v.findViewById(R.id.edt_km);



        btnCreateTask = v.findViewById(R.id.btn_save_task);
        taskService = RetrofitInstance.getRetrofitInstance().create(TaskService.class);
        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double price = Double.parseDouble(rewards.getText().toString());

                String taskTitle = title.getText().toString();
                String taskDesc = desc.getText().toString();
                String taskDate = date.getText().toString();
                Double longitude =  -79.23051763300265;
                Double latitude =43.78443607023798;
                String taskAddress = address.getText().toString();

                /* use system settings instead */
//                int radius = Integer.parseInt(km.getText().toString() );
                int radius = 5;

                UserTask task = new UserTask(taskTitle,taskDesc,price,latitude,longitude,taskAddress,taskDate,radius);

              saveTask(task);
            }
        });


        return v;
    }

    private void saveTask(UserTask userTask)
    {
        taskService.addTask(TokenStore.getToken(getContext()),userTask).enqueue(new Callback<APISingleResponse>() {
            @Override
            public void onResponse(Call<APISingleResponse> call, Response<APISingleResponse> response) {
              UserTask task = response.body().getData();
                Toast.makeText(getContext(),"Task "+task.getTitle()+" is saved!",Toast.LENGTH_LONG).show();

                showCreatedTask(task);

            }

            @Override
            public void onFailure(Call<APISingleResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Call failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showCreatedTask(UserTask task) {
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        DisplayCreatedTaskFragment mfragment=new DisplayCreatedTaskFragment();

        Bundle bundle=new Bundle();
        bundle.putString("title",task.getTitle());
        bundle.putString("desc",task.getDescription());
        bundle.putDouble("price",task.getPrice());
        bundle.putString("date",task.getDate());
        bundle.putString("address",task.getAddress());
        bundle.putInt("radius",task.getRadius());
        mfragment.setArguments(bundle); //data being send to SecondFragment
        transaction.replace(R.id.fragment_main, mfragment);
        transaction.commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
