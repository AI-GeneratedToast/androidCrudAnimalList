package com.example.labo2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AnimalDBHelper animalDBHelper;
    ArrayList<Animal> listeAnimaux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        animalDBHelper = new AnimalDBHelper(this);

        // Add animals from file if database is empty
        addAnimalsFromFile();

        listeAnimaux = new ArrayList<>();
        loadAnimalsFromDatabase();

        AnimalAdapter adapter = new AnimalAdapter(this, listeAnimaux);
        RecyclerView recyclerView = findViewById(R.id.idRecViewListe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    private void loadAnimalsFromDatabase() {
        listeAnimaux.clear();
        listeAnimaux.addAll(animalDBHelper.getAllAnimals());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ajouter) {
            showAddForm();
            return true;
        } else if (id == R.id.supprimer) {
            showDeleteForm();
            return true;
        } else if (id == R.id.lister) {
            showSearchByNameForm();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSearchByNameForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choisissez un animal");

        // Get animal names from the database
        List<Animal> animals = animalDBHelper.getAllAnimals();
        ArrayList<String> nomAnimaux = new ArrayList<>();
        animals.forEach(animal -> nomAnimaux.add(animal.getNom()));

        Spinner spinner = new Spinner(MainActivity.this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item, nomAnimaux);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(spinner);

        builder.setPositiveButton("Afficher", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nomAnimalChoix = spinner.getSelectedItem().toString();

                // Query the database to get animals by name
                ArrayList<Animal> animalAffiche = animalDBHelper.getAnimalsByName(nomAnimalChoix);


                // Start ListeActivity and pass the list of animals
                Intent intent = new Intent(MainActivity.this, ListeActivity.class);
                intent.putParcelableArrayListExtra("listeAnimaux", animalAffiche);
                startActivity(intent);
                System.out.println("THIS HERE!"+animalAffiche);
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDeleteForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Supprimer un animal");

        // Inflate the layout for the delete form
        View view = LayoutInflater.from(this).inflate(R.layout.form_animal_sup, null);
        builder.setView(view);

        EditText idAnimalEditText = view.findViewById(R.id.idAnimal);

        builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the animal ID entered by the user
                String idString = idAnimalEditText.getText().toString().trim();
                if (!idString.isEmpty()) {
                    int animalId = Integer.parseInt(idString);
                    animalDBHelper.deleteAnimal(animalId);
                    loadAnimalsFromDatabase();
                    Toast.makeText(MainActivity.this, "Animal supprimé avec succès", Toast.LENGTH_SHORT).show();
                    recreate();  // Refresh activity
                } else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un ID valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter un animal");

        // Inflate the layout for the add form
        View view = LayoutInflater.from(this).inflate(R.layout.form_animal_eng, null);
        builder.setView(view);

        EditText idEditText = view.findViewById(R.id.idAnimal);
        EditText nomEditText = view.findViewById(R.id.nom);
        EditText classeEditText = view.findViewById(R.id.classe);
        EditText longeviteEditText = view.findViewById(R.id.longevite);
        EditText habitatEditText = view.findViewById(R.id.habitat);
        EditText faitEditText = view.findViewById(R.id.fait);

        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the values entered by the user
                int id = Integer.parseInt(idEditText.getText().toString().trim());
                String nom = nomEditText.getText().toString().trim();
                String classe = classeEditText.getText().toString().trim();
                int longevite = Integer.parseInt(longeviteEditText.getText().toString().trim());
                String habitat = habitatEditText.getText().toString().trim();
                String fait = faitEditText.getText().toString().trim();

                // Create a new Animal object
                Animal newAnimal = new Animal(id, nom, classe, longevite, habitat, fait);

                // Insert the new animal into the database
                animalDBHelper.addAnimal(newAnimal);
                loadAnimalsFromDatabase();
                Toast.makeText(MainActivity.this, "Animal ajouté avec succès", Toast.LENGTH_SHORT).show();
                recreate();  // Refresh activity
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void addAnimalsFromFile() {
        if (animalDBHelper.getRowCount() == 0) {
            try {
                // Read the contents of the text file
                InputStream inputStream = getResources().openRawResource(R.raw.animaux);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    // Split the line by the semicolon (;) delimiter
                    String[] parts = line.split(";");

                    // Parse the values and create an Animal object
                    int id = Integer.parseInt(parts[0]);
                    String nom = parts[1];
                    String classe = parts[2];
                    int longevite = Integer.parseInt(parts[3]);
                    String habitat = parts[4];
                    String fait = parts[5];

                    Animal animal = new Animal(id, nom, classe, longevite, habitat, fait);

                    // Insert the Animal object into the database
                    animalDBHelper.addAnimal(animal);
                }

                // Close the streams
                bufferedReader.close();
                inputStream.close();

                // Notify the user that animals have been added to the database
                Toast.makeText(this, "Animals added to database from file", Toast.LENGTH_SHORT).show();
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error adding animals from file", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
