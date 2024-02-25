package com.example.smartbin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextView registrarse;
    Button ingresar;
    EditText correo, contrasena;
    ImageButton showPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        subrayado();
        correo = findViewById(R.id.correo);
        contrasena = findViewById(R.id.contrasena);
        ingresar = findViewById(R.id.ingresar);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textCorreo = correo.getText().toString();
                String textContrasena = contrasena.getText().toString();

                if (!textCorreo.isEmpty() && !textContrasena.isEmpty()){
                    auth.signInWithEmailAndPassword(textCorreo, textContrasena)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Intent intent = new Intent(Login.this, ContainerFragment.class);
                                        startActivity(intent);
                                        Toast.makeText(Login.this, "Ingreso exitoso", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Login.this, "Error de acceso",
                                                Toast.LENGTH_SHORT).show();
                                        correo.setText("");
                                        contrasena.setText("");
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(Login.this,"Favor, ingrese correo o contraseña para continuar", Toast.LENGTH_SHORT).show();
                }
            }

        });
        registrarse = findViewById(R.id.registrarse);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,  Register.class);
                startActivity(intent);
            }
        });
        showPassword = findViewById(R.id.showPassword);
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(contrasena.getText())) {
                    return;
                }
                int inputType = contrasena.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    contrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    contrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                contrasena.setSelection(contrasena.getText().length());
            }
        });
    }

    public void subrayado(){
        registrarse = (TextView) findViewById(R.id.registrarse);
        SpannableString miTexto = new SpannableString("REGISTRARSE");
        miTexto.setSpan(new UnderlineSpan(),0,miTexto.length(),0);
        registrarse.setText(miTexto);
    }
}