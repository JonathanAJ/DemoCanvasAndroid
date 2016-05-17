package br.projeto.democanvasandroid.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import br.projeto.democanvasandroid.R;

public class MainActivity extends AppCompatActivity {

    private TelaView tela;
    private Button btSalva;
    private Switch btZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tela = (TelaView) findViewById(R.id.telaView);

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
}