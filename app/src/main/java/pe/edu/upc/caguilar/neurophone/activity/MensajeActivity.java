package pe.edu.upc.caguilar.neurophone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.List;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.dao.ContactoDAO;
import pe.edu.upc.caguilar.neurophone.model.Contacto;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class MensajeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        Utility.currentActivity = this;
    }

    /*#############################################################################################*/
    private void EnviarSMS(String texto){

        String numero = texto.split("#;#;")[1];
        String mensaje = texto.split("#;#;")[2];

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(numero, null, mensaje, null, null);
    }

    /*#############################################################################################*/
    private void ObtenerSMS(){

        ContactoDAO contactoDAO = new ContactoDAO();

        String listadoSMS = contactoDAO.listarSMS();

        /* SE EVITA USAR OBJETOS PARA DISMINUIR LA CARGA DE PROCESAMIENTO EN LA APP ANDROIDm
        String cadenaEnviar = "";

        for(int i=0; i<lstContactos.size(); i++){ //alContacts.size()
            if(i==25)
                break;
            cadenaEnviar = cadenaEnviar + lstContactos.get(i).getId() + "#;#;" + lstContactos.get(i).getNombre() + "#;#;" + lstContactos.get(i).getNumero() + "#;#;" + lstContactos.get(i).getEmail() + "#;#;" + lstContactos.get(i).getFoto() + "&;&;";
        }
        */
        String x = String.format("%x", new BigInteger(1, listadoSMS.getBytes(Charset.defaultCharset())));
        Utility.PrintDebug("MensajeActivity","Enviando listado", null);
        DesktopConnection.SendMessage(x);
    }

    /*#############################################################################################*/
    public void RecibirMensaje(String texto) {

        /*############################# DEBUG #############################*/
        Utility.PrintDebug("MensajeActivity","Mensaje Recibido = " + texto, null);

        /*############################# MENSAJE #############################*/
        if(texto.equals("SMSListar")){
            ObtenerSMS();
        }

        if(texto.contains("Enviar")){
            EnviarSMS(texto);
        }

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
