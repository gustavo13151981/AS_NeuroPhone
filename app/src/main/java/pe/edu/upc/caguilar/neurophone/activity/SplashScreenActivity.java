package pe.edu.upc.caguilar.neurophone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Utility.currentActivity = this;

       // LoadSavedVariables();

        InitializeDesktopConnection();

        StartSplashScreenTimer();
    }

    private void LoadSavedVariables() {

        try {
            if(sharedPreferences == null) {
                sharedPreferences = getSharedPreferences("Variables", MODE_PRIVATE);
            }
            String ip = sharedPreferences.getString("ip", "");

            Utility.ipPC = ip;
        }
        catch (Exception e)
        {

        }
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
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(Utility.currentActivity);
                    builder.setTitle("Nueva Dirección IP");

                    final EditText input = new EditText(Utility.currentActivity);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String ip = input.getText().toString();

                            if(sharedPreferences != null) {
                                // Opens Preferences file for edition
                                SharedPreferences.Editor e = sharedPreferences.edit();
                                e.putString("ip", ip);
                                e.commit();
                            }

                            finish();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });

                    builder.show();

                    /*
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Utility.currentActivity);

                    final EditText et = new EditText(Utility.currentActivity);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(et);

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    */
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
