package sample.example.com.proxitask.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.adapter.PlaceArrayAdapter;
import sample.example.com.proxitask.model.APISingleResponse;
import sample.example.com.proxitask.model.UserTask;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TaskService;
import sample.example.com.proxitask.network.TokenStore;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class CreateTaskFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks  {

    private static final int GOOGLE_API_CLIENT_ID = 0;
    //address
    private AutoCompleteTextView address;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    Button btnCreateTask;
    private TaskService taskService;

    EditText title,desc,rewards,date,km;

    private LatLng location;

    private OnFragmentInteractionListener mListener;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    public static CreateTaskFragment newInstance(String param1, String param2) {
        CreateTaskFragment fragment = new CreateTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
//        km = v.findViewById(R.id.edt_km);

        btnCreateTask = v.findViewById(R.id.btn_save_task);
        taskService = RetrofitInstance.getRetrofitInstance().create(TaskService.class);

        address = (AutoCompleteTextView) v.findViewById(R.id.edt_address);
        address.setThreshold(3);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        address.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        address.setAdapter(mPlaceArrayAdapter);

        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double price = Double.parseDouble(rewards.getText().toString());

                String taskTitle = title.getText().toString();
                String taskDesc = desc.getText().toString();
                String taskDate = date.getText().toString();
                Double longitude =  location.longitude;
                Double latitude = location.latitude;
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
        taskService.addTask(TokenStore.getToken(getContext()),userTask).enqueue(
                new Callback<APISingleResponse>() {
            @Override
            public void onResponse(Call<APISingleResponse> call, Response<APISingleResponse> response) {
              UserTask task = response.body().getData();
                Toast.makeText(getContext(),"Task "+task.getTitle()
                        +" is saved!",Toast.LENGTH_LONG).show();

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
        bundle.putDouble("lat",task.getLat());
        bundle.putDouble("long",task.getLon());
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
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
//            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
//                Log.e(TAG, "Place query did not complete. Error: " +
//                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            location = place.getLatLng();


        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
//        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
//        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

//        Log.e(TAG, "Google Places API connection failed with error code: "
//                + connectionResult.getErrorCode());

        Toast.makeText(getActivity().getApplicationContext(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

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
