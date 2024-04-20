package com.example.smartbin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class agregarContenedor extends Fragment {
    EditText agregarDireccion, agregarPeso, agregarNombre;
    Button botonGuardar;
    TextView idAutomatico;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    FirebaseAuth mAuth;
    DatabaseReference agregarContenedorReferencia;

    public agregarContenedor() {
    }

    public static agregarContenedor newInstance(String param1, String param2) {
        agregarContenedor fragment = new agregarContenedor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agregar_contenedor, container, false);
        animacion();
        idAutomatico = view.findViewById(R.id.IdAutomatico);
        //Instanciamos la base de datos
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        //Agregamos el contenedor
        agregarContenedorReferencia = FirebaseDatabase.getInstance().getReference()
                .child("datosUsuariosRegistrados")
                .child(uid)
                .child("Contenedores");
        agregarDireccion = view.findViewById(R.id.agregarDireccion);
        agregarPeso = view.findViewById(R.id.agregarPeso);
        agregarNombre = view.findViewById(R.id.agregarNombre);
        botonGuardar = view.findViewById(R.id.BotonGuardar);
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textDireccion = agregarDireccion.getText().toString();
                String textPeso = agregarPeso.getText().toString();
                String textNombre = agregarNombre.getText().toString();
                String textEstado = "Nuevo";
                String textID = idAutomatico.getText().toString();
                if (!textDireccion.isEmpty() && !textPeso.isEmpty() && !textNombre.isEmpty()) {
                    agregarContenedorReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Map<String, Object> map = new HashMap<>();
                                agregarContenedorReferencia.updateChildren(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                agregarDatosContenedor(agregarContenedorReferencia, textNombre, textDireccion, textPeso, textEstado, textID);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Error al crear el nodo 'Contenedores'", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                agregarDatosContenedor(agregarContenedorReferencia, textNombre, textDireccion, textPeso, textEstado, textID);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Error al verificar el nodo", Toast.LENGTH_SHORT).show();
                        }
                    });
                    agregarDireccion.setText("");
                    agregarNombre.setText("");
                    agregarPeso.setText("");
                    Toast.makeText(getContext(), "Guardado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Favor de ingresar todos los datos solicitados", Toast.LENGTH_SHORT).show();
                    if (textNombre.isEmpty()) {
                        agregarNombre.requestFocus();
                        showKeyboard(agregarNombre);
                    } else if (textDireccion.isEmpty()) {
                        agregarDireccion.requestFocus();
                        showKeyboard(agregarDireccion);
                    } else if (textPeso.isEmpty()) {
                        agregarPeso.requestFocus();
                        showKeyboard(agregarPeso);
                    }
                }
            }
        });
        contarContenedores();
        return view;
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    void animacion() {
        // Configurar animación de entrada
        Transition enterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.slide_left);
        setEnterTransition(enterTransition);

        // Configurar animación de salida
        Transition exitTransition = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.slide_right);
        setExitTransition(exitTransition);
    }

    private void agregarDatosContenedor(DatabaseReference usuarioRef, String nombre, String direccion, String peso, String estado, String Id) {
        Map<String, Object> contenedorMap = new HashMap<>();
        contenedorMap.put("Id", Id);
        contenedorMap.put("Nombre", nombre);
        contenedorMap.put("Direccion", direccion);
        contenedorMap.put("Peso", peso);
        contenedorMap.put("Estado", estado);

        usuarioRef.push().setValue(contenedorMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Contenedor agregado correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al agregar el contenedor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void contarContenedores(){
        agregarContenedorReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long cantidadContenedores = snapshot.getChildrenCount();
                if (cantidadContenedores == 0){
                    idAutomatico.setText("#1");
                }
                else {
                    idAutomatico.setText("#" + (String.valueOf(cantidadContenedores + 1 )));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                idAutomatico.setText("Error,  intentelo nuevamente mas tarde");
                Log.d("Fallo", "Fallo al intentar contabilizar los contenedores");
            }
        });
    }
}