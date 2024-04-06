package com.example.smartbin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.zip.Inflater;

public class ContainerFragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton menuLateral;
    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;
    TextView textoVariable, nombreUsuario;;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    MenuItem menuItem;
    Toolbar toolbarsearch;
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
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Inflar el menu lateral
        View headerLateralMenu = navigationView.getHeaderView(0);
        nombreUsuario = headerLateralMenu.findViewById(R.id.nombreUsuario);
        toolbarsearch = findViewById(R.id.toolbarSearch);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        for (int i = 0; i<20;i++){
            if (currentUser.getDisplayName() != null) {
                nombreUsuario.setText(currentUser.getDisplayName());
                nombreUsuario.setTextColor(getResources().getColor(R.color.black));
                String nombreUsuarioTexto = nombreUsuario.getText().toString();
                Log.d("nombre usuario:", "Tu nombre de usuario es: " + currentUser.getDisplayName()
                        + " gei" );
            } else if (currentUser == null) {
                Log.d("Sin nombre de usuario", "sin nombre de usuario");
            }
        }


        //elementos internos del menu lateral
        navController = navHostFragment.getNavController();
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                int idFragment = navController.getCurrentDestination().getId();
                textoVariable = findViewById(R.id.textoIntercambiable);

                if (idFragment == R.id.agregarContenedor){
                    textoVariable.setText("Agregar");
                    toolbarsearch.setVisibility(View.INVISIBLE);
                }
                if (idFragment == R.id.contenedores){
                    textoVariable.setText("Contenedores");
                    toolbarsearch.setVisibility(View.VISIBLE);
                }
                if (idFragment == R.id.estadisticas){
                    textoVariable.setText("Estadisticas");
                    toolbarsearch.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflamos nuestro menu
        getMenuInflater().inflate(R.menu.search, menu);
        menuItem = menu.findItem(R.id.search);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}