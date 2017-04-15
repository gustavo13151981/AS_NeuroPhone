package pe.edu.upc.caguilar.neurophone.helper;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

import static pe.edu.upc.caguilar.neurophone.util.Utility.llamando;

/**
 * Created by cagui on 15/11/2016.
 */

public class StatePhoneReceiver extends PhoneStateListener {

    Context context;

    public StatePhoneReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {

            case TelephonyManager.CALL_STATE_OFFHOOK: //Call is established
                Utility.PrintDebug("CALLSTATE", "OFFHOOK",null);
                //Activate loudspeaker
                AudioManager audioManager = (AudioManager)Utility.currentActivity.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(true);

                final int FOR_MEDIA = 1;
                final int FORCE_NONE = 0;
                final int FORCE_SPEAKER = 1;

                Class audioSystemClass = null;
                try {
                    audioSystemClass = Class.forName("android.media.AudioSystem");
                    Method setForceUse = audioSystemClass.getMethod("setForceUse", int.class, int.class);
                    setForceUse.invoke(null, FOR_MEDIA, FORCE_SPEAKER);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Utility.PrintDebug("Catch", "1",null);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Utility.PrintDebug("Catch", "1",null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Utility.PrintDebug("Catch", "1",null);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    Utility.PrintDebug("Catch", "1",null);
                }


                DesktopConnection.SendMessage("LlamadaLlamarOK");
                llamando = true;
                break;
            case TelephonyManager.CALL_STATE_IDLE: //Call is finished
                Utility.PrintDebug("CALLSTATE", "IDLE",null);
                if(Utility.llamando){
                    DesktopConnection.SendMessage("LlamadaCorto");
//                    TelephonyManager tManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//                    tManager.listen(this,PhoneStateListener.LISTEN_NONE);
                    Utility.llamando = false;
                }
                break;
        }
    }
}
