package br.projeto.democanvasandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;

import br.projeto.democanvasandroid.model.Circulo;

public class TelaView extends View {

    private boolean inicial = true;

    private Paint paintCirc;
    private Paint paintLine;
    private Circulo circulo;
    private ArrayList<Circulo> listCirc;
    private Iterator<Circulo> circIt;

    /**
     *
     * getters e setters
     */

    public boolean isInicial() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public Circulo getCirculo() {
        return circulo;
    }

    public void setCirculo(Circulo circulo) {
        this.circulo = circulo;
    }

    /**
     *
     * Construtores padrões para criar uma Custom View
     */

    public TelaView(Context context){
        super(context);
        init(context);
    }

    public TelaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TelaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     *
     * Inicializa a View
     */

    private void init(Context context) {
        paintCirc = new Paint();
        paintCirc.setAntiAlias(true);
        paintCirc.setColor(Color.rgb(240, 0, 0));


        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.rgb(240, 0, 0));

        circulo = new Circulo();
        circulo.setX(0);
        circulo.setY(0);

        listCirc = new ArrayList<Circulo>();
    }

    /**
     *
     * Método onDraw desenha na tela
     */

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(inicial) {
            circulo.setX((float) (getWidth()*0.5));
            circulo.setY((float) (getHeight()*0.5));
            inicial = false;
        }
        circIt = listCirc.iterator();
        int num = 1;
        Circulo circuloTmp = null;
        while(circIt.hasNext()) {
            Circulo novoCirc = circIt.next();
            if(num%2 == 0){
                Log.i(" Coords : ", "DRAW LINE");
                desenhaLinha(canvas, circuloTmp.getX(), circuloTmp.getY(), novoCirc.getX(), novoCirc.getY(), paintLine);
            }
            desenhaCirculo(canvas, novoCirc, paintCirc);
            circuloTmp = novoCirc;
            Log.i(" Coords : ", "Pos = " + num + " -- x = " + novoCirc.getX() + " -- y =" + novoCirc.getY());
            num++;
        }
        desenhaCirculo(canvas, circulo, paintCirc);
    }

    /**
     *
     * Método desenhaCirculo() cria circulo nas definições do paint
     * com x e y definidos pelo evento de touch.
     */

    public void desenhaCirculo(Canvas canvas, Circulo circulo, Paint paintCirc){
        canvas.drawCircle(circulo.getX(), circulo.getY(), 7, paintCirc);
    }

    public void desenhaLinha(Canvas canvas, float xStart, float yStart, float xStop, float yStop, Paint paintLine){
        canvas.drawLine(xStart, yStart, xStop, yStop, paintLine);
    }

    public void salvaListaCirculo(float x, float y){
        Circulo circuloSave = new Circulo();
        circuloSave.setX(x);
        circuloSave.setY(y);
        Log.i("Coords circuloSave : ", circuloSave.getX() + " -- " + circuloSave.getY());
        listCirc.add(circuloSave);
        inicial = true;
        invalidate();
    }

    /**
     *
     * Evento de touch, muda as variáveis x e y,
     * tanto quanto pressiona DOWN, tanto quanto segura MOVE
     */

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                circulo.setX(event.getX());
                circulo.setY(event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                circulo.setX(event.getX());
                circulo.setY(event.getY());
                invalidate();
                break;
        }
        return true;
    }
}