package com.example.smartbin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContainerFragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageButton menuLateral;
    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private TextView textoVariable, nombreUsuario;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private Toolbar toolbarsearch;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_fragment);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.lateral_menu);
        navigationView.bringToFront();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        menuLateral = findViewById(R.id.barra_lateral);
        menuLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        View headerLateralMenu = navigationView.getHeaderView(0);
        nombreUsuario = headerLateralMenu.findViewById(R.id.nombreUsuario);
        toolbarsearch = findViewById(R.id.toolbarSearch);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference nombrereferencia = databaseReference.child("usuarios");
        String uid = currentUser.getUid();
        nombrereferencia.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = snapshot.child("nombreUsuario").getValue(String.class);
                    nombreUsuario.setText(nombre);
                    nombreUsuario.setTextColor(getResources().getColor(R.color.white));
                } else {
                    Log.d("Sin nombre de usuario", "sin nombre de usuario en la base de datos");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Error al obtener el nombre de usuario: " + error.getMessage());
            }
        });

        navController = navHostFragment.getNavController();
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                int idFragment = navController.getCurrentDestination().getId();
                textoVariable = findViewById(R.id.textoIntercambiable);
                resetIconScale(bottomNavigationView);
                View iconView = bottomNavigationView.findViewById(idFragment);
                if (idFragment == R.id.agregarContenedor) {
                    textoVariable.setText("Agregar");
                    toolbarsearch.setVisibility(View.INVISIBLE);
                    animateIconScale(iconView, true);
                }
                if (idFragment == R.id.contenedores) {
                    textoVariable.setText("Contenedores");
                    toolbarsearch.setVisibility(View.VISIBLE);
                    animateIconScale(iconView, true);
                }
                if (idFragment == R.id.estadisticas) {
                    textoVariable.setText("Estadisticas");
                    toolbarsearch.setVisibility(View.INVISIBLE);
                    animateIconScale(iconView, true);
                }
            }
        });
    }

    private void resetIconScale(BottomNavigationView bottomNavigationView) {
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem item = bottomNavigationView.getMenu().getItem(i);
            View iconView = bottomNavigationView.findViewById(item.getItemId());
            animateIconScale(iconView, false);
        }
    }

    private void animateIconScale(View iconView, boolean isSelected) {
        float targetScale = isSelected ? 1.8f : 1.0f;
        iconView.animate().scaleX(targetScale).scaleY(targetScale).setDuration(100).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        getMenuInflater().inflate(R.menu.lateral_bar, menu);
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

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
        finish();
    }
}
