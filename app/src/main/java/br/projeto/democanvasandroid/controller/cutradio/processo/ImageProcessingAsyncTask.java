package br.projeto.democanvasandroid.controller.cutradio.processo;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


public class ImageProcessingAsyncTask extends AsyncTask<Void, Void, Void> {
	
	private Callback callback;
	
	public  interface Callback {
		void onComplete();
	}
	
	public ImageProcessingAsyncTask(Callback callback){
		this.callback = callback;
	}
	
	Mat loadedImage1,cloadedImage1;
	int r,g,b;
	int pos;

	byte buff1[], copyBuff1[];
	private Bitmap img;
	byte limiarR = (byte)128;
	byte limiarG = (byte)128;
	byte limiarB = (byte)128;
	
	
	public static Mat loadfiles(String path){

		Mat pmat = Highgui.imread(path);
		Log.i("pixelspmat",(pmat.total()+" "+pmat.channels()+" "+pmat.cols()+" "+pmat.rows()));
		Imgproc.cvtColor(pmat, pmat, Imgproc.COLOR_BGR2RGB);
		return pmat;
	}
	
	
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		callback.onComplete();
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		
		UtilsPJ.getInstance().imageClose = UtilsPJ.getInstance().compressImage(UtilsPJ.getInstance().imageClose);
		loadedImage1 = loadfiles(UtilsPJ.getInstance().imageClose);
		Imgproc.GaussianBlur(loadedImage1, loadedImage1, new Size(3, 3), 0);
		cloadedImage1=loadedImage1.clone();

		buff1 = new byte[(int) (loadedImage1.total() * loadedImage1.channels())];
		copyBuff1 = new byte[(int) (cloadedImage1.total() * cloadedImage1.channels())];
		Log.i("pixels",(loadedImage1.total() * loadedImage1.channels())+"  "+(cloadedImage1.total() * cloadedImage1.channels()));
		//rastrear r�gua
		loadedImage1.get(0, 0, buff1);
		cloadedImage1.get(0, 0, copyBuff1);
		// working with buff
		// ...
		for(int row = 0; row < loadedImage1.rows(); row++)
		{
			for(int col = 0; col < loadedImage1.cols()* loadedImage1.channels(); col+=3)
			{
				pos = row*loadedImage1.cols()* loadedImage1.channels()+col;
				r=buff1[(pos)]& 0xff;//=(byte) 0;
				g=buff1[(pos)+1]& 0xff;// =(byte) 0;
				b=buff1[(pos)+2]& 0xff;// = (byte) 255;
				if (r>(limiarR & 0xff) && g > (limiarG & 0xff) && b > (limiarB & 0xff)){
					copyBuff1[(pos)]=(byte) 255;
					copyBuff1[(pos)+1]=(byte) 255;
					copyBuff1[(pos)+2]= (byte) 255;
				}
				else{
					copyBuff1[(pos)]=(byte) 0;
					copyBuff1[(pos)+1]=(byte) 0;
					copyBuff1[(pos)+2]= (byte) 0;
				}

			}
		}
		Point menorXmenorY = new Point(9999,9999), maiorXmaiorY = new Point(0,0);
		cloadedImage1.put(0, 0, copyBuff1);
		buff1 = null;
		cloadedImage1.get(0, 0, copyBuff1);
		for(int row = 0; row < cloadedImage1.rows(); row++)
		{
			for(int col = 0; col < cloadedImage1.cols()* cloadedImage1.channels(); col+=3)
			{
				pos = row*cloadedImage1.cols()* cloadedImage1.channels()+col;
				r=copyBuff1[(pos)]& 0xff;//=(byte) 0;
				g=copyBuff1[(pos)+1]& 0xff;// =(byte) 0;
				b=copyBuff1[(pos)+2]& 0xff;// = (byte) 255;
				if (r==(255 & 0xff) && g == (255 & 0xff) && b == (255 & 0xff)){
					if(row<menorXmenorY.y)
						menorXmenorY = new Point(col, row);
					if(row>maiorXmaiorY.y)
						maiorXmaiorY = new Point(col, row);
				}
			}
		}
		menorXmenorY.x = menorXmenorY.x/3;
		maiorXmaiorY.x = maiorXmaiorY.x/3;
		Log.i("ponto 1", menorXmenorY.x +" "+menorXmenorY.y);
		Log.i("ponto 2", maiorXmaiorY.x +" "+maiorXmaiorY.y);
		Core.line(cloadedImage1, new Point(0,menorXmenorY.y), new Point(UtilsPJ.MAX_WIDTH,menorXmenorY.y), new Scalar(255,255,0));
		Core.line(cloadedImage1, new Point(0,maiorXmaiorY.y), new Point(UtilsPJ.MAX_WIDTH,maiorXmaiorY.y), new Scalar(255,0,0));
		Core.line(cloadedImage1, new Point(0,menorXmenorY.y), maiorXmaiorY, new Scalar(255,0,0));
		Rect rectCrop = new Rect(0,(int)menorXmenorY.y, loadedImage1.cols(), (int)maiorXmaiorY.y-(int)menorXmenorY.y);
		Mat imCrop=  new Mat(loadedImage1,rectCrop);

		img = Bitmap.createBitmap(imCrop.cols(), imCrop.rows(),Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(imCrop, img);
		UtilsPJ.getInstance().imageFinalClose = UtilsPJ.saveCanvas(img);
		
		return null;
	}
	
	protected void onPostExecute() {
		super.onPostExecute(null);
		callback.onComplete();
	}
	
	

}
