package pe.edu.upc.caguilar.neurophone.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class LlamadaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);

        Utility.currentActivity = this;
    }


    private void RealizarLlamada(String texto) {

        // LlamadaLlamar#############
        String numero = texto.substring(13);

        Utility.PrintDebug("Llamada","Llamar = " + numero, null);

        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Utility.PrintDebug("Llamada","Error Llamar = " + numero, null);
            return;
        }
        else{
            Utility.PrintDebug("Llamada","PreLlamada = " + numero, null);
            startActivity(in);
        }


    }

    public void RecibirMensaje(String texto) {

        //############################# DEBUG #############################\\
        Utility.PrintDebug("Llamada","Mensaje Recibido = " + texto, null);

        //############################# LLAMADA #############################\\
        if(texto.contains("Llamar"))
            RealizarLlamada(texto);

        //############################# GENERICOS #############################\\
        if(texto.equals("Menu")){
            Intent intent = new Intent(this,MainMenuActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("Llamada")){
            Intent intent = new Intent(this,LlamadaActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("Mensaje")){
            Intent intent = new Intent(this,MensajeActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("Contacto")){
            Intent intent = new Intent(this,ContactoActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
