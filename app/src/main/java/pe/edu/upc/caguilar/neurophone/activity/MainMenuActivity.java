package pe.edu.upc.caguilar.neurophone.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.helper.ContactoHelper;
import pe.edu.upc.caguilar.neurophone.helper.LlamadaHelper;
import pe.edu.upc.caguilar.neurophone.helper.MensajeHelper;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class MainMenuActivity extends AppCompatActivity {

    private TextView txtTitulo;
    private ImageView ivFuncionalidad;
    private View vwActivity;

    /*#############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Utility.currentActivity = this;

        CapturarGUI();
        ModificarGUI();
    }

    /*#############################################################################################*/
    public void RecibirMensaje(String texto) {

        try {

            Utility.PrintDebug("MainMenu","Mensaje Recibido = " + texto, null);

            String categoria = texto.split("#;#;")[0];
            String accionTotal = texto.split("#;#;",2)[1];

            switch (categoria){

                case "Focus":
                    ActualizarFondo(accionTotal);
                    break;

                case "Llamada":
                    FuncionalidadLlamada(accionTotal);
                    break;

                case "Contacto":
                    FuncionalidadContacto(accionTotal);
                    break;

                case "Mensaje":
                    FuncionalidadMensaje(accionTotal);
                    break;

                case "Emergencia":
                    FuncionalidadEmergencia(accionTotal);
                    break;

            }
        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }

    /*#############################################################################################*/
    private void FuncionalidadEmergencia(String accionTotal) {

        try {  // Ejecutar#;#;986655532#;#;Hellooooo

            accionTotal = accionTotal + " --- http://maps.google.com/?q=-12.104349,-76.963782";

            MensajeHelper.EnviarSMS(accionTotal.split("#;#;", 2)[1]);

        } catch (Exception e) {
            Utility.PrintDebug("Catch", e.getMessage(), null);
        }
    }

    /*#############################################################################################*/
    private void FuncionalidadMensaje(String accionTotal) {

        try {

            String accion = accionTotal.split("#;#;")[0];

            switch (accion) {

                case "Listar": // Listar
                    MensajeHelper.ObtenerSMS();
                    break;
                case "Enviar": // Enviar#;#;986655532#;#;SMS
                    MensajeHelper.EnviarSMS(accionTotal.split("#;#;", 2)[1]);
                    break;
            }

        } catch (Exception e) {
            Utility.PrintDebug("Catch", e.getMessage(), null);
        }
    }

    /*#############################################################################################*/
    private void FuncionalidadContacto(String accionTotal) {

        try {

            String accion = accionTotal.split("#;#;")[0];

            switch (accion) {

                case "Listar": // Listar
                    ContactoHelper.ObtenerContactos();
                    break;
                case "Editar": // Editar#;#;ID#;#;Nombre#;#;Numero#;#;Email
                    ContactoHelper.EditarContacto(accionTotal.split("#;#;", 2)[1]);
                    break;
            }

        } catch (Exception e) {
            Utility.PrintDebug("Catch", e.getMessage(), null);
        }
    }

    /*#############################################################################################*/
    private void FuncionalidadLlamada(String accionTotal) {

        try {

            String accion = accionTotal.split("#;#;")[0];

            switch (accion){

                case "Llamar": // Llamar#;#;986655532
                    LlamadaHelper.RealizarLlamada(accionTotal.split("#;#;")[1]);
                    break;
                case "Cortar": // Cortar
                    LlamadaHelper.CortarLlamada();
                    break;
            }

        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }

    /*#############################################################################################*/
    private void ActualizarFondo(String accion) {

        try {

            Utility.PrintDebug("ActualizarFondo",accion, null);

            switch (accion){
                case "Llamada":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_llamada);
                    vwActivity.setBackgroundResource(R.color.colorLlamada);
                    break;
                case "Contacto":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_contacto);
                    vwActivity.setBackgroundResource(R.color.colorContacto);
                    break;
                case "Emergencia":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_emergencia);
                    vwActivity.setBackgroundResource(R.color.colorEmergencia);
                    break;
                case "Mensaje":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_mensaje);
                    vwActivity.setBackgroundResource(R.color.colorMensaje);
                    break;
                case "Galería":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_galeria);
                    vwActivity.setBackgroundResource(R.color.colorGaleria);
                    break;
                case "Email":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_email);
                    vwActivity.setBackgroundResource(R.color.colorEmail);
                    break;
                case "Cámara":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_camara);
                    vwActivity.setBackgroundResource(R.color.colorCamara);
                    break;
                case "Nota":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_nota);
                    vwActivity.setBackgroundResource(R.color.colorNota);
                    break;
                case "Reproductor":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_reproductor);
                    vwActivity.setBackgroundResource(R.color.colorReproductor);
                    break;
                case "Internet":
                    txtTitulo.setText(accion);
                    ivFuncionalidad.setImageResource(R.drawable.fondo_internet);
                    vwActivity.setBackgroundResource(R.color.colorInternet);
                    break;
            }

        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }

    /*#############################################################################################*/
    private void CapturarGUI() {

        txtTitulo = (TextView)findViewById(R.id.txtTitulo);
        ivFuncionalidad = (ImageView)findViewById(R.id.ivFuncionalidad);
        vwActivity = (View)findViewById(R.id.activity_main_menu);
    }

    /*#############################################################################################*/
    private void ModificarGUI() {

        //Modificación de tipo de letra TTF
        TextView myTextView = (TextView) findViewById(R.id.txtTitulo);
        Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");
        myTextView.setTypeface(typeface);
    }
}
