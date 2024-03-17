package com.example.smartbin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link agregarContenedor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class agregarContenedor extends Fragment {
    EditText agregarContenedor, agregarDireccion, agregarPeso, agregarEstado;
    Button botonGuardar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public agregarContenedor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment agregarContenedor.
     */
    // TODO: Rename and change types and number of parameters
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
        agregarDireccion = view.findViewById(R.id.agregarDireccion);
        agregarPeso = view.findViewById(R.id.agregarPeso);
        botonGuardar = view.findViewById(R.id.BotonGuardar);
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textDireccion = agregarDireccion.getText().toString();
                String textPeso = agregarPeso.getText().toString();
                if (!textDireccion.isEmpty() && !textPeso.isEmpty()){
                    Toast.makeText(getContext(), "Guardado", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), "Favor de ingresar todos los datos solicitados", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}