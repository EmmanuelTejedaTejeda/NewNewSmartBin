package com.example.smartbin;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class contenedores extends Fragment {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    LinearLayout sinConexion;
    LinearLayoutCompat noHayConcidencias, no_existen_contenedores;
    SwipeRefreshLayout refreshLayout;
    EditText buscarDentro;
    FirebaseAuth mAuth;
    DatabaseReference contenedorReferencia;
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
        no_existen_contenedores = view.findViewById(R.id.no_existen_contenedores);
        //instanciamos la base de datos
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        contenedorReferencia = FirebaseDatabase.getInstance().getReference().child("datosUsuariosRegistrados")
                .child(uid)
                .child("Contenedores");
        verificarConexion(view, uid);
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verificarConexion(view, uid);
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
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("datosUsuariosRegistrados")
                                    .child(uid)
                                    .child("Contenedores"),MainModel.class)
                            .build();
                    mainAdapter = new MainAdapter(options);
                    mainAdapter.startListening();
                    recyclerView.setAdapter(mainAdapter);
                    noHayConcidencias.setVisibility(View.GONE);
                } else {
                    FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child( "datosUsuariosRegistrados")
                                    .child(uid)
                                    .child("Contenedores")
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

    void verificarConexion(View view, String uid){
        sinConexion = view.findViewById(R.id.sin_conexion);
        sinConexion.setVisibility(View.GONE);
        ConnectivityManager con = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = con.getActiveNetworkInfo();
        if (info!= null && info.isConnected()){
            sinConexion.setVisibility(View.GONE);
            mostrarContenedores(view, uid);
            mainAdapter.startListening();
        } else {
            mainAdapter.stopListening();
            sinConexion.setVisibility(View.VISIBLE);
            no_existen_contenedores.setVisibility(View.GONE);

        }
    }
    void mostrarContenedores(View view, String uid){
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("datosUsuariosRegistrados")
                        .child(uid)
                        .child("Contenedores"),MainModel.class)
                .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        contenedorReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long cantidad = snapshot.getChildrenCount();
                if (cantidad==0){
                    no_existen_contenedores.setVisibility(View.VISIBLE);
                }
                else {
                    no_existen_contenedores.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}