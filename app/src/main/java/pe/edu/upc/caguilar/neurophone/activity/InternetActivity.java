package pe.edu.upc.caguilar.neurophone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class InternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        Utility.currentActivity = this;
    }

    /*#############################################################################################*/
    public void RecibirMensaje(String texto) {

        /*############################# DEBUG #############################*/
        Utility.PrintDebug("InternetActivity","Mensaje Recibido = " + texto, null);

        /*############################# INTERNET #############################*/


        /*############################# GENERICOS #############################*/
        /*
        if(texto.equals("Menu")){
            Intent intent = new Intent(this,MainMenuActivity.class);
            startActivity(intent);
            finish();
        }*/

        if(texto.equals("LlamadaFocus")){
            Intent intent = new Intent(this,LlamadaActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("MensajeFocus")){
            Intent intent = new Intent(this,MensajeActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("ContactoFocus")){
            Intent intent = new Intent(this,ContactoActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("CamaraFocus")){
            Intent intent = new Intent(this,CamaraActivity.class);
            startActivity(intent);
            finish();
        }

//        if(texto.equals("ReproductorFocus")){
//            Intent intent = new Intent(this,ReproductorAudioActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        if(texto.equals("RelojFocus")){
//            Intent intent = new Intent(this,RelojAlarmaActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        if(texto.equals("DocumentoFocus")){
//            Intent intent = new Intent(this,DocumentoActivity.class);
//            startActivity(intent);
//            finish();
//        }

        if(texto.equals("InternetFocus")){
            Intent intent = new Intent(this,InternetActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("GaleriaFocus")){
            Intent intent = new Intent(this,GaleriaActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("EmergenciaFocus")){
            Intent intent = new Intent(this,EmergenciaActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
