package br.projeto.democanvasandroid.controller;

import com.google.firebase.database.FirebaseDatabase;

public class Aplicacao extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}