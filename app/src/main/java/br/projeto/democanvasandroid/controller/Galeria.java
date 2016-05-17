package br.projeto.democanvasandroid.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.model.Imagem;

public class Galeria extends AppCompatActivity {

    private Firebase url = new Firebase("https://appjoelho.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        listaImagens();
    }

    public void listaImagens() {
        url.child("Imagens").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("Existem " + dataSnapshot.getChildrenCount() + " imagens");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Imagem img = postSnapshot.getValue(Imagem.class);
                    img.setId(postSnapshot.getKey());

                    System.out.println("Imagem - ID: " + img.getId() + " - IMAGE: " + img.getImagem());
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Erro no banco", firebaseError.getMessage());
            }
        });
    }
}