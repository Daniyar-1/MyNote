package com.example.mynote;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mynote.utils.Prefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_note,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_profile)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

  /*      boolean isShown = new Prefs(this).isShown();
        if (!isShown)
            navController.navigate(R.id.boardFragment);
    else if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            navController.navigate(R.id.phoneFragment);
        }*/


        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_note ||
                        destination.getId() == R.id.navigation_dashboard ||
                        destination.getId() == R.id.navigation_notifications ||
                        destination.getId() == R.id.navigation_profile) {
                    navView.setVisibility(View.VISIBLE);
                } else {
                    navView.setVisibility(View.GONE);
                }
                if (destination.getId() == R.id.boardFragment || destination.getId() == R.id.phoneFragment) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();

    }
}