package pe.edu.upc.caguilar.neurophone.util;

import android.app.Activity;
import android.util.Log;

/**
 * Created by cagui on 4/11/2016.
 */

public class Utility {

    public static int splashScreenTimer = 5000; //5000
    public static int socketTimeOutTimer = 4000;
    //public static Context miContext;
    public static Activity currentActivity;

    public static String ipPC = "192.168.43.105"; //TODO: Abstraer esto de alguna forma, al igual que en la dekstop app
    public static int port = 200; //TODO: Abstraer esto de alguna forma, al igual que en la dekstop app


    /*######################### Variables Llamada ###################################*/
    public static boolean llamando = false;


    /*######################### Metodos Genericos Utiles ###################################*/
    public static void PrintDebug(String tag, String message, Throwable e){

        if(e == null)
            Log.d("#######-" + tag, message);
        else
            Log.d("#######-" + tag, message, e);
    }
}
