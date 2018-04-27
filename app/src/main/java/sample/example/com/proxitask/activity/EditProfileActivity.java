package sample.example.com.proxitask.activity;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.activity.myProfile.MyProfileFragment;
import sample.example.com.proxitask.adapter.PlaceArrayAdapter;
import sample.example.com.proxitask.model.APIUserResponse;
import sample.example.com.proxitask.model.User;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TokenStore;
import sample.example.com.proxitask.network.UserService;

public class EditProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, MyProfileFragment.OnFragmentInteractionListener {


    private static final int GOOGLE_API_CLIENT_ID = 0;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    //address
    private AutoCompleteTextView address;
    private EditText name,phone,email;
    private TextView txtSaveUser;
    private UserService userService;
    private LatLng location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        address = findViewById(R.id.et_address);
        address.setThreshold(3);
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .build();


        address.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        address.setAdapter(mPlaceArrayAdapter);
        name = findViewById(R.id.et_name);
        phone = findViewById(R.id.et_phone_number);
        email = findViewById(R.id.et_email);

        txtSaveUser = findViewById(R.id.tv_save);


        userService = RetrofitInstance.getRetrofitInstance().create(UserService.class);

        txtSaveUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userPhone = phone.getText().toString();
                String userEmail = email.getText().toString();
                String userAddress = address.getText().toString();
                Double lat = location.latitude;
                Double lon = location.longitude;

                User user = new User(userName,userAddress,userEmail,userPhone,lat,lon);
                editUserProfile(user);

                finish();
            }
        });

    }

    private void editUserProfile(User updateUser)
    {
        userService.updateUser(TokenStore.getToken(getApplicationContext()),updateUser).enqueue(new Callback<APIUserResponse>() {
            @Override
            public void onResponse(Call<APIUserResponse> call, Response<APIUserResponse> response) {
                User updateUser = response.body().getUser();
                Toast.makeText(getApplicationContext(),"User is updated! ",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<APIUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"User update is not successful! ",Toast.LENGTH_LONG).show();

            }
        });
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
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
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

        Toast.makeText(getApplicationContext(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
