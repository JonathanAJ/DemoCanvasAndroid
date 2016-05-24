package br.projeto.democanvasandroid.controller;

<<<<<<< HEAD
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
=======
>>>>>>> 1ae24ee1ba5df17263f5bb83d43bd09c52648664
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.model.Imagem;

public class Galeria extends AppCompatActivity {

    private DatabaseReference url = FirebaseDatabase.getInstance().getReference();

    //Header do base64 > data:image/png;base64,
    private final int HEADER = 22;
    private ViewPager galeria;
    private GaleriaAdapter galeriaAdapter;
    private Button btSelecionaImg;
    private List<Bitmap> bitmapList = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

<<<<<<< HEAD
        galeria = (ViewPager) findViewById(R.id.galeria);
        galeriaAdapter =  new GaleriaAdapter(this, bitmapList);

        galeria.setAdapter(galeriaAdapter);

        btSelecionaImg = (Button) findViewById(R.id.bt_seleciona_img);
        btSelecionaImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int posicao = galeria.getCurrentItem();

                System.out.println(" ITEM >>> " + posicao);

                byte[] imgByte = converteBitmapParaByte(bitmapList.get(posicao));
                String imgBase64 = Base64.encodeToString(imgByte, Base64.NO_WRAP);

                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                intentMain.putExtra("imagem", imgBase64);
                startActivity(intentMain);

            }
        });
=======
        ViewPager galeria = (ViewPager) findViewById(R.id.galeria);
        GaleriaPage adapter = new GaleriaPage(this);
        galeria.setAdapter(adapter);
>>>>>>> 1ae24ee1ba5df17263f5bb83d43bd09c52648664

        listaImagens();
    }



/*    btSelecionaImg.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int posicao = galeria.getCurrentItem();

            System.out.println(" ITEM >>> " + posicao);

                *//*Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                intentMain.putExtra("imagem", bitmapList.get(posicao));
                startActivity(intentMain);*//*

        }
    });*/

    public void listaImagens() {
        url.child("Imagens").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bitmapList.clear();

                System.out.println("Existem " + dataSnapshot.getChildrenCount() + " imagens");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Imagem img = postSnapshot.getValue(Imagem.class);
                    img.setId(postSnapshot.getKey());

                    String imagem = img.getImagem().substring(HEADER, img.getImagem().length());
                    Bitmap bitmap = Base64ParaBitmap(imagem);

                    bitmapList.add(bitmap);

                    System.out.println("Imagem - ID: " + img.getId() + " - IMAGE: " + imagem);
                }

                galeriaAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("Erro no banco", firebaseError.getMessage());
            }
        });
    }

    public static Bitmap Base64ParaBitmap(String imagemB64){
        //decodifica base64 para byte
        byte[] imgByte = Base64.decode(imagemB64, Base64.DEFAULT);
        //decodifica byte para Bitmap
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

    public byte[] converteBitmapParaByte(Bitmap img){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}