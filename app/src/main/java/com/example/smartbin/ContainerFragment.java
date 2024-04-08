package com.example.smartbin;

import static java.security.AccessController.getContext;

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

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                    }, 1000);
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
                    }, 1000);

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
                    }, 1000);
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

        //Inflar el menu lateral
        View headerLateralMenu = navigationView.getHeaderView(0);
        nombreUsuario = headerLateralMenu.findViewById(R.id.nombreUsuario);
        toolbarsearch = findViewById(R.id.toolbarSearch);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        for (int i = 0; i<20;i++){
            if (currentUser.getDisplayName() != null) {
                nombreUsuario.setText(currentUser.getDisplayName());
                nombreUsuario.setTextColor(getResources().getColor(R.color.white));
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
        finish();
    }
}