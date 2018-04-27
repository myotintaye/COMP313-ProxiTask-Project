package sample.example.com.proxitask.activity.myTasks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.adapter.TaskAppliedAdapter;
import sample.example.com.proxitask.model.APIMyTasksResponse;
import sample.example.com.proxitask.model.Task;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TaskService;
import sample.example.com.proxitask.network.TokenStore;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyTasksAppliedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyTasksAppliedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTasksAppliedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private RecyclerView recyclerView;
    private TaskAppliedAdapter adapter;
    private List<Task> taskList;

    private TaskService taskService;

    public MyTasksAppliedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyTasksAppliedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyTasksAppliedFragment newInstance(String param1, String param2) {
        MyTasksAppliedFragment fragment = new MyTasksAppliedFragment();
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

        taskService = RetrofitInstance.getRetrofitInstance().create(TaskService.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_tasks_applied, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_task_applied);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);


        TextView noTasks = view.findViewById(R.id.tv_notasksApplied);
        noTasks.setVisibility(View.INVISIBLE);

        taskService.getMyAppliedTasks(TokenStore.getToken(getContext())).enqueue(new Callback<APIMyTasksResponse>() {
            @Override
            public void onResponse(Call<APIMyTasksResponse> call, Response<APIMyTasksResponse> response) {

                taskList = response.body().getTaskList();

                if (taskList != null){
                    adapter = new TaskAppliedAdapter(getContext(), taskList);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                }
                else{
                    noTasks.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<APIMyTasksResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Having troubles in pulling task data. Please try again later.",Toast.LENGTH_LONG).show();
            }
        });

        return view;

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
