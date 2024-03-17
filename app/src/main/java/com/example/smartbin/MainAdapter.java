package com.example.smartbin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.Id.setText(model.getId());
        holder.Direccion.setText(model.getDireccion());
        holder.Estado.setText(model.getEstado());
        ImageView alertaImagen = holder.itemView.findViewById(R.id.alerta);
        LinearLayoutCompat contenedor = holder.itemView.findViewById(R.id.contenedor);
        if (model.getEstado().equals("Necesita mantenimiento")){
            holder.Estado.setBackgroundResource(R.color.alerta);
            int colorBlack = holder.itemView.getContext().getResources().getColor(R.color.black);
            holder.Estado.setTextColor(colorBlack);
            alertaImagen.setVisibility(View.VISIBLE);
            contenedor.setBackgroundResource(R.color.alerta);
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainitem,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView Id, Direccion, Estado;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Id = itemView.findViewById(R.id.Id);
            Direccion = itemView.findViewById(R.id.Direccion);
            Estado = itemView.findViewById(R.id.Estado);
        }
    }
}
