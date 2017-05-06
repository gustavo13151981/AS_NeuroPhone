package pe.edu.upc.caguilar.neurophone.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class CamaraActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback jpegCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        Utility.currentActivity = this;

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                FileOutputStream outputStream = null;
                File file_image = getDirc();
                if (!file_image.exists() && !file_image.mkdirs()){
                    Toast.makeText(getApplicationContext(),"Can't create directory to save image",Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = simpleDateFormat.format(new Date());
                String photofile = "NeuroPhone" + date + ".jpg";
                String file_name = file_image.getAbsolutePath() + "/" + photofile;

                File picfile = new File(file_name);

                try{
                    outputStream= new FileOutputStream(picfile);
                    outputStream .write(data);
                    outputStream.close();
                }
                catch (FileNotFoundException e){
                    Utility.PrintDebug("Catch1 (PictureCallback) : ",e.getMessage(),null);
                }catch (IOException e){
                    Utility.PrintDebug("Catch2 (PictureCallback) : ",e.getMessage(),null);
                }

                Toast.makeText(getApplicationContext(),"Picture saved",Toast.LENGTH_SHORT).show();
//                refreshCamera();
                refreshGallery(picfile);
            }
        };
    }

    /*#############################################################################################*/
    private void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    /*#############################################################################################*/
    private File getDirc(){
        File dics = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(dics,"NeuroPhone_Camera");
    }

    /*#############################################################################################*/
    public void btnTakePicture(View v){

        camera.takePicture(null,null,jpegCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        try{
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

        }catch (RuntimeException e){
            Utility.PrintDebug("Catch (surfaceCreated) : ",e.getMessage(),null);
        }

        Camera.Parameters parameters;
        parameters=camera.getParameters();
        //modify parameter
        parameters.setPreviewFrameRate(20);
        parameters.setPreviewSize(352,288);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }catch (Exception e){
            Utility.PrintDebug("Catch2 (surfaceCreated) : ",e.getMessage(),null);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    /*#############################################################################################*/
    public void RecibirMensaje(String texto) {

        try {

            switch (texto){
                case  "Tomar":
                    btnTakePicture(this.getCurrentFocus());
                    break;

                case "Finalizar":
                    Intent intent = new Intent(this, MainMenuActivity.class);
                    startActivity(intent);
                    break;
            }
        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }
}
