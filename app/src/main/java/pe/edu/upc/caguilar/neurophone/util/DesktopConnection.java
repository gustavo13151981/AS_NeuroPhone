package pe.edu.upc.caguilar.neurophone.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.telephony.SmsManager;

import pe.edu.upc.caguilar.neurophone.activity.CamaraActivity;
import pe.edu.upc.caguilar.neurophone.activity.ContactoActivity;
import pe.edu.upc.caguilar.neurophone.activity.EmergenciaActivity;
import pe.edu.upc.caguilar.neurophone.activity.GaleriaActivity;
import pe.edu.upc.caguilar.neurophone.activity.InternetActivity;
import pe.edu.upc.caguilar.neurophone.activity.LlamadaActivity;
import pe.edu.upc.caguilar.neurophone.activity.MainMenuActivity;
import pe.edu.upc.caguilar.neurophone.activity.MensajeActivity;

/**
 * Created by cagui on 4/11/2016.
 */

public class DesktopConnection {

    public static TcpClient tcpClient;
    public static String ip;
    public static Boolean connected = false;

    public static void Conectar(){

        new ConnectTask().execute();
    }

    public static void SendMessage(String message){

        tcpClient.sendMessage(message);
    }

    public static class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            tcpClient = new TcpClient(new TcpClient.OnMessageReceived() {

                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    publishProgress(message);
                }
            });

            tcpClient.run(ip);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            String texto = values[0];

            Utility.PrintDebug("DesktopConnection","Mensaje Sockets Recibido = " + texto, null);

            if(Utility.currentActivity != null) {

                Activity x = Utility.currentActivity;
                String activityName = x.getLocalClassName();
                activityName = activityName.substring(9);

                switch(activityName){
                    case "MainMenuActivity":
                        MainMenuActivity objActivity1 = (MainMenuActivity) Utility.currentActivity;
                        objActivity1.RecibirMensaje(texto);
                        break;
                    case "CamaraActivity":
                        CamaraActivity objActivity2 = (CamaraActivity) Utility.currentActivity;
                        objActivity2.RecibirMensaje(texto);
                        break;
                }
            }
        }
    }
}
