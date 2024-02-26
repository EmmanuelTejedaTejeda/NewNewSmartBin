package com.example.smartbin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ContainerFragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton menuLateral;
    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;
    TextView textoVariable;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_fragment);
        bottomNavigationView =  findViewById(R.id.bottomNavigationView);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());


        //Declaracion de los componentes de el menu lateral
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.lateral_menu);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        menuLateral = findViewById(R.id.barra_lateral);
        menuLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);}
        });

        //elementos internos del menu lateral


        navController = navHostFragment.getNavController();
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                int idFragment = navController.getCurrentDestination().getId();
                textoVariable = findViewById(R.id.textoIntercambiable);
                if (idFragment == R.id.agregarContenedor){
                    textoVariable.setText("Agregar");
                }
                if (idFragment == R.id.contenedores){
                    textoVariable.setText("Contenedores");
                }
                if (idFragment == R.id.estadisticas){
                    textoVariable.setText("Estadisticas");
                }
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}