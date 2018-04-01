package sample.example.com.proxitask.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import sample.example.com.proxitask.R;

public class DisplayCreatedTaskFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    MapView mapView;


    public DisplayCreatedTaskFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DisplayCreatedTaskFragment newInstance(String param1, String param2) {
        DisplayCreatedTaskFragment fragment = new DisplayCreatedTaskFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = getArguments();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_created_task, container, false);


        populateForm(view);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);


        return view;
    }

    private void populateForm(View view) {
        Bundle bundle = getArguments();
        TextView title = view.findViewById(R.id.txt_task_title);
        TextView desc = view.findViewById(R.id.txt_task_desc);
        TextView price = view.findViewById(R.id.txt_price);
        TextView date = view.findViewById(R.id.txt_date);
        TextView address = view.findViewById(R.id.txt_address);
        TextView radius = view.findViewById(R.id.txt_radius);

        String taskTitle = bundle.getString("title");
        String taskDesc = bundle.getString("desc");
        Double taskPrice = bundle.getDouble("price");
        String taskDate = bundle.getString("date");
        String taskAddress = bundle.getString("address");
        int taskRadius = bundle.getInt("radius");

        title.setText(taskTitle);
        desc.setText(taskDesc);
        price.setText(taskPrice.toString());
        date.setText(taskDate);
        address.setText(taskAddress);
        radius.setText(taskRadius + "");
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
    public void onMapReady(GoogleMap googleMap) {
        // Updates the location and zoom of the MapView
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(43.1, -87.9)));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        googleMap.moveCamera(cameraUpdate);
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


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
