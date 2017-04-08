package pe.edu.upc.caguilar.neurophone.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by cagui on 4/11/2016.
 */

public class TcpClient {

    private Socket socket = null;
    // message to send to the server
    private String mServerMessage = "";
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            //mBufferOut.println(message);
            Utility.PrintDebug("TcpClient","Enviando (" + message.length() + "): " + message, null);
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    public void stopClient() {

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run(String serverIP) {

        mRun = true;

        try {

            Utility.PrintDebug("TcpClient", "Initializing connection", null);

            try {
                SocketAddress socketAddress = new InetSocketAddress(Utility.ipPC,Utility.port);
                Utility.PrintDebug("TcpClient", "Connecting " + Utility.ipPC, null);
                socket = new Socket();
                socket.connect(socketAddress,Utility.socketTimeOutTimer);

                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                sendMessage("OK");
                DesktopConnection.connected = true;

                while (mRun) {

                    mServerMessage = mBufferIn.readLine();
                        if (mServerMessage != null && mMessageListener != null) {
                            if(mServerMessage.equals("EXIT"))
                                break;
                            else{
                                mMessageListener.messageReceived(mServerMessage);
                                mServerMessage = "";
                            }
                        }
                }

                Utility.PrintDebug("TcpClient", "Conexion Finalizada EXIT", null);

            } catch (Exception e) {
                Utility.PrintDebug("TcpClient", "Socket Exception " + e.getMessage(), null);
            } finally {
                socket.close();
                //Utility.currentActivity.finish();
            }

        } catch (Exception e) {

            Utility.PrintDebug("TcpClient", "Socket Final Exception " + e.getMessage(), null);
            Utility.PrintDebug("TcpClient",Utility.currentActivity.getLocalClassName(), null);
            Utility.currentActivity.finish();
        }
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
