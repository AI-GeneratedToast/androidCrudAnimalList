package com.example.labo2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Animal extends ArrayList<Parcelable> implements Parcelable{
    private int id;
    private String nom;
    private String classe;
    private int longevite;
    private String habitat;
    private String fait;

    //CONSTRUCTEURS
    public Animal(){}

    public Animal(int id, String nom, String classe, int longevite, String habitat, String fait) {
        this.id = id;
        this.nom = nom;
        this.classe = classe;
        this.longevite = longevite;
        this.habitat = habitat;
        this.fait = fait;
    }

    //GETTERS
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getClasse() {
        return classe;
    }

    public int getLongevite() {
        return longevite;
    }

    public String getHabitat() {
        return habitat;
    }

    public String getFait() {
        return fait;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nom);
        dest.writeString(classe);
        dest.writeInt(longevite);
        dest.writeString(habitat);
        dest.writeString(fait);
    }

    public static final Parcelable.Creator<Animal>
            CREATOR = new Creator<Animal>() {
        @NonNull
        public Animal createFromParcel(@NonNull Parcel source) {
            Animal animal = new Animal();
            animal.id = source.readInt();
            animal.nom = source.readString();
            animal.classe = source.readString();
            animal.longevite = source.readInt();
            animal.habitat = source.readString();
            animal.fait = source.readString();
            return animal;
        }

        @Override
        public Animal[] newArray(int size) {return new Animal[size];}
    };

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", classe='" + classe + '\'' +
                ", longevite=" + longevite +
                ", habitat='" + habitat + '\'' +
                ", fait='" + fait + '\'' +
                '}';
    }
}

