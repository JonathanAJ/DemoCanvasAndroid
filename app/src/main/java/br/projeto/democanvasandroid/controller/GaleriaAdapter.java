package br.projeto.democanvasandroid.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.List;

public class GaleriaAdapter extends PagerAdapter{

    private Context context;
    
    private List<Bitmap> bitmapList;

    public GaleriaAdapter(Context context, List<Bitmap> bitmapList){
        this.context = context;
        this.bitmapList = bitmapList;
    }

    @Override
    public int getCount(){
        return bitmapList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj){
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.VERTICAL);
        container.addView(ll);

        ImageView image = new ImageView(context);
        image.setImageBitmap(bitmapList.get(position));
        ll.addView(image);

        return ll;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view){
        container.removeView((View) view);
    }


}