package pe.edu.upc.caguilar.neurophone.helper;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

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
                audioManager.setSpeakerphoneOn(true);
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
