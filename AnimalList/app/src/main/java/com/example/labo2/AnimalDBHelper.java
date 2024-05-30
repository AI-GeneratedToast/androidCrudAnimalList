package com.example.labo2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.labo2.Animal;

import java.util.ArrayList;
import java.util.List;

public class AnimalDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "animal_database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "animal_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_CLASSE = "classe";
    private static final String COLUMN_LONGEVITE = "longevite";
    private static final String COLUMN_HABITAT = "habitat";
    private static final String COLUMN_FAIT = "fait";

    public AnimalDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOM + " TEXT, " +
                COLUMN_CLASSE + " TEXT, " +
                COLUMN_LONGEVITE + " INTEGER, " +
                COLUMN_HABITAT + " TEXT, " +
                COLUMN_FAIT + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addAnimal(Animal animal) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOM, animal.getNom());
        cv.put(COLUMN_CLASSE, animal.getClasse());
        cv.put(COLUMN_LONGEVITE, animal.getLongevite());
        cv.put(COLUMN_HABITAT, animal.getHabitat());
        cv.put(COLUMN_FAIT, animal.getFait());

        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result != -1;
    }

    public List<Animal> getAllAnimals() {
        List<Animal> animalList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String nom = cursor.getString(cursor.getColumnIndex(COLUMN_NOM));
                @SuppressLint("Range") String classe = cursor.getString(cursor.getColumnIndex(COLUMN_CLASSE));
                @SuppressLint("Range") int longevite = cursor.getInt(cursor.getColumnIndex(COLUMN_LONGEVITE));
                @SuppressLint("Range") String habitat = cursor.getString(cursor.getColumnIndex(COLUMN_HABITAT));
                @SuppressLint("Range") String fait = cursor.getString(cursor.getColumnIndex(COLUMN_FAIT));

                Animal animal = new Animal(id, nom, classe, longevite, habitat, fait);
                animalList.add(animal);
            }
            cursor.close();
        }

        db.close();
        return animalList;
    }

    public boolean deleteAnimal(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public ArrayList<Animal> getAnimalsByName(String nomAnimalChoix) {
        ArrayList<Animal> animals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selection = COLUMN_NOM + " = ?";
        String[] selectionArgs = {nomAnimalChoix};

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String nom = cursor.getString(cursor.getColumnIndex(COLUMN_NOM));
                @SuppressLint("Range") String classe = cursor.getString(cursor.getColumnIndex(COLUMN_CLASSE));
                @SuppressLint("Range") int longevite = cursor.getInt(cursor.getColumnIndex(COLUMN_LONGEVITE));
                @SuppressLint("Range") String habitat = cursor.getString(cursor.getColumnIndex(COLUMN_HABITAT));
                @SuppressLint("Range") String fait = cursor.getString(cursor.getColumnIndex(COLUMN_FAIT));

                Animal animal = new Animal(id, nom, classe, longevite, habitat, fait);
                animals.add(animal);
            }
            cursor.close();
        }

        db.close();
        return animals;
    }

    public int getRowCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return count;
    }

}
