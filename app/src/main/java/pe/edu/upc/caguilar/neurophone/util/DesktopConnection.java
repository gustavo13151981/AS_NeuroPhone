package pe.edu.upc.caguilar.neurophone.util;

import android.app.Activity;
import android.os.AsyncTask;

import pe.edu.upc.caguilar.neurophone.activity.ContactoActivity;
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
