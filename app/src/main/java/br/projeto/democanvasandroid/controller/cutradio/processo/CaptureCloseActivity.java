package br.projeto.democanvasandroid.controller.cutradio.processo;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import br.projeto.democanvasandroid.R;
import br.projeto.democanvasandroid.controller.cutradio.fileexplore.FileExplore;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CaptureCloseActivity extends Activity implements ImageProcessingAsyncTask.Callback{


	private Bitmap img;

	ImageView iv1;
	PhotoViewAttacher mAttacher;
	Button bot;

	private ImageProcessingAsyncTask imgTask;

	static final int REQUEST_TAKE_PHOTO1 = 1;
	static final int REQUEST_TAKE_FILE2 = 2;
	private static final String CAPTURED_PHOTO_PATH_KEY = "CAPTURED_PHOTO_PATH_KEY";
	private static final String CAPTURED_PHOTO_ABSOLUT_KEY = "CAPTURED_PHOTO_ABSOLUT_KEY";
	static String mCurrentPhotoPath,mCurrentAbsolutPath;

	
	ProgressDialog progress;
	
	public void onComplete() {
		progress.dismiss();
		Toast.makeText(getApplicationContext(), "Processamento de imagem finalizado", Toast.LENGTH_LONG).show();
		img = BitmapFactory.decodeFile(UtilsPJ.getInstance().imageFinalClose);
		iv1.setImageBitmap(img);
		mAttacher = new PhotoViewAttacher(iv1);
				
}
	
	@Override 
	public void onSaveInstanceState(Bundle savedInstanceState) { 
	   if (mCurrentPhotoPath != null) {            
	     savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath); 
	   } 
	  if (mCurrentAbsolutPath != null) {  
	     savedInstanceState.putString(CAPTURED_PHOTO_ABSOLUT_KEY, mCurrentAbsolutPath); 
	  } 
	  super.onSaveInstanceState(savedInstanceState); 
	} 

	@Override 
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

	   if (savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
	       mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY); 
	   } 
	   if (savedInstanceState.containsKey(CAPTURED_PHOTO_ABSOLUT_KEY)) {
		   mCurrentAbsolutPath = savedInstanceState.getString(CAPTURED_PHOTO_ABSOLUT_KEY); 
	   } 
	   super.onRestoreInstanceState(savedInstanceState); 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		iv1 = (ImageView) findViewById(R.id.imageViewBinary1);
		// Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
		// (not needed unless you are going to change the drawable later)
		mAttacher = new PhotoViewAttacher(iv1);
		bot = (Button) findViewById(R.id.buttonProximo);
		bot.setText("Cortar");
		progress = new ProgressDialog(this);
		progress.setMessage("Processando Imagens");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);
		if(!UtilsPJ.STORAGE_DIR.exists())
			UtilsPJ.STORAGE_DIR.mkdir();
		UtilsPJ.getInstance().imageClose = new File(UtilsPJ.STORAGE_DIR,"teste.jpg").getAbsolutePath();//mCurrentAbsolutPath;
		
	}

		

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		imgTask = new ImageProcessingAsyncTask(this);
		//carrega opencv pela primeira vez e possibilita usuário cortar imagem padrão
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}



	private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{

				Toast.makeText(getApplicationContext(),"OpenCV loaded successfully",Toast.LENGTH_SHORT).show();


			} 
			break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};
	
	private BaseLoaderCallback  resumeCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
					
			} 
			break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};
	
    //Se o resultado da Intent for a captura da câmera, trata o arquivo gerado pela mesma
    //Se o resultado da Intent for um arquivo do disco do celular, tratar esse arquivo
    //Em ambos os casos, recarregam o opencv, devido à alguns celulares descarregarem essa operação
    // da memória quando a aplicaçao chama outra.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (resultCode == RESULT_OK)
			if (requestCode == REQUEST_TAKE_PHOTO1) {
				UtilsPJ.getInstance().imageClose = mCurrentAbsolutPath;
				OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, resumeCallback);
				bot.setText("Cortar");

			}
			else if(requestCode == REQUEST_TAKE_FILE2){
				TinyDB db = new TinyDB(getApplicationContext());
				if(UtilsPJ.getInstance().imageClose==null||UtilsPJ.getInstance().imageClose.equals("")){
					UtilsPJ.getInstance().imageClose = db.getString(UtilsPJ.IMG_CLOSE_URL);
				}
				OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, resumeCallback);
				bot.setText("Cortar");
			}
			img = BitmapFactory.decodeFile(UtilsPJ.getInstance().imageClose);
			iv1.setImageBitmap(img);
			mAttacher.update();
	}



	private void dispatchTakePictureIntent(int code) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				Log.e("IOERROR", ex.getMessage());
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, code);
			}
		}
	}


    //cria arquivo que será preenchido com a imagem obtida pela câmera utilizando a data como nome da imagem
	private File createImageFile() throws IOException {

		String imageFileName =new Date().getTime()+".jpg";
		File image = new File(UtilsPJ.STORAGE_DIR,imageFileName);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		mCurrentAbsolutPath = image.getAbsolutePath();
		Log.e("try", mCurrentAbsolutPath);
		return image;
	}

    //chama câmera nativa para que retorne a fotografia que irá ser cortada
	public void chamaPhoto(View v){
		dispatchTakePictureIntent(REQUEST_TAKE_PHOTO1);
		
	}
    //abre tela para escolher arquivo a ser cortado
	public void subirArquivo(View v){
		Intent fileChooser = new Intent(CaptureCloseActivity.this,FileExplore.class);
		startActivityForResult(fileChooser, REQUEST_TAKE_FILE2);
	
	}
    //inicia o processo de cortar a foto
	public void proximaFoto(View v){
		progress.show();
		imgTask.execute();
		bot.setEnabled(true);
		bot.setText("Cortar");

	}

}
