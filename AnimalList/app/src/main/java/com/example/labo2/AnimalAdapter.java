package com.example.labo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    private Context context;
    private ArrayList<Animal> listeAnimaux;

    public AnimalAdapter(Context context, ArrayList<Animal> listeAnimaux) {
        this.context = context;
        this.listeAnimaux = listeAnimaux;

    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Animal animal = listeAnimaux.get(position);
        String id = animal.getId()+"";
        String longevite = animal.getLongevite()+"";
        holder.txtViewId.setText("ID: "+id);
        holder.txtViewNom.setText("Nom: "+animal.getNom());
        holder.txtViewClasse.setText("Classe: "+animal.getClasse());
        holder.txtViewLongevite.setText("Longevite: "+longevite);
        holder.txtViewHabitat.setText("Habitat: "+animal.getHabitat());
        holder.txtViewFait.setText("Fait: "+animal.getFait());
    }

    @Override
    public int getItemCount() {
        return listeAnimaux.size();
    }
    public static class AnimalViewHolder extends RecyclerView.ViewHolder{

        TextView txtViewId,txtViewNom,txtViewClasse,txtViewLongevite,txtViewHabitat,txtViewFait;

        public AnimalViewHolder(@NonNull View itemView){
            super(itemView);
            txtViewId = itemView.findViewById(R.id.idAnimal);
            txtViewNom = itemView.findViewById(R.id.idNom);
            txtViewClasse = itemView.findViewById(R.id.idClasse);
            txtViewLongevite = itemView.findViewById(R.id.idLongevite);
            txtViewHabitat = itemView.findViewById(R.id.idHabitat);
            txtViewFait = itemView.findViewById(R.id.idFait);
        }
    }
}
