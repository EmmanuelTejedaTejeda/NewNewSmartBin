package com.example.smartbin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class settings extends AppCompatActivity {
    ImageButton backtoHome;
    TextView nombreUsuario, correoUsuario;
    FirebaseUser nombreUsuarioReferencia;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceNombre;
    DatabaseReference databaseReferencecorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        backtoHome = findViewById(R.id.backtohome);
        nombreUsuario = findViewById(R.id.nombreUsuario);
        correoUsuario = findViewById(R.id.correoUsuario);
        mAuth = FirebaseAuth.getInstance();
        nombreUsuarioReferencia = mAuth.getCurrentUser();
        String uid = nombreUsuarioReferencia.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceNombre = databaseReference.child("usuarios");
        databaseReferencecorreo = databaseReference.child("correos");
        databaseReferenceNombre.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("nombreUsuario").getValue(String.class);
                String a = "<b>Nombre de usuario: </b>" + name;
                nombreUsuario.setText(Html.fromHtml(a));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", "Hubo un error al llamar el nombre de usuario");
            }
        });
        databaseReferencecorreo.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String correo = snapshot.child("correoUsuario").getValue(String.class);
                String a = "<b>Correo: </b>" + correo;
                correoUsuario.setText(Html.fromHtml(a));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", "hubo un error al llamar al correo del usuario");
            }
        });
        backtoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });
    }
    private void regresar(){
        backtoHome.setBackgroundTintList(getResources().getColorStateList(R.color.gris));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backtoHome.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                Intent intent = new Intent(settings.this, ContainerFragment.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }
}