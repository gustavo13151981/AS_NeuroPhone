package pe.edu.upc.caguilar.neurophone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Utility.currentActivity = this;

        InitializeDesktopConnection();

        StartSplashScreenTimer();
    }

    private void StartSplashScreenTimer() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(DesktopConnection.connected){
                    Intent intent = new Intent(SplashScreenActivity.this,LlamadaActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(SplashScreenActivity.this, "Error de conexion a Desktop", Toast.LENGTH_LONG).show();
                }
            }
        }, Utility.splashScreenTimer);
    }

    private void InitializeDesktopConnection() {

        DesktopConnection.ip = Utility.ipPC;
        DesktopConnection.Conectar();
    }

    /*
   public void btnConectar(View view){

        DesktopConnection.ip = txtIP.getText().toString();
        DesktopConnection.Conectar();

    }

    public void recibirMensaje(String texto){

        txtRecibido.setText("Recibido: " + texto);

        if(texto.equals("blink"))
            btnCursor.setY(btnCursor.getY() + 30);
        if(texto.equals("left"))
            btnCursor.setX(btnCursor.getX() - 30);
        if(texto.equals("right"))
            btnCursor.setX(btnCursor.getX() + 30);
        if(texto.equals("surprise"))
            btnCursor.setY(btnCursor.getY() - 30);
    }
     */
}
