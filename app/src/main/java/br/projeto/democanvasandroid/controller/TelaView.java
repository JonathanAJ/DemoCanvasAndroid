package br.projeto.democanvasandroid.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.model.Circulo;

public class TelaView extends View {

    private boolean circInicial = true;
    private boolean zoom = false;

    private Paint paintCirc;
    private Paint paintLine;
    private Circulo circulo;
    private ArrayList<Circulo> listCirc;
    private Iterator<Circulo> circIt;
    private Drawable img;

    private float yImg = 0;

    /**
     *
     * getters e setters
     */

    public boolean isCircInicial() {
        return circInicial;
    }

    public void setCircInicial(boolean circInicial) {
        this.circInicial = circInicial;
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    public Circulo getCirculo() {
        return circulo;
    }

    public void setCirculo(Circulo circulo) {
        this.circulo = circulo;
    }

    public float getyImg() {
        return yImg;
    }

    public void setyImg(float yImg) {
        this.yImg = yImg;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
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

        TypedArray typedArray = null;
        try{
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.TelaView);
            setImg(typedArray.getDrawable(R.styleable.TelaView_img_drawable));

        } finally {
            typedArray.recycle();
        }
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
        paintCirc.setColor(Color.WHITE);

        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.RED);

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
        /**
         * Desenha fundo e imagem antes de tudo.
         * Imagem recebe o yImg.
         */
        canvas.drawColor(Color.BLACK);
        desenhaImagem(canvas, getImg(), getyImg());

        /**
         * Desenha círculo circInicial em 50% da tela
         */
        if(circInicial) {
            circulo.setX((float) (getWidth()*0.5));
            circulo.setY((float) (getHeight()*0.5));
            circInicial = false;
        }

        /**
         * @circIt : lista de Círculos que passará por uma iteração.
         * @circuloTmpAntPar : cria um círculo temporário para receber o valor da posiçao do anterior sempre par.
         * @circuloTmp1 : cria um círculo temporário para receber o valor da posiçao do circulo 1 par.
         * @circuloTmp2 : cria um círculo temporário para receber o valor da posiçao do circulo 2 par.
         * @num : cria um inteiro que representará a posição dos Circulos que criam a reta comum.
         * @num2 : cria um inteiro que representará a posição dos Circulos que criam a reta perpendicular.
         */
        circIt = listCirc.iterator();
        Circulo circuloTmpAntPar = null;
        Circulo circuloTmpAnt1 = null;
        Circulo circuloTmpAnt2 = null;
        int num1 = 1;
        int num2 = 1;
        while(circIt.hasNext()) {
            Circulo novoCirc = circIt.next();

            if(num1%2 == 0){
                Log.i(" Coords : ", "DRAW LINE");
                desenhaLinha(canvas, circuloTmpAntPar.getX(), circuloTmpAntPar.getY(), novoCirc.getX(), novoCirc.getY(), paintLine);

                if(num2%2 == 0){
                    circuloTmpAnt2 = novoCirc;
                    Log.i(" Coords : ", "DRAW LINE 2");
                    desenhaLinha(canvas, circuloTmpAnt1.getX(), circuloTmpAnt1.getY(), circuloTmpAnt2.getX(), circuloTmpAnt2.getY(), paintLine);
                }

                circuloTmpAnt1 = novoCirc;
                num2++;
            }

            paintCirc.setColor(Color.RED);
            desenhaCirculo(canvas, novoCirc, paintCirc);

            circuloTmpAntPar = novoCirc;

            Log.i(" Coords : ", "Pos = " + num1 + " -- x = " + novoCirc.getX() + " -- y =" + novoCirc.getY());
            num1++;
        }

        paintCirc.setColor(Color.YELLOW);
        desenhaCirculo(canvas, circulo, paintCirc);
    }

    /**
     *
     * Método desenhaCirculo() cria circulo nas definições do paint
     * com x e y definidos pelo evento de touch.
     */

    public void desenhaCirculo(Canvas canvas, Circulo circulo, Paint paintCirc){
        /**
         * Raio recebe 2,5% da tela. Ocupando 5% da mesma.
         */
        float raio = (float) (canvas.getWidth()*0.025);
        canvas.drawCircle(circulo.getX(), circulo.getY(), raio, paintCirc);
    }

    public void desenhaLinha(Canvas canvas, float xStart, float yStart, float xStop, float yStop, Paint paintLine){
        paintLine.setStrokeWidth(3);
        canvas.drawLine(xStart, yStart, xStop, yStop, paintLine);
    }

    public void desenhaImagem(Canvas canvas, Drawable d, float yImg){
        // calcula centro
        int larguraCanvas = canvas.getWidth();
        int larguraImagem = d.getMinimumWidth();
        int centro = (larguraCanvas/2) - (larguraImagem/2);

        // left, top, right, bottom.
        d.setBounds(centro, (int) (yImg - (d.getMinimumHeight()/2)), d.getMinimumWidth() + centro, (int) (d.getMinimumHeight() + (yImg - (d.getMinimumHeight()/2))));
        d.draw(canvas);
    }

    public void salvaListaCirculo(float x, float y){
        Circulo circuloSave = new Circulo();
        circuloSave.setX(x);
        circuloSave.setY(y);
        Log.i("Coords circuloSave : ", circuloSave.getX() + " -- " + circuloSave.getY());
        listCirc.add(circuloSave);
        circInicial = true;
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
                if(isZoom()){
                    setyImg(event.getY());
                }else{
                    circulo.setX(event.getX());
                    circulo.setY(event.getY());
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if(isZoom()){
                    setyImg(event.getY());
                }else{
                    circulo.setX(event.getX());
                    circulo.setY(event.getY());
                }
                invalidate();
                break;
        }
        return true;
    }
}