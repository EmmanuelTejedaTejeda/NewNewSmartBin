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

import android.text.Editable;
import android.text.TextWatcher;
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
    LinearLayoutCompat noHayConcidencias;
    SwipeRefreshLayout refreshLayout;
    EditText buscarDentro;
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
        noHayConcidencias = view.findViewById(R.id.No_hay_concidencias);
        verificarConexion(view);
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
        buscarDentro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().trim();
                if (searchText.isEmpty()) {
                    FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Contenedores"), MainModel.class)
                            .build();
                    mainAdapter = new MainAdapter(options);
                    mainAdapter.startListening();
                    recyclerView.setAdapter(mainAdapter);
                    noHayConcidencias.setVisibility(View.GONE);
                } else {
                    FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Contenedores")
                                            .orderByChild("Direccion")
                                            .startAt(searchText)
                                            .endAt(searchText + "\uf8ff"),
                                    MainModel.class)
                            .build();
                    if (mainAdapter.getItemCount() == 0) {
                        noHayConcidencias.setVisibility(View.VISIBLE);
                    } else {
                        noHayConcidencias.setVisibility(View.GONE);
                    }
                    mainAdapter = new MainAdapter(options);
                    mainAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            if (mainAdapter.getItemCount() == 0) {
                                noHayConcidencias.setVisibility(View.VISIBLE);
                            } else {
                                noHayConcidencias.setVisibility(View.GONE);
                            }
                        }
                    });
                    mainAdapter.startListening();
                    recyclerView.setAdapter(mainAdapter);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

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
        sinConexion.setVisibility(View.GONE);
        ConnectivityManager con = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = con.getActiveNetworkInfo();
        if (info!= null && info.isConnected()){
            sinConexion.setVisibility(View.GONE);
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