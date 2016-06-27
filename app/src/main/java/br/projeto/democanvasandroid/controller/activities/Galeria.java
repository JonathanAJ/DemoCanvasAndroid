package br.projeto.democanvasandroid.controller.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.controller.GaleriaAdapter;
import br.projeto.democanvasandroid.controller.activities.MainActivity;
import br.projeto.democanvasandroid.model.Imagem;

public class Galeria extends AppCompatActivity {

    private DatabaseReference url = FirebaseDatabase.getInstance().getReference();

    private ViewPager galeria;
    private GaleriaAdapter galeriaAdapter;
    private Button btSelecionaImg;
    private List<Imagem> imgList = new ArrayList<>();
    private LinearLayout layoutLoad;
    private LinearLayout layoutPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        galeria = (ViewPager) findViewById(R.id.galeria);
        galeriaAdapter =  new GaleriaAdapter(this, imgList);
        galeria.setAdapter(galeriaAdapter);
        layoutLoad = (LinearLayout) findViewById(R.id.layoutLoad);
        layoutPrincipal = (LinearLayout) findViewById(R.id.layoutPrincipal);
        btSelecionaImg = (Button) findViewById(R.id.bt_seleciona_img);

        iniciaListeners();
        listaImagens();
    }

    public void listaImagens() {
        url.child("Imagens").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imgList.clear();
                layoutLoad.setVisibility(View.GONE);
                layoutPrincipal.setVisibility(View.VISIBLE);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Imagem img = postSnapshot.getValue(Imagem.class);
                    img.setId(postSnapshot.getKey());

                    imgList.add(img);

                    System.out.println("Imagem - ID: " + img.getId() + " - IMAGE: " + img.getImagem());
                }

                galeriaAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("Erro no banco", firebaseError.getMessage());
            }
        });
    }

    public void iniciaListeners(){

        btSelecionaImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int posicao = galeria.getCurrentItem();
                String idImg = imgList.get(posicao).getId();

                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                intentMain.putExtra("idImg", idImg);
                startActivity(intentMain);

            }
        });

    }
}