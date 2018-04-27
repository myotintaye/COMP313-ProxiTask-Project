package sample.example.com.proxitask.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.model.APISingleResponse;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TaskService;
import sample.example.com.proxitask.network.TokenStore;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayTaskDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayTaskDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayTaskDetailFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    MapView mapView;
    Button btnApplyTask;

    private String taskId;
    private TaskService taskService;

    public DisplayTaskDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DisplayTaskDetailFragment newInstance(String param1, String param2) {

        DisplayTaskDetailFragment fragment = new DisplayTaskDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        taskService = RetrofitInstance.getRetrofitInstance().create(TaskService.class);
//
//        Bundle bundle = getArguments();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_task_detail, container, false);

        populateForm(view);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);

        return view;
    }

    private void populateForm(View view) {
        Bundle bundle = getArguments();
        TextView title = view.findViewById(R.id.txt_task_title);
        TextView desc = view.findViewById(R.id.txt_task_desc);
        TextView price = view.findViewById(R.id.txt_price);

//        TextView date = view.findViewById(R.id.txt_date);
        TextView time = view.findViewById(R.id.txt_task_time);

        TextView address = view.findViewById(R.id.txt_address);
        TextView radius = view.findViewById(R.id.txt_radius);


        String taskTitle = "placeholder";
        String taskDesc = "placeholder";
        String taskTime = "placeholder";
        String taskAddress = "placeholder";
        double taskPrice = 1.0;

        if (bundle != null){
            if (bundle.getString("title") != null){
                taskTitle = bundle.getString("title");
            }
            else{
                taskTitle = "Title not existed";
            }

            if (bundle.getString("desc") != null){
                taskDesc = bundle.getString("desc");
            }
            else{
                taskDesc = "Description not existed";
            }

            if (bundle.getDouble("price") != 0.0){
                taskPrice = bundle.getDouble("price");
            }
            else{
                taskPrice = 1.0;
            }


            if (bundle.getString("date") != null){
                taskTime = bundle.getString("date");
            }
            else{
                taskTime = "Date not existed";
            }

//        String taskDate = bundle.getString("date");


            if (bundle.getString("address") != null){
                taskAddress = bundle.getString("address");
            }
            else{
                taskAddress = "Address not existed";
            }

        }

//        int taskRadius = bundle.getInt("radius");

        /* no longer set by user */
        int taskRadius = 5;

        title.setText(taskTitle);
        desc.setText(taskDesc);
        price.setText(String.valueOf(taskPrice));

//        date.setText(taskDate);
        time.setText(taskTime);

        address.setText(taskAddress);
        radius.setText(taskRadius + "");

        btnApplyTask = view.findViewById(R.id.btn_apply_task);

        btnApplyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                taskId = bundle.getString("taskId");
                applyTask(taskId);
            }
        });

    }

    public void applyTask(String taskId){
        taskService.applyTask(TokenStore.getToken(getContext()), taskId).enqueue(new Callback<APISingleResponse>() {
            @Override
            public void onResponse(Call<APISingleResponse> call, Response<APISingleResponse> response) {
                Toast.makeText(getContext(),"Task applied, please wait for task owner to review.",Toast.LENGTH_LONG).show();

                /* Disable the color */
                btnApplyTask.setText(getString(R.string.btn_apply_task_applied));
                btnApplyTask.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                btnApplyTask.setEnabled(false);
            }

            @Override
            public void onFailure(Call<APISingleResponse> call, Throwable t) {
                Toast.makeText(getContext(),"There is something wrong. Please try again later.",Toast.LENGTH_LONG).show();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Updates the location and zoom of the MapView
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(43.1, -87.9)));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        googleMap.moveCamera(cameraUpdate);
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
