package sample.example.com.proxitask.activity;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sample.example.com.proxitask.R;

public class MyTasksActivity_NotUsed extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FragmentManager fragmentManager;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks_not_used);

        fragmentManager = getSupportFragmentManager();
        buildFAB();
//        buildDrawer();
    }

    private void buildFAB() {
        fab = (FloatingActionButton) findViewById(R.id.fab_add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Create your task!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //create your task fragment
                setTitle("Create a UserTask");
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
            Intent intent = new Intent(this, MyTasksActivity_NotUsed.class);
            startActivity(intent);

        } else if (id == R.id.nav_noti) {
            setTitle("Notifications");
            NotificationFragment notificationFragment = new NotificationFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, notificationFragment).commit();

            fab.hide();

        } else if (id == R.id.nav_account_settings) {

        } else if (id == R.id.nav_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MyTasksActivity_NotUsed.this, LoginActivity.class));
                            finish();
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
