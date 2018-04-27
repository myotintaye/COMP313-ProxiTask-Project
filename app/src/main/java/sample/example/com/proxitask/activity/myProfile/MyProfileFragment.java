package sample.example.com.proxitask.activity.myProfile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.activity.EditProfileActivity;
import sample.example.com.proxitask.model.APIUserResponse;
import sample.example.com.proxitask.model.User;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TokenStore;
import sample.example.com.proxitask.network.UserService;

public class MyProfileFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView username,email,address,phone,completedTasks;

    private User user;
    private UserService userService;

    MenuItem updateProfile;


    private OnFragmentInteractionListener mListener;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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

        userService = RetrofitInstance.getRetrofitInstance().create(UserService.class);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        updateProfile = view.findViewById(R.id.update_profile);

        username = view.findViewById(R.id.txt_username_myprofile);
        address = view.findViewById(R.id.txt_address_myprofile);
        email = view.findViewById(R.id.txt_email_myprofile);
        phone = view.findViewById(R.id.txt_phone_myprofile);
        completedTasks = view.findViewById(R.id.txt_num_of_completed_tasks_myprofile);

        /* Get user info */
        fetchUser();

        return view;
    }

    private void fetchUser() {
        userService.getUser(TokenStore.getToken(getContext())).enqueue(new Callback<APIUserResponse>() {
            @Override
            public void onResponse(Call<APIUserResponse> call, Response<APIUserResponse> response) {
                user = response.body().getUser();

                if (user != null){

                    /* Load the UI */
                    checkAndSetText(username, user.getUserName());
                    checkAndSetText(address, user.getAddress());
                    checkAndSetText(phone, user.getPhone());
                    checkAndSetText(email,user.getEmail());
//                    completedTasks.setText(user.getTaskCompleted().length);
                }
            }

            @Override
            public void onFailure(Call<APIUserResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Having troubles in pulling user data. Please try again later.",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkAndSetText(TextView t, String value){
        if (value != null){
            t.setText(value);
        }
        else{
            t.setText("null");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        fetchUser();
        super.onResume();
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
