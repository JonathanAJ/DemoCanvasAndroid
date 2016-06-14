package br.projeto.democanvasandroid.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.model.Circulo;

public class TelaView extends View {
    /**
     * Constantes
     */
    final private int AFASTAMENTO = 100;
    /**
     * Flags
     */
    private boolean circInicial = true;
    private boolean zoom = false;

    /**
     * Variáveis de Estilização
     */
    private Paint paintCirc;
    private Paint paintLine;

    /**
     * Variáveis de componentes
     */
    private Circulo circulo;
    private ArrayList<Circulo> listCirc;
    private Iterator<Circulo> circIt;
    private Drawable img;

    /**
     * Variaveis de Posicionamento
     */
    private float inicioY = 0;
    private float scrollY = 0;
    private float anteriorScrollY = 0;

    /**
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
        paintCirc.setColor(Color.RED);

        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.RED);
        paintLine.setStrokeWidth(3);

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
         * Translate é um método que posiciona toda a tela do canvas,
         * no caso, receberá a posição do Scroll Y.
         */
        canvas.translate(0, scrollY);
        canvas.drawColor(Color.BLACK);
        desenhaImagem(canvas, getImg());

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

            /**
             * Desenha valor do círculo da iteração
             */
            paintCirc.setColor(Color.RED);
            desenhaCirculo(canvas, novoCirc, paintCirc);

            /**
             * Dependendo do valor atual do número da iteração, faça
             * as retas mediante o valor das determinadas posições
             * nos círculos criados.
             */
            switch(num){
                case 3 :
                    /**
                     * Desenha retas saindo dos círculos 0 e 1 (primeiro e segundo) em direção ao novo círculo criado.
                     */
                    desenhaLinha(canvas, listCirc.get(0).getX(), listCirc.get(0).getY(), listCirc.get(2).getX(), listCirc.get(2).getY(), paintLine);
                    desenhaLinha(canvas, listCirc.get(1).getX(), listCirc.get(1).getY(), listCirc.get(2).getX(), listCirc.get(2).getY(), paintLine);
                    /**
                     * Desenha a perpendicular da segunda reta criada.
                     */
                    desenhaPerpendicular(canvas, listCirc.get(1), listCirc.get(2), paintLine);
                    break;
                case 5 :
                    /**
                     * Desenha retas saindo do círculo 3 (quarto) em direção ao novo círculo criado.
                     */
                    desenhaLinha(canvas, listCirc.get(3).getX(), listCirc.get(3).getY(), listCirc.get(4).getX(), listCirc.get(4).getY(), paintLine);
                    /**
                     * Desenha a perpendicular dos 4º e 5º pontos criados, que formam a última reta.
                     * O ponto de referência é o 4º, por isso ele está como primeiro.
                     */
                    desenhaPerpendicular(canvas, listCirc.get(4), listCirc.get(3), paintLine);
                    break;
            }
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
            canvas.drawCircle((int) circulo.getX(), (int) circulo.getY(), raio, paintCirc);
        }else {
            canvas.drawCircle((int) circulo.getX(), (int) circulo.getY(), raio, paintCirc);
        }
    }

    public void desenhaLinha(Canvas canvas, float xStart, float yStart, float xStop, float yStop, Paint paintLine){
        paintLine.setStrokeWidth(3);
        paintLine.setColor(Color.RED);
        canvas.drawLine(xStart, yStart, xStop, yStop, paintLine);
    }

    /**
     * Retorna o coeficiente angular, dado por:
     *    m = (y² - y¹)/ (x² - x¹)
     * Logo após retorna o negativo do recíproco do coeficiente.
     *    -1/(m)
     */
    public float getCoeficienteAngular(Circulo ponto1, Circulo ponto2){
        float coeficiente = ((ponto2.getY() - ponto1.getY()) / (ponto2.getX() - ponto1.getX()));
        // negativo do recíproco
        return (-1/(coeficiente));
    }

    /**
     * Retorna o coeficiente linear, dado por:
     *    b = y - mx
     *
     * Escolhendo apenas um dos pontos da reta para
     * representar o x e o y, no caso, sempre será
     * o segundo ponto dado.
     *
     */
    public float getCoeficienteLinear(Circulo ponto1, Circulo ponto2){
        // Retorna o coeficiente angular
        float coeficienteAngular = getCoeficienteAngular(ponto1, ponto2);
        // Coeficiente linear: b = y - mx;
        // Escolhe um dos pontos, no caso, o segundo;
        return (ponto2.getY() - (coeficienteAngular * ponto2.getX()));
    }

    /**
     * Calcula com base na equação reduzida da reta,
     *     (y = mx + b)
     * o valor de Y (perpendicular) dado um X qualquer.
     * No caso, o X é o valor X do ponto + afastamento.
     */
    public float getYPerpendicular(Circulo ponto1, Circulo ponto2, float afastamento){
        // Retorna o coeficiente linear
        float coeficienteLinear = getCoeficienteLinear(ponto1, ponto2);
        // Retorna o coeficiente angular
        float coeficienteAngular = getCoeficienteAngular(ponto1, ponto2);
        // Equação reduzida da reta y = mx + b
        return ((coeficienteAngular * (ponto2.getX() + afastamento)) + coeficienteLinear);
    }

    public void desenhaPerpendicular(Canvas canvas, Circulo ponto1, Circulo ponto2, Paint paintLine){
        /**
         * Retorna o valor de Y da perpendicular dado um determinado valor de X,
         * no caso, o valor de x é relativo ao x do ponto + um afastamento padrão.
         */
        float yPerpendicular1 = getYPerpendicular(ponto1, ponto2, - AFASTAMENTO);
        float yPerpendicular2 = getYPerpendicular(ponto1, ponto2, AFASTAMENTO);
        /**
         * Desenha a reta perpendicular com os valores obtidos no cálculo.
         * Perpendicular 1 refere-se ao afastamento negativo, enquanto que
         * Perpendicular 2 ao afastamento positivo.
        */
        // linhas brancas
        paintLine.setColor(Color.WHITE);
        paintLine.setStrokeWidth(5);
        canvas.drawLine(ponto2.getX() - AFASTAMENTO, yPerpendicular1, ponto2.getX() + AFASTAMENTO, yPerpendicular2, paintLine);
    }

    /**
     * Desenha a imagem da radiografia na tela.
     */
    public void desenhaImagem(Canvas canvas, Drawable d){
        // calcula centro
        float larguraCanvas = canvas.getWidth();
        float larguraImagem = d.getIntrinsicWidth();
        float alturaImagem = d.getIntrinsicHeight();
        /**
         * retorna o aspect ratio da imagem para calcular sua altura,
         * multiplicando ele pela largura e arredondando.
         */
        float aspectRatio = getAspectRatio(alturaImagem, larguraImagem);
        int baixo = Math.round(larguraCanvas*aspectRatio);

        // left, top, right, bottom.
        d.setBounds(0, 0, (int) larguraCanvas, baixo);
        d.draw(canvas);
    }

    public float getAspectRatio(float alturaImage, float larguraImagem){
        /**
         * Calcula AspectRatio da imagem
         */
        return alturaImage/larguraImagem;
    }

    public void salvaListaCirculo(float x, float y){
        Circulo circuloSave = new Circulo();
        circuloSave.setX(x);
        circuloSave.setY(y);
        listCirc.add(circuloSave);
        circInicial = true;
        invalidate();
    }

    /**
     * Evento de touch para posicionamento e colocação dos pontos.
     */
    @Override
    public boolean onTouchEvent (MotionEvent event) {
        switch (event.getAction()) {
            // aperta o touch
            case MotionEvent.ACTION_DOWN:
                /**
                 * Verifica se a flag zoom está ligada e retorna o
                 * ponto atual do DOWN menos o scroll anterior.
                 */
                if(isZoom()){
                    inicioY = event.getY() - anteriorScrollY;
                    scrollY = 0;
                }else{
                /**
                 * Senão, recoloca o circulo em sua posicao atual, subtraindo o
                 * valor do scroll.
                 */
                    circulo.setX(event.getX());
                    circulo.setY(event.getY() - scrollY);
                    invalidate();
                }
                break;
            // move o touch
            case MotionEvent.ACTION_MOVE:
                /**
                 * Valor do scroll recebe o valor atual do MOVE
                 * subtraindo do DOWN inicioY.
                 */
                if(isZoom()){
                    scrollY = event.getY() - inicioY;
                }else{
                    circulo.setX(event.getX());
                    circulo.setY(event.getY() - scrollY);
                }
                invalidate();
                break;
            // solta o touch
            case MotionEvent.ACTION_UP:
                /**
                 * Valor do Scroll Anterior recebe valor do Scroll quando
                 * o usuário solta o touch.
                 */
                if(isZoom()) {
                    anteriorScrollY = scrollY;
                }
                invalidate();
                break;
        }
        return true;
    }

}