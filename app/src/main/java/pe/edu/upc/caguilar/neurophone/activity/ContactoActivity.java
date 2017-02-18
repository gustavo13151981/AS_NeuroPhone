package pe.edu.upc.caguilar.neurophone.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.dao.ContactoDAO;
import pe.edu.upc.caguilar.neurophone.model.Contacto;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

import static android.R.attr.max;

public class ContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        Utility.currentActivity = this;
    }

    /*#############################################################################################*/
    private void EditarContacto(String texto) {

        Contacto objContacto = new Contacto();

        String[] arrayTexto = texto.split("#;#;");
        objContacto.setId(arrayTexto[1]);
        objContacto.setNombre(arrayTexto[2]);
        objContacto.setNumero(arrayTexto[3]);
        objContacto.setEmail(arrayTexto[4]);

        ContactoDAO contactoDAO = new ContactoDAO();
        contactoDAO.actualizarContacto(objContacto);
    }

    /*#############################################################################################*/
    private void ObtenerContactos(){

        ContactoDAO contactoDAO = new ContactoDAO();

        List<Contacto> lstContactos = contactoDAO.listarContactos();

        String cadenaEnviar = "";

        for(int i=0; i<lstContactos.size(); i++){ //alContacts.size()
            if(i==25)
                break;
            cadenaEnviar = cadenaEnviar + lstContactos.get(i).getId() + "#;#;" + lstContactos.get(i).getNombre() + "#;#;" + lstContactos.get(i).getNumero() + "#;#;" + lstContactos.get(i).getEmail() + "#;#;" + lstContactos.get(i).getFoto() + "&;&;";
        }
//        cadenaEnviar.replaceAll("\r\n","");
        DesktopConnection.SendMessage(cadenaEnviar);

    }

    /*#############################################################################################*/
    public void RecibirMensaje(String texto) {

        /*############################# DEBUG #############################*/
        Utility.PrintDebug("ContactoActivity","Mensaje Recibido = " + texto, null);

        /*############################# CONTACTO #############################*/

        if(texto.equals("ContactosListar")){
            ObtenerContactos();
        }

        if(texto.contains("Editar")){
            EditarContacto(texto);
        }


        /*############################# GENERICOS #############################*/
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
