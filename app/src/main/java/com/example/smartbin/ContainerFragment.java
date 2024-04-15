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
    private TextView textoVariable, nombreUsuario;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private Toolbar toolbarsearch;
    private MenuItem buscador;
    MainAdapter mainAdapter;

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
        toolbarsearch = findViewById(R.id.toolbarSearch);
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
        buscador = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) buscador.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscar(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                buscar(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void buscar(String str){
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Contenedores").orderByChild("Direccion").startAt(str).endAt(str+"~"),MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();

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
