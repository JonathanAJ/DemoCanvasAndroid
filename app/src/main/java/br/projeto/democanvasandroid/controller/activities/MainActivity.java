package br.projeto.democanvasandroid.controller.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shawnlin.numberpicker.NumberPicker;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.controller.TelaView;
import br.projeto.democanvasandroid.model.Circulo;
import br.projeto.democanvasandroid.model.Imagem;
import br.projeto.democanvasandroid.model.Reta;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference url = FirebaseDatabase.getInstance().getReference();

    //Header do base64 > data:image/png;base64,
    private final int HEADER = 22;
    private TelaView tela;
    private Button btSalva;
    private Button btRetorna;
    private Switch btZoom;
    private NumberPicker valorMili;
    private String imgBase64;
    private Bitmap imgBitmap;

    private int numProcesso = 1;

    private boolean pickerNegative = true;
    private final int minValorMm = -30;
    private final int maxValorMm = 30;
    private int atualValorMm = 0;

    private int angulo1;
    private int angulo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tela = (TelaView) findViewById(R.id.telaView);
        String idImg = getIntent().getExtras().getString("idImg");

        btSalva = (Button) findViewById(R.id.btSalva);
        btRetorna = (Button) findViewById(R.id.btRetorna);
        btZoom = (Switch) findViewById(R.id.btZoom);
        valorMili = (NumberPicker) findViewById(R.id.valorMili);
        formatPickerNegative();

        iniciaListeners();
        getImagem(idImg);
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

        btRetorna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                numProcesso = anteriorNumeroProcesso(numProcesso);
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
     * Método que gerencia em qual estado o processo se encontra.
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
            /**
             * Calcula e retorna o ângulo entre as retas geradas
             */
            int angulo = (int) tela.getAnguloEntreRetas(tela.getListCirc().get(0), tela.getListCirc().get(2),
                    tela.getListCirc().get(1), tela.getListCirc().get(2));
            System.out.println("Ângulo entre alinhamento mecânico e anatômico: " + angulo);
            angulo1 = angulo;
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
        else if (numProcesso == 9) {
            // última reta criada para fechar lógica de visibilidade
            tela.salvaReta(new Reta(0,0,0,0), 0);
            valorMili.setVisibility(View.GONE);
            tela.invalidate();
            /**
             * Calcula e retorna o ângulo entre as 2ª e 4ª retas brancas geradas
             */
            Reta reta1 = tela.getListReta().get(1);
            Reta reta2 = tela.getListReta().get(3);

            Circulo pontoUmReta1 = new Circulo(reta1.getxInicio(), reta1.getyInicio());
            Circulo pontoDoisReta1 = new Circulo(reta1.getxFinal(), reta1.getyFinal());

            Circulo pontoUmReta2 = new Circulo(reta2.getxInicio(), reta2.getyInicio());
            Circulo pontoDoisReta2 = new Circulo(reta2.getxFinal(), reta2.getyFinal());

            int angulo = (int) tela.getAnguloEntreRetas(pontoUmReta1, pontoDoisReta1, pontoUmReta2, pontoDoisReta2);
            System.out.println("Ângulo da deformidade: " + angulo);
            angulo2 = angulo;
        }
        else if (numProcesso == 10){
            Intent activity = new Intent(this, FinalActivity.class);
            activity.putExtra("angulo1", angulo1);
            activity.putExtra("angulo2", angulo2);
            startActivity(activity);
        }

        if(numProcesso == 10) {
            return numProcesso;
        }
        else {
            return numProcesso + 1;
        }
    }


    public int anteriorNumeroProcesso(int numProcesso){
        if(numProcesso == 1) {
            return numProcesso;
        }
        else {
            return numProcesso - 1;
        }
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

    public Bitmap Base64ParaBitmap(String imagemB64){
        //decodifica base64 para byte
        byte[] imgByte = Base64.decode(imagemB64, Base64.DEFAULT);
        //decodifica byte para Bitmap
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

    public void getImagem(String id) {
        System.out.println("ID____"+id);
        url.child("Imagens").child(id).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // retorna imagemBase64
                    Imagem img = dataSnapshot.getValue(Imagem.class);
                    imgBase64 = img.getImagem();
                    String imagem = img.getImagem().substring(HEADER, imgBase64.length());
                    imgBitmap = Base64ParaBitmap(imagem);
                    System.out.println("IMG____"+imagem);
                    tela.setImg(new BitmapDrawable(getResources(), imgBitmap));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Erro no banco", databaseError.getMessage());
                }
            });
    }
}