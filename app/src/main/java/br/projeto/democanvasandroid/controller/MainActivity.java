package br.projeto.democanvasandroid.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import br.projeto.democanvasandroid.R;

public class MainActivity extends AppCompatActivity {

    private TelaView tela;
    private Button btSalva;
    private Switch btZoom;

    private Intent intent;
    private String imagem64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        imagem64 = intent.getExtras().getString("imagem");

        Bitmap imagem = Base64ParaBitmap(imagem64);

        tela = (TelaView) findViewById(R.id.telaView);
        tela.setImg(new BitmapDrawable(getResources(), imagem));

        btSalva = (Button) findViewById(R.id.btSalva);

        btSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tela.salvaListaCirculo(tela.getCirculo().getX(), tela.getCirculo().getY());
            }
        });

        btZoom = (Switch) findViewById(R.id.btZoom);

        btZoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tela.setZoom(true);
                } else {
                    tela.setZoom(false);
                }
            }
        });

    }


    public static Bitmap Base64ParaBitmap(String imagemB64){
        //decodifica base64 para byte
        byte[] imgByte = Base64.decode(imagemB64, Base64.DEFAULT);
        //decodifica byte para Bitmap
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }
}