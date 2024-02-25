package com.example.smartbin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContainerFragment extends AppCompatActivity {
    ImageButton menuLateral;
    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;
    TextView textoVariable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_fragment);
        bottomNavigationView =  findViewById(R.id.bottomNavigationView);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
        menuLateral = findViewById(R.id.barra_lateral);
        menuLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ContainerFragment.this, "Hola", Toast.LENGTH_SHORT).show();
            }
        });

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
}