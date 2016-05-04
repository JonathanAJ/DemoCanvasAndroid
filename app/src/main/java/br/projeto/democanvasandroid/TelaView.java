package br.projeto.democanvasandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class TelaView extends View {

    private View rootView;
    private Button btSalva;
    private Paint paint = new Paint();
    private float xPos = 0;
    private float yPos = 0;
    private boolean inicial = true;

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

//        btSalva = (Button) findViewById(R.id.btSalva);
//        btSalva.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                xPos = (float) (getWidth()*0.5);
//                yPos = (float) (getHeight()*0.5);
//                invalidate();
//            }
//        });
    }

    /**
     *
     * Método onDraw desenha na tela
     */

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(inicial){
            xPos = (float) (getWidth()*0.5);
            yPos = (float) (getHeight()*0.5);
            inicial = false;
        }
        desenhaCirculo(canvas, xPos, yPos, paint);
    }

    /**
     *
     * Método desenhaCirculo() cria circulo nas definições do paint
     * com x e y definidos pelo evento de touch.
     */

    public void desenhaCirculo(Canvas canvas, float x, float y, Paint paint){
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(240, 0, 0));
        canvas.drawCircle(x, y, 5, paint);
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
                xPos = event.getX();
                yPos = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                xPos = event.getX();
                yPos = event.getY();
                invalidate();
                break;
        }
        return true;
    }
}