package com.example.imt_atlantique.tp1final;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class  User implements Parcelable {
    String nomUser, prenomUser, dateUser, villeUser,departUser;
    ArrayList <String> numero;

    public User(String nom, String prenom, String dateNais, String villeNais,String departement, ArrayList <String> numero) {
        super();
        this.nomUser = nom;
        this.prenomUser = prenom;
        this.dateUser = dateNais;
        this.villeUser = villeNais;
        this.departUser=departement;
        this.numero = numero;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(nomUser);
        out.writeString(prenomUser);
        out.writeString(dateUser);
        out.writeString(villeUser);
        out.writeString(departUser);
        out.writeArray(new ArrayList[]{numero});
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel in) {
        nomUser = in.readString();
        prenomUser = in.readString();
        dateUser = in.readString();
        villeUser = in.readString();
        departUser= in.readString();
        numero = in.readArrayList(String.class.getClassLoader());
    }
}
