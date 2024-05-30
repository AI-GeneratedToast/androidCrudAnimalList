package com.example.labo2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListeActivity extends AppCompatActivity {
    private ArrayList<Animal> listeAnimaux;
    private Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        Toolbar toolbar = findViewById(R.id.toolbarliste);
        setSupportActionBar(toolbar);
        recupererDonnees();
        afficheDonnees();
    }

    public void recupererDonnees() {
        it = getIntent();
        listeAnimaux = it.getParcelableArrayListExtra("listeAnimaux");
    }

    public void afficheDonnees() {
        RecyclerView rv = findViewById(R.id.idRecViewListe);
        AnimalAdapter adp = new AnimalAdapter(this, listeAnimaux);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(rv.getContext(),
                DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(divider);
        rv.setAdapter(adp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_liste, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.retour) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
