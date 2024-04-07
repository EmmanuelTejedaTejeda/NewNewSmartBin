package com.example.smartbin;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
    TextView registrarse;
    Button ingresar;
    EditText correo, contrasena;
    ImageButton showPassword;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    int RC_SING_IN = 1;
    Button registerWithGoogle, registerWithFacebook;
    String TAG = "GoogleSignIn";

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        registerWithGoogle = findViewById(R.id.registerWithGoogle);
        registerWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        registerWithFacebook = findViewById(R.id.registerWithFacebook);
        registerWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SING_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "FirabaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Log.w(TAG, "Inicio de sesion con google fallido", e);
                }
            } else {
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Log.d(TAG, "Error, incio de sesion fallido" + task.getException().toString());
                Toast.makeText(this, "Ocurrio un error"+ task.getException().toString(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void signIn(){
        Intent signInIntent = new Intent(mGoogleSignInClient.getSignInIntent());
        startActivityForResult(signInIntent, RC_SING_IN);
    }

    private void subrayado(){
        registrarse = (TextView) findViewById(R.id.registrarse);
        SpannableString miTexto = new SpannableString("REGISTRARSE");
        miTexto.setSpan(new UnderlineSpan(),0,miTexto.length(),0);
        registrarse.setText(miTexto);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "SignInWithCredential:success");
                    Intent intent = new Intent(Login.this, ContainerFragment.class);
                    startActivity(intent);
                    Toast.makeText(Login.this, "Ingreso exitoso", Toast.LENGTH_SHORT).show();
                    Login.this.finish();
                }
                else{
                    Log.w(TAG, "SignInWithCredential:failure", task.getException());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}