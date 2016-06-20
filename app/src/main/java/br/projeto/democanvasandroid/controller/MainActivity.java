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
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.model.Reta;

public class MainActivity extends AppCompatActivity {

    private TelaView tela;
    private Button btSalva;
    private Switch btZoom;
    private NumberPicker valorMili;

    private int numProcesso = 1;

    private boolean pickerNegative = true;
    private final int minValorMm = -30;
    private final int maxValorMm = 30;
    private int atualValorMm = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tela = (TelaView) findViewById(R.id.telaView);
        tela.setImg(getImagem(getIntent()));
        btSalva = (Button) findViewById(R.id.btSalva);
        btZoom = (Switch) findViewById(R.id.btZoom);
        valorMili = (NumberPicker) findViewById(R.id.valorMili);
        formatPickerNegative();

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
                numProcesso = proximoNumeroProcesso(numProcesso);
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
                if (pickerNegative) {
                    atualValorMm = newVal + minValorMm;
                    tela.setListRetaY(atualValorMm);
                    System.out.println("Valor em mm " + atualValorMm);
                    tela.invalidate();
                }
                else {
                    if (numProcesso < 6){
                        tela.setListRetaY(-newVal);
                        System.out.println("Valor em mm " + (-newVal));
                    }
                    else {
                        tela.setListRetaY(newVal);
                        System.out.println("Valor em mm " + newVal);
                    }
                    tela.invalidate();
                }
            }
        });

    }

    /**
     *
     * Método que gerencia em qual estado o processo todo se encontra.
     * Ele é definido a cada click do botão salvar.
     */
    public int proximoNumeroProcesso(int numProcesso){
        /**
         * Gera os primeiros 2 pontos
         */
        if (numProcesso < 3) {
            tela.salvaListaCirculo(tela.getCirculo().getX(), tela.getCirculo().getY());
        }
        /**
         * Gera o 3º, a perpendicular e a visibilidade do Picker
         */
        else if (numProcesso == 3) {
            tela.salvaListaCirculo(tela.getCirculo().getX(), tela.getCirculo().getY());
            tela.salvaPerpendicular(tela.getListCirc().get(1), tela.getListCirc().get(2));
            valorMili.setVisibility(View.VISIBLE);
        }
        /**
         * Informa que o número do Picker foi salvo.
         */
        else if (numProcesso == 4) {
            Toast.makeText(getApplicationContext(), "Tamanho salvo com sucesso!", Toast.LENGTH_SHORT).show();
            tela.salvaReta(tela.getListReta().get(0), 0);
            formatPickerNoNegative();
            pickerNegative = false;
            tela.invalidate();
        }
        else if (numProcesso == 5) {
            valorMili.setVisibility(View.GONE);
            formatPickerNegative();
            pickerNegative = true;
        }
        else if (numProcesso == 6) {
            tela.salvaListaCirculo(tela.getCirculo().getX(), tela.getCirculo().getY());
        }
        else if (numProcesso == 7) {
            tela.salvaListaCirculo(tela.getCirculo().getX(), tela.getCirculo().getY());
            tela.salvaPerpendicular(tela.getListCirc().get(4), tela.getListCirc().get(3));
            valorMili.setVisibility(View.VISIBLE);
        }
        else if (numProcesso == 8) {
            Toast.makeText(getApplicationContext(), "Tamanho salvo com sucesso!", Toast.LENGTH_SHORT).show();
            tela.salvaReta(tela.getListReta().get(2), 0);
            formatPickerNoNegative();
            pickerNegative = false;
            tela.invalidate();
        }

        return numProcesso + 1;
    }

    public void formatPickerNegative(){
        /**
         * Método encontrado para formatar o picker e adicionar
         * valores negativos ao mesmo.
         */
        valorMili.setMinValue(0);
        valorMili.setMaxValue(maxValorMm - minValorMm);
        valorMili.setValue(atualValorMm - minValorMm);
        valorMili.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int index) {
                if(index == maxValorMm){
                    return "0";
                }else {
                    return index + minValorMm + "mm";
                }
            }
        });
    }

    public void formatPickerNoNegative(){
        /**
         * Método encontrado para formatar o picker.
         */
        valorMili.setMinValue(0);
        valorMili.setMaxValue(30);
        valorMili.setValue(0);
        valorMili.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int index) {
                return index + "mm";
            }
        });
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