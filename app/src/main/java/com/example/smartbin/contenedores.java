package com.example.smartbin;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class contenedores extends Fragment {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    LinearLayout sinConexion;
    SwipeRefreshLayout refreshLayout;
    EditText buscarDentro;
    ImageButton buscador;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public contenedores() {
    }
    public static contenedores newInstance(String param1, String param2) {
        contenedores fragment = new contenedores();
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
        View view = inflater.inflate(R.layout.fragment_contenedores, container, false);
        verificarConexion(view);
        buscador = view.findViewById(R.id.buscador);
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verificarConexion(view);
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000); // 2000 milisegundos = 2 segundos
            }
        });

        buscarDentro = view.findViewById(R.id.buscarDentro);
        buscarDentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarDentro.setSelection(buscarDentro.getText().length());
                buscador.setBackground(getContext().getDrawable(R.drawable.cancelar_icon));
            }
        });
        buscador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarDentro.setText("");
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                buscador.setBackground(getContext().getDrawable(R.drawable.lupa_buscador));
                FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Contenedores"), MainModel.class)
                        .build();
                mainAdapter = new MainAdapter(options);
                mainAdapter.startListening();
                recyclerView.setAdapter(mainAdapter);
            }
        });
        buscarDentro.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = buscarDentro.getText().toString().trim();
                    if (searchText.isEmpty()) {
                        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Contenedores"), MainModel.class)
                                .build();
                        mainAdapter = new MainAdapter(options);
                        mainAdapter.startListening();
                        recyclerView.setAdapter(mainAdapter);
                    } else {
                        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Contenedores")
                                                .orderByChild("Direccion")
                                                .startAt(searchText)
                                                .endAt(searchText + "\uf8ff"),
                                        MainModel.class)
                                .build();
                        mainAdapter = new MainAdapter(options);
                        mainAdapter.startListening();
                        recyclerView.setAdapter(mainAdapter);

                    }
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    buscarDentro.clearFocus();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    void verificarConexion(View view){
        sinConexion = view.findViewById(R.id.sin_conexion);
        sinConexion.setVisibility(View.INVISIBLE);
        ConnectivityManager con = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = con.getActiveNetworkInfo();
        if (info!= null && info.isConnected()){
            sinConexion.setVisibility(View.INVISIBLE);
            mostrarContenedores(view);
            mainAdapter.startListening();
        } else {
            mainAdapter.stopListening();
            sinConexion.setVisibility(View.VISIBLE);
        }
    }
    void mostrarContenedores(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Contenedores"),MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }
}