package pe.edu.upc.caguilar.neurophone.util;

import android.app.Activity;
import android.os.AsyncTask;

import pe.edu.upc.caguilar.neurophone.activity.CamaraActivity;
import pe.edu.upc.caguilar.neurophone.activity.ContactoActivity;
import pe.edu.upc.caguilar.neurophone.activity.DocumentoActivity;
import pe.edu.upc.caguilar.neurophone.activity.EmergenciaActivity;
import pe.edu.upc.caguilar.neurophone.activity.GaleriaActivity;
import pe.edu.upc.caguilar.neurophone.activity.InternetActivity;
import pe.edu.upc.caguilar.neurophone.activity.LlamadaActivity;
import pe.edu.upc.caguilar.neurophone.activity.MainMenuActivity;
import pe.edu.upc.caguilar.neurophone.activity.MensajeActivity;
import pe.edu.upc.caguilar.neurophone.activity.RelojAlarmaActivity;
import pe.edu.upc.caguilar.neurophone.activity.ReproductorAudioActivity;

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

            //we create a TCPClient object
            tcpClient = new TcpClient(new TcpClient.OnMessageReceived() {

                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    //Toast.makeText(getApplicationContext(), "Mensaje Recibido", Toast.LENGTH_SHORT).show();
                    publishProgress(message);
                    //Log.d("Debug","Input message: " + message);
                }
            });
            tcpClient.run(ip);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
//            Log.d("#DESKTOPCONNECTION", "response " + values[0]);
            //process server response here....
            //TODO: Aca setear cuando se pierde la conexion y finalizar el socket

            //currently test methods for PoC purpose
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
                    case "LlamadaActivity":
                        LlamadaActivity objActivity2 = (LlamadaActivity) Utility.currentActivity;
                        objActivity2.RecibirMensaje(texto);
                        break;
                    case "ContactoActivity":
                        ContactoActivity objActivity3 = (ContactoActivity) Utility.currentActivity;
                        objActivity3.RecibirMensaje(texto);
                        break;
                    case "MensajeActivity":
                        MensajeActivity objActivity4 = (MensajeActivity) Utility.currentActivity;
                        objActivity4.RecibirMensaje(texto);
                        break;
                    case "EmergenciaActivity":
                        EmergenciaActivity objActivity5 = (EmergenciaActivity) Utility.currentActivity;
                        objActivity5.RecibirMensaje(texto);
                        break;
                    case "GaleriaActivity":
                        GaleriaActivity objActivity6 = (GaleriaActivity) Utility.currentActivity;
                        objActivity6.RecibirMensaje(texto);
                        break;
                    case "RelojAlarmaActivity":
                        RelojAlarmaActivity objActivity7 = (RelojAlarmaActivity) Utility.currentActivity;
                        objActivity7.RecibirMensaje(texto);
                        break;
                    case "CamaraActivity":
                        CamaraActivity objActivity8 = (CamaraActivity) Utility.currentActivity;
                        objActivity8.RecibirMensaje(texto);
                        break;
                    case "DocumentoActivity":
                        DocumentoActivity objActivity9 = (DocumentoActivity) Utility.currentActivity;
                        objActivity9.RecibirMensaje(texto);
                        break;
                    case "ReproductorAudioActivity":
                        ReproductorAudioActivity objActivity10 = (ReproductorAudioActivity) Utility.currentActivity;
                        objActivity10.RecibirMensaje(texto);
                        break;
                    case "InternetActivity":
                        InternetActivity objActivity11 = (InternetActivity) Utility.currentActivity;
                        objActivity11.RecibirMensaje(texto);
                        break;
                }

            }

            /*
            txtRecibido.setText("Recibido: " + texto);

            if(texto.equals("blink"))
                btnCursor.setY(btnCursor.getY() + 30);
            if(texto.equals("left"))
                btnCursor.setX(btnCursor.getX() - 30);
            if(texto.equals("right"))
                btnCursor.setX(btnCursor.getX() + 30);
            if(texto.equals("surprise"))
                btnCursor.setY(btnCursor.getY() - 30);
            */

            //TODO: Aca referenciar cada Activity con un switch y castear enviando el string comando del EE
            //if(Utility.getMyActivity() != null){
            //    MenuActivity x = (MenuActivity)Utility.getMyActivity();
            //    x.setRecibido(x.getLocalClassName());
            //}
        }
    }
}
