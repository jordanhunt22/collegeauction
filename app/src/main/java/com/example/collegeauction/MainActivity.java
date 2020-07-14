package com.example.collegeauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.collegeauction.LoginFragments.LoginFragment;
import com.example.collegeauction.MainFragments.CreationFragment;
import com.example.collegeauction.MainFragments.FavoritesFragment;
import com.example.collegeauction.MainFragments.HomeFragment;
import com.example.collegeauction.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Implementing ViewBinding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // Sets the toolbar text to nothing
        getSupportActionBar().setTitle("");

        // Sets up action for the floating action button
        fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new CreationFragment();
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack("home").commit();
            }
        });

        fragment = new HomeFragment();

        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();

        // Resolves the bottom navigation bar
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_favorites:
                    default:
                        fragment = new FavoritesFragment();
                        break;
//                    case R.id.action_profile:
//                    default:
//                        fragment = new ProfileFragment();
//                        Bundle args = new Bundle();
//                        args.putParcelable("user", Parcels.wrap(ParseUser.getCurrentUser()));
//                        fragment.setArguments(args);
//                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}