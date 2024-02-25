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

public class Register extends AppCompatActivity {
    EditText Rcorreo, Rcontrasena;
    Button registrarse;
    TextView Rsubrayado;
    ImageButton ShowPassword;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        Rcorreo = findViewById(R.id.Rcorreo);
        Rcontrasena = findViewById(R.id.Rcontrasena);
        registrarse = findViewById(R.id.Rregistro);
        registrarse.setEnabled(true);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro(Rcorreo, Rcontrasena, auth);
            }
        });
        Rsubrayado = findViewById(R.id.subrayado_ingresar);
        Rsubrayado.setEnabled(true);
        subrayado(Rsubrayado);
        Rsubrayado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });
        ShowPassword = findViewById(R.id.showPasswordRegister);
        ShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarcontrasena();
            }
        });
    }
    public void registro(EditText Rcorreo, EditText Rcontrasena, FirebaseAuth auth){
        registrarse.setEnabled(false);
        Rsubrayado.setEnabled(false);
        String textCorreo = Rcorreo.getText().toString();
        String textContrasena = Rcontrasena.getText().toString();
        if (!textContrasena.isEmpty() && !textCorreo.isEmpty()){
            auth.createUserWithEmailAndPassword(textCorreo, textContrasena)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Error de registro",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
        else{
            Toast.makeText(Register.this, "Favor de ingresar correo o  contraseña", Toast.LENGTH_SHORT).show();
        }
    }
    public void subrayado(TextView Rsubrayado){
        SpannableString mi_texto = new SpannableString("INGRESAR");
        mi_texto.setSpan(new UnderlineSpan(),0,mi_texto.length(),0);
        Rsubrayado.setText(mi_texto);
    }

    public void mostrarcontrasena(){
        if (TextUtils.isEmpty(Rcontrasena.getText())) {
            return;
        }
        int inputType = Rcontrasena.getInputType();
        if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
            Rcontrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else{
            Rcontrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        Rcontrasena.setSelection(Rcontrasena.getText().length());
    }
}