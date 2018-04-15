package sample.example.com.proxitask.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.model.APINotificationResponse;
import sample.example.com.proxitask.model.MsgToken;
import sample.example.com.proxitask.model.NotificationBean;
import sample.example.com.proxitask.network.NotificationService;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TokenStore;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    private NotificationAdapter notificationAdapter;
    private List<NotificationBean> data;
    private NotificationService notificationService;

    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        notificationService = RetrofitInstance.getRetrofitInstance().create(NotificationService.class);
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.list_view_notification);
        notificationAdapter = new NotificationAdapter(getActivity());
        listView.setAdapter(notificationAdapter);
    }

    private void loadData(String msgToken)
    {
        notificationService.getNotification(TokenStore.getToken(getContext()),new MsgToken(msgToken)).enqueue(new Callback<APINotificationResponse>() {
            @Override
            public void onResponse(Call<APINotificationResponse> call, Response<APINotificationResponse> response) {
                NotificationBean bean = response.body().getData();
                data.add(bean);
                notificationAdapter.setData(data);
                Toast.makeText(getContext(),"Call Succeed !",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<APINotificationResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Call failed",Toast.LENGTH_LONG).show();
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



    /*adapter for ListView */
    class NotificationAdapter extends BaseAdapter
    {
        private LayoutInflater inflater;
        private List<NotificationBean> data= new ArrayList<>();

        public NotificationAdapter(Context context)
        {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<NotificationBean> data)
        {
            this.data = data;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            NotificationViewHolder  viewHolder = null;
            NotificationBean bean = (NotificationBean) getItem(i);
            if(view == null)
            {
                 view = inflater.inflate(R.layout.item_notification_list,null);
                 viewHolder = new NotificationViewHolder();
                 viewHolder.tvTitle = view.findViewById(R.id.tv_title);
                 viewHolder.tvMessage = view.findViewById(R.id.tv_message);
                 view.setTag(viewHolder);
            }
            else
            {
                viewHolder = (NotificationViewHolder) view.getTag();
            }
            viewHolder.tvTitle.setText(bean.getTitle());
            viewHolder.tvMessage.setText(bean.getMessage());
            return view;
        }
    }
    class NotificationViewHolder
    {
        TextView tvTitle;
        TextView tvMessage;
    }
}
