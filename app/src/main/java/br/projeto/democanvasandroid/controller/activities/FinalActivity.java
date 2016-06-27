package br.projeto.democanvasandroid.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.projeto.democanvasandroid.R;

public class FinalActivity extends AppCompatActivity {

    private TextView txt1;
    private TextView txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Bundle bd = getIntent().getExtras();
        int angulo1 = bd.getInt("angulo1");
        int angulo2 = bd.getInt("angulo2");

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);

        txt1.setText("O Ângulo entre o alinhamento mecânico e anatômico é : " + angulo1 + "°");
        txt2.setText("O Ângulo da deformidade é : " + angulo2 + "°");
    }
}
