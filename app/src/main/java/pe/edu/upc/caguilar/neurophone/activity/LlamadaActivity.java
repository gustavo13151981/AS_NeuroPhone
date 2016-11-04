package pe.edu.upc.caguilar.neurophone.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

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

    private void CortarLlamada(String texto){

        try {
            /*
                Actually, adding ITelephony.aidl to your project isn't necessary, it is just a convenience. You could just as well do it this way:

                TelephonyManager tm = (TelephonyManager) context
                            .getSystemService(Context.TELEPHONY_SERVICE);
                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
                c = Class.forName(telephonyService.getClass().getName()); // Get its class
                m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
                m.setAccessible(true); // Make it accessible
                m.invoke(telephonyService); // invoke endCall()
                Under the covers this all works using Java reflection to access private
                (ie: not publicly documented) methods. You can figure out what methods are there, and what
                 they do, by reading the open-source (ie: publicly available) Android source code.
                 Once you know what is there and what it does, you can use reflection to get to it, even though it is "hidden".
              */

            Utility.PrintDebug("Llamada","Entro Cortar", null);
            TelephonyManager tm = (TelephonyManager)this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
            c = Class.forName(telephonyService.getClass().getName()); // Get its class
            m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
            m.setAccessible(true); // Make it accessible
            m.invoke(telephonyService); // invoke endCall()
            Utility.PrintDebug("Llamada","Termino Cortar", null);

        }catch (Exception e){
            Utility.PrintDebug("Llamada","Exception Cortar", null);
        }


    }

    public void RecibirMensaje(String texto) {

        //############################# DEBUG #############################\\
        Utility.PrintDebug("Llamada","Mensaje Recibido = " + texto, null);

        //############################# LLAMADA #############################\\
        if(texto.contains("Llamar"))
            RealizarLlamada(texto);

        if(texto.contains("Cortar"))
            CortarLlamada(texto);

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
