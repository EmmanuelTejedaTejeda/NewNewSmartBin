package com.example.smartbin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    EditText Rcorreo, Rcontrasena, Rusuario, Rtelefono, Rcontrasenarepetida;
    Button registrarse;
    TextView Rsubrayado;
    ImageButton ShowPassword, showPasswordRepedtida;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        Rcorreo = findViewById(R.id.Rcorreo);
        Rcontrasena = findViewById(R.id.Rcontrasena);
        Rusuario = findViewById(R.id.Rusuario);
        Rtelefono = findViewById(R.id.Rtelefono);
        Rcontrasenarepetida = findViewById(R.id.Rcontrasenarepetida);
        registrarse = findViewById(R.id.Rregistro);
        registrarse.setEnabled(true);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro(Rcorreo, Rcontrasena, auth, Rusuario, Rtelefono, Rcontrasenarepetida);
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
                mostrarContrasena();
            }
        });
        showPasswordRepedtida = findViewById(R.id.showPasswordRegisterRepetida);
        showPasswordRepedtida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarContrasenaRepetida();
            }
        });
    }
    public void registro(EditText Rcorreo, EditText Rcontrasena, FirebaseAuth auth,  EditText Rusuario, EditText Rtelefono, EditText Rcontrasenarepetida){
        String textCorreo = Rcorreo.getText().toString();
        String textContrasena = Rcontrasena.getText().toString();
        String textUsuario = Rusuario.getText().toString();
        String textTelefono = Rtelefono.getText().toString();
        String textContrasenarepetida = Rcontrasenarepetida.getText().toString();
        if (!textContrasena.isEmpty() && !textCorreo.isEmpty() && !textUsuario.isEmpty() && (textTelefono.length()==10) && !textContrasenarepetida.isEmpty()){
            if (!textContrasena.equals(textContrasenarepetida) ) {
                Toast.makeText(this, "Las contraseñas no coinciden, favor de verificar que las contraseñas sean iguales", Toast.LENGTH_SHORT).show();
                Rcontrasena.setText("");
                Rcontrasenarepetida.setText("");
            } else {
                auth.createUserWithEmailAndPassword(textCorreo, textContrasena)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this, "Error de registro", Toast.LENGTH_LONG).show();
                                Rusuario.setText("");
                                Rtelefono.setText("");
                                Rcorreo.setText("");
                                Rcontrasena.setText("");
                                Rcontrasenarepetida.setText("");
                            }
                        }
                    });
            }
        } else{
            Toast.makeText(this, "Favor de rellenar  los datos solicitados", Toast.LENGTH_SHORT).show();
            if (textUsuario.isEmpty()){
                Rusuario.setError("Este campo es obligatorio");
                Rusuario.requestFocus();
                showKeyboard(Rusuario);
            } else if (textTelefono.isEmpty()) {
                Rtelefono.setError("Este campo es obligatorio");
                Rtelefono.requestFocus();
                showKeyboard(Rtelefono);
            } else if (textCorreo.isEmpty()) {
                Rcorreo.setError("Este campo es obligatorio");
                Rcorreo.requestFocus();
                showKeyboard(Rcorreo);
            } else if (textContrasena.isEmpty()) {
                Rcontrasena.setError("Este campo es obligatorio");
                Rcontrasena.requestFocus();
                showKeyboard(Rcontrasena);
            } else if (textContrasenarepetida.isEmpty()) {
                Rcontrasenarepetida.setError("Este campo es obligatorio");
                Rcontrasenarepetida.requestFocus();
                showKeyboard(Rcontrasenarepetida);
            }
            if (textTelefono.length()<10){
                Rtelefono.setError("El numero de telefono no es valido");
                Rtelefono.requestFocus();
                showKeyboard(Rtelefono);
            }
        }
    }
    public void subrayado(TextView Rsubrayado){
        SpannableString mi_texto = new SpannableString("INGRESAR");
        mi_texto.setSpan(new UnderlineSpan(),0,mi_texto.length(),0);
        Rsubrayado.setText(mi_texto);
    }
    public void mostrarContrasena() {
        cambiarVisibilidadContrasena(Rcontrasena, ShowPassword);
    }
    public void mostrarContrasenaRepetida() {
        cambiarVisibilidadContrasena(Rcontrasenarepetida, showPasswordRepedtida);
    }
    private void cambiarVisibilidadContrasena(EditText editText, ImageButton ojo) {
        int inputType = editText.getInputType();
        if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setSelection(editText.getText().length());
            ojo.setBackgroundResource(R.drawable.ojo_cerrado);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setSelection(editText.getText().length());
            ojo.setBackgroundResource(R.drawable.ojo_abierto);
        }
    }
    private void showKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}