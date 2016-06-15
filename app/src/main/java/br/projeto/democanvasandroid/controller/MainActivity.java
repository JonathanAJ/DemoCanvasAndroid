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
import android.widget.LinearLayout;
import android.widget.Switch;

import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.model.Circulo;

public class MainActivity extends AppCompatActivity {

    private TelaView tela;
    private Button btSalva;
    private Switch btZoom;
    private NumberPicker valorMili;
    private LinearLayout layoutBtSalva;
    private LinearLayout layoutBarra;

    private int sizeListCirc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tela = (TelaView) findViewById(R.id.telaView);
        tela.setImg(getImagem(getIntent()));
        btSalva = (Button) findViewById(R.id.btSalva);
        btZoom = (Switch) findViewById(R.id.btZoom);
        valorMili = (NumberPicker) findViewById(R.id.valorMili);
        layoutBtSalva = (LinearLayout) findViewById(R.id.layoutBtSalva);
        layoutBarra = (LinearLayout) findViewById(R.id.layoutBarra);

        iniciaListeners();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void iniciaListeners(){

        btSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tela.salvaListaCirculo(tela.getCirculo().getX(), tela.getCirculo().getY());
                sizeListCirc = tela.getSizeListCirc();
                verificaTamanhoDaLista(sizeListCirc);
            }
        });

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

        valorMili.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
                System.out.println("Valor em mm " + newVal);
                tela.setListRetaY(newVal);
                tela.invalidate();
            }
        });

    }

    public void verificaTamanhoDaLista(int size){
        switch(size){
            /**
             * Caso chegue no 3º círculo criado, set para visível
             * o input e mude o salvamento do botão para salvá-lo.
             */
            case 3 : {
                valorMili.setVisibility(View.VISIBLE);
                layoutBtSalva.setVisibility(View.GONE);
                layoutBarra.setWeightSum(2);
                break;
            }
            case 5 : {
                System.out.println("chegou no 5º");
                break;
            }
        }
    }

    public BitmapDrawable getImagem(Intent intent){
        String imagem64 = intent.getExtras().getString("imagem");
        Bitmap imgBitmap = Base64ParaBitmap(imagem64);
        // transforma o Bitmap em BitmapDrawable
        return new BitmapDrawable(getResources(), imgBitmap);
    }


    public static Bitmap Base64ParaBitmap(String imagemB64){
        //decodifica base64 para byte
        byte[] imgByte = Base64.decode(imagemB64, Base64.DEFAULT);
        //decodifica byte para Bitmap
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }
}