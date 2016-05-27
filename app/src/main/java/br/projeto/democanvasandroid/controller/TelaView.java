package br.projeto.democanvasandroid.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.gesture.Gesture;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.model.Circulo;

public class TelaView extends View {

    final private int SLOW_SLIDE = 10;

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
         * @num : cria um inteiro que representará a posição dos Circulos que criam as retas.
         */
        circIt = listCirc.iterator();
        int num = 1;
        while(circIt.hasNext()) {
            /**
             * Recebe valor do círculo da iteração
             */
            Circulo novoCirc = circIt.next();

            switch(num){
                case 3 :
                    /**
                     * Desenha retas saindo dos círculos 0 e 1 (primeiro e segundo) em direção ao novo círculo criado.
                     */
                    desenhaLinha(canvas, listCirc.get(0).getX(), listCirc.get(0).getY(), novoCirc.getX(), novoCirc.getY(), paintLine);
                    desenhaLinha(canvas, listCirc.get(1).getX(), listCirc.get(1).getY(), novoCirc.getX(), novoCirc.getY(), paintLine);
                    break;
                case 5 :
                    /**
                     * Desenha retas saindo do círculo 3 (quarto) em direção ao novo círculo criado.
                     */
                    desenhaLinha(canvas, listCirc.get(3).getX(), listCirc.get(3).getY(), novoCirc.getX(), novoCirc.getY(), paintLine);
                    break;
            }

            /**
             * Desenha valor do círculo da iteração
             */
            paintCirc.setColor(Color.RED);
            desenhaCirculo(canvas, novoCirc, paintCirc);

            Log.i(" Coords : ", "Pos = " + num + " -- x = " + novoCirc.getX() + " -- y =" + novoCirc.getY());
            num++;
        }
        /**
         * Se chegar no limite de círculos que devem ser criados (5), não desenhe nada.
         */
        if(num <= 5){
            paintCirc.setColor(Color.YELLOW);
            desenhaCirculo(canvas, circulo, paintCirc);
        }
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

        if(isZoom()) {
            circulo.setY(circulo.getY() + (resultScroll / SLOW_SLIDE));
            canvas.drawCircle(circulo.getX(), circulo.getY(), raio, paintCirc);
        }else {

            canvas.drawCircle(circulo.getX(), circulo.getY(), raio, paintCirc);

        }
    }

    public void desenhaLinha(Canvas canvas, float xStart, float yStart, float xStop, float yStop, Paint paintLine){
        paintLine.setStrokeWidth(3);
        canvas.drawLine(xStart, yStart, xStop, yStop, paintLine);
    }

    public void desenhaImagem(Canvas canvas, Drawable d, float yImg){
        // calcula centro
        int larguraCanvas = canvas.getWidth();
        int larguraImagem = d.getIntrinsicWidth();

        int esquerdo = (larguraCanvas/2) - (larguraImagem/2);
        int topo = (int) (yImg);
        int direito = d.getIntrinsicWidth() + esquerdo;
        int baixo = (int) (yImg + (d.getIntrinsicHeight()));
        // left, top, right, bottom.
        d.setBounds(esquerdo, topo, direito, baixo);
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

    float down;
    float resultScroll;

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isZoom()){

                    System.out.println(" DOWN " + event.getY());

                    down = event.getY();

                    resultScroll = 0;

                }else{
                    circulo.setX(event.getX());
                    circulo.setY(event.getY());
                }
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:

                if(isZoom()){

                    int baixo = (int) (yImg + (getImg().getIntrinsicHeight()));

                    resultScroll = event.getY() - down;

                    float imageMove = getyImg() + (resultScroll/SLOW_SLIDE);

                    System.out.println(" MOVE " + event.getY());

                    /*System.out.println(" IMAGE_MOVE " + imageMove);

                    System.out.println(" CALC MOVE " + resultScroll);

                    System.out.println(" IMG ALT = " + baixo);

                    System.out.println(" CANVAS ALT = " + getHeight());*/

                    setyImg(imageMove);
                    invalidate();


                }else{
                    circulo.setX(event.getX());
                    circulo.setY(event.getY());

                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                
                if(isZoom()) {

                    resultScroll = event.getY() - down;

                    float imageMove = getyImg() + (resultScroll / SLOW_SLIDE);

                    System.out.println(" UP ");

                    setyImg(imageMove);
                } else {

                }
                invalidate();
                break;
        }
        return true;
    }

}