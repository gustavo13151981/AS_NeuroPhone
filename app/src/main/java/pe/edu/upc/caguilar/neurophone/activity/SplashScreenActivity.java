package pe.edu.upc.caguilar.neurophone.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import pe.edu.upc.caguilar.neurophone.R;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class SplashScreenActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;

    private static final int MY_PERMISSIONS_REQUEST_MULTIPLE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Utility.currentActivity = this;

        LoadPermissionesRequest();

    }

    private void LoadPermissionesRequest() {

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_MULTIPLE);

    }

    private void LoadSavedVariables() {

        try {
            if(sharedPreferences == null) {
                sharedPreferences = getSharedPreferences("Variables", MODE_PRIVATE);
            }
            String ip = sharedPreferences.getString("ip", "");

            if(ip.equals(""))
                Utility.ipPC = "192.168.2.20";
            else
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
                    input.setText("192.168.2.20");
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
                }
            }
        }, Utility.splashScreenTimer);
    }

    private void InitializeDesktopConnection() {

        DesktopConnection.ip = Utility.ipPC;
        DesktopConnection.Conectar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            Utility.PrintDebug("PERMISOS","Permisos OKIS", null);
            LoadSavedVariables();
            InitializeDesktopConnection();
            StartSplashScreenTimer();
        }
        else{
            Utility.PrintDebug("PERMISOS","Falto permisos", null);
            Toast.makeText(SplashScreenActivity.this, "Se necesitan los permisos. Cerrando..", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(3000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
