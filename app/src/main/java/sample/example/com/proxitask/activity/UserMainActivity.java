package sample.example.com.proxitask.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.example.com.proxitask.R;
import sample.example.com.proxitask.adapter.CustomBaseAdapter;
import sample.example.com.proxitask.model.APIResponse;
import sample.example.com.proxitask.model.UserTask;
import sample.example.com.proxitask.network.RetrofitInstance;
import sample.example.com.proxitask.network.TaskService;
import sample.example.com.proxitask.network.TokenStore;

public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NotificationFragment.OnFragmentInteractionListener,
        MyProfileFragment.OnFragmentInteractionListener,
        CreateTaskFragment.OnFragmentInteractionListener,
        DisplayCreatedTaskFragment.OnFragmentInteractionListener,
        DisplayTaskDetailFragment.OnFragmentInteractionListener,
        MyTasksFragment.OnFragmentInteractionListener,
        AccountSettingsFragment.OnFragmentInteractionListener
{

    FragmentManager fragmentManager;
    TaskService taskService;
    ListView tasksListView;
    ArrayAdapter<String> taskAdapter;
    ProgressBar  progressBar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        fragmentManager = getSupportFragmentManager();

        progressBar= findViewById(R.id.progressBar2);

        tasksListView = findViewById(R.id.list_all_tasks);

        taskAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item);

        taskService = RetrofitInstance.getRetrofitInstance().create(TaskService.class);
        buildFAB();
        buildDrawer();

        populateTasksListView();

    }

    private void populateTasksListView() {
        taskService.getAllTasks(TokenStore.getToken(this)).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                List<UserTask> tasks = response.body().getData();
                List<TaskItem> rowItems=new ArrayList<TaskItem>();


                for (UserTask task : tasks) {

                    rowItems.add(new TaskItem(task.getRadius(),task.getTitle(),task.getDescription()));
                }
                //taskAdapter.addAll(taskNames);
                CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(),rowItems);
                tasksListView.setAdapter(customBaseAdapter);
                progressBar.setVisibility(View.GONE);

                /* set onClick */
                tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    public void onItemClick(AdapterView<?> adapter, View v, int position, long arg){

                        UserTask task = tasks.get(position);

                        Bundle bundle=new Bundle();
                        bundle.putString("title",task.getTitle());
                        bundle.putString("desc",task.getDescription());
                        bundle.putDouble("price",task.getPrice());
                        bundle.putString("date",task.getDate());
                        bundle.putString("address",task.getAddress());
                        bundle.putInt("radius",task.getRadius());

                        DisplayTaskDetailFragment displayTaskDetailfragment=new DisplayTaskDetailFragment();
                        displayTaskDetailfragment.setArguments(bundle);
                        
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_main, displayTaskDetailfragment).commit();

                        fab.hide();
                    }
                });
            }


            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Call failed",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void buildFAB() {
        fab = (FloatingActionButton) findViewById(R.id.fab_add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Create your task!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //create your task fragment
                setTitle("Create a Task");
                CreateTaskFragment createTaskFragment = new CreateTaskFragment();

                fragmentManager.beginTransaction().replace(R.id.fragment_main, createTaskFragment).commit();
                fab.hide();
            }
        });
    }

    private void buildDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Add Item selection listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        // User name display labels
        TextView drawerEmail = headerView.findViewById(R.id.txt_user_email);
        TextView drawerUserName = headerView.findViewById(R.id.txt_user_name);

        // Updating User Information in Drawer Header
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            drawerEmail.setText(currentUser.getEmail());
            //need to fix - not showing the name from database
            drawerUserName.setText(currentUser.getDisplayName());

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, UserMainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_inbox) {

        } else if (id == R.id.nav_profile) {

            setTitle("My Profile");
            MyProfileFragment profileFragment = new MyProfileFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, profileFragment).commit();

            fab.hide();

        } else if (id == R.id.nav_task) {
            setTitle("My Tasks");
            MyTasksFragment myTasksFragment = new MyTasksFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, myTasksFragment).commit();

            fab.hide();

        } else if (id == R.id.nav_noti) {
            setTitle("Notifications");
            NotificationFragment notificationFragment = new NotificationFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, notificationFragment).commit();

            fab.hide();

        } else if (id == R.id.nav_account_settings) {
            setTitle("Account Settings");
            AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, accountSettingsFragment).commit();

            fab.hide();
        } else if (id == R.id.nav_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(UserMainActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
