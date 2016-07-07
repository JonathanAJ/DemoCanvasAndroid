package br.projeto.democanvasandroid.controller.cutradio.processo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

public class UtilsPJ {
	
	public final static File STORAGE_DIR = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/teste/");
	private static  UtilsPJ singleton;
	public String imageFar = new File(UtilsPJ.STORAGE_DIR,"raio_pequena_ang.jpg").getAbsolutePath();
	public String imageClose = new File(UtilsPJ.STORAGE_DIR,"raio_grande_ang.jpg").getAbsolutePath();
	public String imageFinalFar;
	public String imageFinalClose;
	public double point1x,point2x,point3x,point4x;
	public double point1y,point2y,point3y,point4y;
	public double h1;
	public double point21x,point22x,point23x,point24x;
	public double point21y,point22y,point23y,point24y;
	public double h2;
	public String imageMerged;
	public static final float MAX_HEIGTH = 1224.0f;
	public static final float MAX_WIDTH = 918.0f;
	public static final String IMG_CLOSE_URL = "IMG_CLOSE_URL";
	
	private UtilsPJ(){}
	
	public static synchronized UtilsPJ getInstance(){
		if(singleton == null){
			singleton = new UtilsPJ();
			return singleton;
		}
		else
			return singleton;
	}
	
	public static Mat loadfiles(String path){
		//Mat pmat = new Mat(width,height, CvType.CV_8UC3);
		//pmat = Utils.loadResource(MainActivity.this, p,Highgui.CV_LOAD_IMAGE_COLOR);
		Mat pmat = Highgui.imread(path);
		Log.i("pixelspmat",(pmat.total()+" "+pmat.channels()+" "+pmat.cols()+" "+pmat.rows()));
		Imgproc.cvtColor(pmat, pmat, Imgproc.COLOR_BGR2RGB);//remove this if your image don't need  to convert. 
		//			Imgproc.resize(pmat, pmat, new Size(width, height), 0, 0, Imgproc.INTER_CUBIC); 
		return pmat;
	}
	
	public static String saveCanvas(Bitmap b) {
		File storageDir = new File(STORAGE_DIR.getAbsolutePath());
		String fileName = "";
		if(!storageDir.exists())
			storageDir.mkdir();
		//String path = customCanvas.bitMapToFile(this.getExternalFilesDir(null) + "" + new Date().getTime() + ".png");
		//create a file to write bitmap data
		File f = new File(storageDir, new Date().getTime() + ".png");
		try {
			f.createNewFile();


			//Convert bitmap to byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			b.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
			byte[] bitmapdata = bos.toByteArray();
			//write the bytes in file
			FileOutputStream fos = new FileOutputStream(f);
			fileName = f.getAbsolutePath();
			fos.write(bitmapdata);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;

	}
	
	public String compressImage(String imageUri) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = MAX_HEIGTH;//1224.0f/544//816
        float maxWidth = MAX_WIDTH;//918.0f//408//612
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = STORAGE_DIR;
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
	
	

}
