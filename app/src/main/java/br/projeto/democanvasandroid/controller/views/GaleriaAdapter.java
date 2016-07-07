package br.projeto.democanvasandroid.controller.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.util.List;

import br.projeto.democanvasandroid.model.Imagem;

public class GaleriaAdapter extends PagerAdapter{

    //Header do base64 > data:image/png;base64,
    private final int HEADER = 22;

    private Context context;
    
    private List<Imagem> imgList;

    public GaleriaAdapter(Context context, List<Imagem> imgList){
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public int getCount(){
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj){
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){

        LinearLayout.LayoutParams lp;
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                           LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lp);
        container.addView(ll);

        Imagem img = imgList.get(position);
        String imagem = img.getImagem().substring(HEADER, img.getImagem().length());
        Bitmap bitmap = Base64ParaBitmap(imagem);
        ImageView image = new ImageView(context);
        image.setImageBitmap(bitmap);
        image.setLayoutParams(lp);
        ll.addView(image);

        return ll;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view){
        container.removeView((View) view);
    }


    public Bitmap Base64ParaBitmap(String imagemB64){
        //decodifica base64 para byte
        byte[] imgByte = Base64.decode(imagemB64, Base64.DEFAULT);
        //decodifica byte para Bitmap
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

}