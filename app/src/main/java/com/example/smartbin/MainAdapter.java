package com.example.smartbin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.Id.setText(model.getId());
        holder.Direccion.setText(model.getDireccion());
        holder.Estado.setText(model.getEstado());
        holder.Nombre.setText(model.getNombre());
        ImageView alertaImagen = holder.itemView.findViewById(R.id.alerta);
        LinearLayoutCompat contenedor = holder.itemView.findViewById(R.id.contenedor);
        if (model.getEstado().equals("Necesita mantenimiento")){
            holder.Estado.setBackgroundResource(R.color.alerta);
            int colorBlack = holder.itemView.getContext().getResources().getColor(R.color.black);
            holder.Estado.setTextColor(colorBlack);
            alertaImagen.setVisibility(View.VISIBLE);
            contenedor.setBackgroundResource(R.color.alerta);
        }
        holder.Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(holder.Id.getContext())
                        .setContentHolder(new ViewHolder(R.layout.updates_contenedores))
                        .setExpanded(true, 1400)
                        .create();
                View  view = dialogPlus.getHolderView();
                EditText direccionEditable = view.findViewById(R.id.Edit_Localizacion);
                EditText capacidadEditable = view.findViewById(R.id.Edit_Capacidad);
                EditText estadoEditable = view.findViewById(R.id.Edit_Estado);

                Button GuardarCambios = view.findViewById(R.id.Guardar_boton);
                Button CancelarCambios = view.findViewById(R.id.Cancelar_boton);

                direccionEditable.setText(model.getDireccion());
                capacidadEditable.setText(model.getPeso());
                estadoEditable.setText(model.getEstado());

                GuardarCambios.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("Direccion", direccionEditable.getText().toString());
                        map.put("Peso", capacidadEditable.getText().toString());
                        map.put("Estado", estadoEditable.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("Contenedores")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.Direccion.getContext(), "Datos actualizacos exitosamente", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.Direccion.getContext(), "Error al actualizar los  datos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                CancelarCambios.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPlus.dismiss();
                    }
                });

                dialogPlus.show();
            }
        });
        holder.Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                String uid = currentUser.getUid();
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.Direccion.getContext());
                builder.setTitle("¿Esta seguro que quiere eliminar este contenedor?");
                builder.setMessage("Al elimiar este contenedor no  habra forma  de recuperarlo");
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("datosUsuariosRegistrados")
                                .child(uid)
                                .child("Contenedores")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.Direccion.getContext(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainitem,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView Id, Direccion, Estado, Nombre;
        Button Eliminar, Editar;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Id = itemView.findViewById(R.id.Id);
            Direccion = itemView.findViewById(R.id.Direccion);
            Estado = itemView.findViewById(R.id.Estado);
            Nombre = itemView.findViewById(R.id.Nombre);
            Eliminar = (Button) itemView.findViewById(R.id.deleteThisContainer);
            Editar = (Button) itemView.findViewById(R.id.editThisContainer);
        }
    }
}
