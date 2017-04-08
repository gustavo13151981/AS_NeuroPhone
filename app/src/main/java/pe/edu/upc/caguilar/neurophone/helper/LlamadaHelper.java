package pe.edu.upc.caguilar.neurophone.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

/**
 * Created by cagui on 7/04/2017.
 */

public class LlamadaHelper {

    private static Activity act = Utility.currentActivity;

    /*#############################################################################################*/
    public static void RealizarLlamada(String numero){

        try{

            Utility.PrintDebug("LlamadaHelper","Llamar = " + numero, null);

            Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));

            if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                Utility.PrintDebug("LlamadaActivity","Error Llamar = " + numero + " No aceptó permisos", null);
                DesktopConnection.SendMessage("LlamadaErrorPermiso");
            }
            else{

                Utility.PrintDebug("LlamadaHelper","Llamando = " + numero, null);
                act.startActivity(in);

                TelephonyManager tManager = (TelephonyManager) act.getSystemService(act.TELEPHONY_SERVICE);
                tManager.listen(new StatePhoneReceiver(act), PhoneStateListener.LISTEN_CALL_STATE);
            }
        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }

    /*#############################################################################################*/
    public static void CortarLlamada() {

        try {

            Utility.PrintDebug("LlamadaActivity","Entro Cortar", null);
            TelephonyManager tm = (TelephonyManager)act.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
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
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }
}
