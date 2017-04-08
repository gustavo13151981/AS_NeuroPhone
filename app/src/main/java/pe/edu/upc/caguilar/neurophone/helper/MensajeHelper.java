package pe.edu.upc.caguilar.neurophone.helper;

import android.telephony.SmsManager;

import java.math.BigInteger;
import java.nio.charset.Charset;

import pe.edu.upc.caguilar.neurophone.dao.ContactoDAO;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

/**
 * Created by cagui on 8/04/2017.
 */

public class MensajeHelper {

    /*#############################################################################################*/
    public static void EnviarSMS(String texto){

        try {

            String numero = texto.split("#;#;")[0];
            String mensaje = texto.split("#;#;")[1];

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numero, null, mensaje, null, null);

        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }

    /*#############################################################################################*/
    public static void ObtenerSMS(){

        try {

            ContactoDAO contactoDAO = new ContactoDAO();

            String listadoSMS = contactoDAO.listarSMS();

            // conversion HEX
            String x = String.format("%x", new BigInteger(1, listadoSMS.getBytes(Charset.defaultCharset())));
            Utility.PrintDebug("MensajeActivity","Enviando listado", null);
            DesktopConnection.SendMessage(x);

        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }
}
