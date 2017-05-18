package pe.edu.upc.caguilar.neurophone.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import pe.edu.upc.caguilar.neurophone.model.Imagen;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class GaleriaDAO {

    final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    private ArrayList<Imagen> lstImagen = new ArrayList<Imagen>();
    private String jpgPattern = ".jpg";
    private String pngPattern = ".png";

    public void GaleriaDAO(){}

    /*#############################################################################################*/
    public ArrayList<Imagen> listarImagenes() {

        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return lstImagen;
    }

    /*#############################################################################################*/
    private void scanDirectory(File directory) {

        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }

    /*#############################################################################################*/
    private void addSongToList(File file) {

        if (file.getName().startsWith("149"))
        return;

        if (file.getName().endsWith(jpgPattern) || file.getName().endsWith(pngPattern)) {

            Imagen img = new Imagen();
            img.setNombre(file.getName());
            img.setUrlFoto(file.getPath());

            String foto;
            Bitmap image_bitmap = null;
            image_bitmap = BitmapFactory.decodeFile(file.getPath());
            byte[] bArray = null;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            bArray = stream.toByteArray();
            foto = new String(Base64.encode(bArray,Base64.NO_WRAP)); //NO_CLOSE

            img.setFoto(foto);

            lstImagen.add(img);
        }
    }
}
