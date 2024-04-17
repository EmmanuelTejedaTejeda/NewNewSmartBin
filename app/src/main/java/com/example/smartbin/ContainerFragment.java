package com.example.smartbin;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    private TextView textoVariable, nombreUsuario, float_database, int_database;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    DatabaseReference referenciaFloat;
    DatabaseReference referenceInt;

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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID =  item.getItemId();
                if (itemID == R.id.logout){
                    SpannableString s = new SpannableString("logout");
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.casiblanco)),0,s.length(),0);
                    SpannableString a = new SpannableString("logout");
                    a.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),0,s.length(),0);
                    item.setTitle(s);
                    item.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.casiblanco)));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run(){
                            item.setTitle(a);
                            item.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                            cerrarSesion();
                        }
                    }, 200);
                    return true;
                }
                if (itemID == R.id.setting){
                    SpannableString s = new SpannableString("settings");
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.casiblanco)),0,s.length(),0);
                    SpannableString a = new SpannableString("settings");
                    a.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),0,s.length(),0);
                    item.setTitle(s);
                    item.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.casiblanco)));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            item.setTitle(a);
                            Intent settings = new Intent(ContainerFragment.this, settings.class);
                            item.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                            startActivity(settings);
                        }
                    }, 200);

                    return true;
                }
                if (itemID == R.id.share){
                    SpannableString s = new SpannableString("share");
                    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.casiblanco)),0,s.length(),0);
                    SpannableString a = new SpannableString("share");
                    a.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),0,s.length(),0);
                    item.setTitle(s);
                    item.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.casiblanco)));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            item.setTitle(a);
                            item.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                            Toast.makeText(ContainerFragment.this, "no we", Toast.LENGTH_SHORT).show();
                        }
                    }, 200);
                }
                return true;
            }
        });

        menuLateral = findViewById(R.id.barra_lateral);
        menuLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        View headerLateralMenu = navigationView.getHeaderView(0);
        nombreUsuario = headerLateralMenu.findViewById(R.id.nombreUsuario);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference nombrereferencia = databaseReference.child("datosUsuariosRegistrados");
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
                    animateIconScale(iconView, true);
                }
                if (idFragment == R.id.contenedores) {
                    textoVariable.setText("Contenedores");
                    animateIconScale(iconView, true);
                }
                if (idFragment == R.id.estadisticas) {
                    textoVariable.setText("Estadisticas");
                    animateIconScale(iconView, true);
                }
            }
        });
        float_database = findViewById(R.id.float_database);
        int_database = findViewById(R.id.int_database);
        referenciaFloat = FirebaseDatabase.getInstance().getReference();
        referenciaFloat.child("test").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Obtener valor flotante
                float valorFloat = snapshot.child("float").getValue(Float.class);
                // Mostrar el valor en tu TextView
                float_database.setText(String.valueOf(valorFloat));
                float_database.setTextColor(getResources().getColor(R.color.black));

                // Obtener valor entero
                int valorInt = snapshot.child("int").getValue(Integer.class);
                // Mostrar el valor en tu TextView
                int_database.setText(String.valueOf(valorInt));
                int_database.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar cancelación de la operación
                Log.e("Error", "Error en la lectura de la base de datos: " + error.getMessage());
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
        getMenuInflater().inflate(R.menu.lateral_bar, menu);
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
