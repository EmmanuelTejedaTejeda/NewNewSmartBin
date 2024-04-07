package com.example.smartbin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
public class agregarContenedor extends Fragment {
    EditText agregarContenedor, agregarDireccion, agregarPeso, agregarEstado, agregarNombre;
    Button botonGuardar;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
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
                if (!textDireccion.isEmpty() && !textPeso.isEmpty() && !textNombre.isEmpty()){
                    Toast.makeText(getContext(), "Guardado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Favor de ingresar todos los datos solicitados", Toast.LENGTH_SHORT).show();
                    if (textNombre.isEmpty()){
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
        return view;
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
    void animacion(){
        // Configurar animación de entrada
        Transition enterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.slide_left);
        setEnterTransition(enterTransition);

        // Configurar animación de salida
        Transition exitTransition = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.slide_right);
        setExitTransition(exitTransition);
    }
}