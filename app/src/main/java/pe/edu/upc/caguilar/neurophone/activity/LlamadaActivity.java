package pe.edu.upc.caguilar.neurophone.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class LlamadaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);

        Utility.currentActivity = this;
    }

    /*#############################################################################################*/
    private void RealizarLlamada(String texto){

        // LlamadaLlamar#############
        String numero = texto.substring(13);

        Utility.PrintDebug("LlamadaActivity","Llamar = " + numero, null);

        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));

        //TODO: revisar por que no sale el popup de pedir permiso..
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},1);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Utility.PrintDebug("LlamadaActivity","Error Llamar = " + numero + " No aceptó permisos", null);
            return;
        }
        else{

            Utility.PrintDebug("LlamadaActivity","Llamando = " + numero, null);
            startActivity(in);

            //Variables para el loop de CALL_STATE
            Boolean llamando = false;
            int contadorIdle = 0;  //usualmente tarda 1 segundo en entrar al OFFHOOK, pero se usara 3 segds x si falla
            while(true) {

                TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                int callState = tm.getCallState();

                //LOOP para captar cuando se realice la llamada e inicie el SPEAKER
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                if(callState == TelephonyManager.CALL_STATE_IDLE){
//                    Utility.PrintDebug("LlamadaActivity", "CALL_STATE_IDLE",null);
//                    contadorIdle++;
//                    if(llamando){
//                        DesktopConnection.SendMessage("LlamadaCorto");
//                        break;
//                    }else if(contadorIdle==6){
//                        DesktopConnection.SendMessage("LlamadaCortoPosibleFalla");
//                        break;
//                    }
//                }
                if(callState == TelephonyManager.CALL_STATE_OFFHOOK){
                    Utility.PrintDebug("LlamadaActivity", "CALL_STATE_OFFHOOK",null);
//                    if(!llamando){
                        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        audioManager.setMode(AudioManager.MODE_IN_CALL);
                        audioManager.setSpeakerphoneOn(true);
                        DesktopConnection.SendMessage("LlamadaLlamarOK");
                        break;
//                        llamando = true;
//                    }
                }
                //if(tm.getCallState() == TelephonyManager.CALL_STATE_RINGING){
                //    Utility.PrintDebug("LlamadaActivity", "CALL_STATE_RINGING",null);
                //}
            }

            //DesktopConnection.SendMessage("LlamadaLlamarOK");
        }
    }

    /*#############################################################################################*/
    private void CortarLlamada(){

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

            Utility.PrintDebug("LlamadaActivity","Entro Cortar", null);
            TelephonyManager tm = (TelephonyManager)this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
            c = Class.forName(telephonyService.getClass().getName()); // Get its class
            m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
            m.setAccessible(true); // Make it accessible
            m.invoke(telephonyService); // invoke endCall()
            Utility.PrintDebug("LlamadaActivity","Termino Cortar", null);

        }catch (Exception e){
            Utility.PrintDebug("LlamadaActivity","Exception Cortar", null);
        }
    }

    /*#############################################################################################*/
    public class StatePhoneReceiver extends PhoneStateListener{

        Context context;
        public StatePhoneReceiver(Context context) {
            this.context = context;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {

                case TelephonyManager.CALL_STATE_OFFHOOK: //Call is established

                        try {
                            Thread.sleep(500); // Delay 0,5 seconds to handle better turning on loudspeaker
                        } catch (InterruptedException e) {
                        }

                        //Activate loudspeaker
                        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        audioManager.setMode(AudioManager.MODE_IN_CALL);
                        audioManager.setSpeakerphoneOn(true);

                    break;

                case TelephonyManager.CALL_STATE_IDLE: //Call is finished

                        AudioManager audioManager1 = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        audioManager1.setMode(AudioManager.MODE_NORMAL); //Deactivate loudspeaker
//                        manager.listen(myPhoneStateListener, // Remove listener
//                                PhoneStateListener.LISTEN_NONE);

                    break;
            }
        }
    }

    /*#############################################################################################*/
    public void RecibirMensaje(String texto) {

        /*############################# DEBUG #############################*/
        Utility.PrintDebug("LlamadaActivity","Mensaje Recibido = " + texto, null);

        /*############################# LLAMADA #############################*/
        if(texto.contains("Llamar"))
            RealizarLlamada(texto);

        if(texto.contains("Cortar"))
            CortarLlamada();

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

        if(texto.equals("ReproductorFocus")){
            Intent intent = new Intent(this,ReproductorAudioActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("RelojFocus")){
            Intent intent = new Intent(this,RelojAlarmaActivity.class);
            startActivity(intent);
            finish();
        }

        if(texto.equals("DocumentoFocus")){
            Intent intent = new Intent(this,DocumentoActivity.class);
            startActivity(intent);
            finish();
        }

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
