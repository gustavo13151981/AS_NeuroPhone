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

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            //mBufferOut.println(message);
            Utility.PrintDebug("TcpClient","Enviando (" + message.length() + "): " + message, null);
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    /**
     * Close the connection and release the members
     */
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
            //here you must put your computer's IP address.
            //InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            InetAddress serverAddr = InetAddress.getByName(serverIP);

            Utility.PrintDebug("TcpClient", "Initializing connection", null);

            //create a socket to make the connection with the server
//            Socket socket = null;

            try {
                SocketAddress socketAddress = new InetSocketAddress(Utility.ipPC,Utility.port);
                //socket = new Socket(serverAddr, SERVER_PORT);
                socket = new Socket();
                socket.connect(socketAddress,Utility.socketTimeOutTimer);
                //socket.connect(serverAddr,serverAddr);
                //sends the message to the server

//              SplashScreenActivity objActivity1 = (SplashScreenActivity) Utility.currentActivity;
//              objActivity1.recibirMensaje("OK");

                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //sends a message to the server as an SUCCESS
                sendMessage("OK");
                //sets connected to true so splashScreen can switch to MainMenu
                DesktopConnection.connected = true;

                //in this while the client listens for the messages sent by the server
                while (mRun) {

                    mServerMessage = mBufferIn.readLine();
                    //if(!mBufferIn.ready()){
                        if (mServerMessage != null && mMessageListener != null) {
                            //call the method messageReceived from MyActivity class
                            if(mServerMessage.equals("EXIT"))
                                break;
                            else{
                                mMessageListener.messageReceived(mServerMessage);
                                mServerMessage = "";
                            }
                        }
                    //}
                    //mServerMessage += Character.toString((char)mBufferIn.read());
                }

                Utility.PrintDebug("TcpClient", "Conexion Finalizada EXIT", null);
            } catch (Exception e) {
                Utility.PrintDebug("TcpClient", "Socket Exception", null);
            } finally {
                socket.close();
                //Utility.currentActivity.finish();
            }

        } catch (Exception e) {

            Utility.PrintDebug("TcpClient", "Socket Final Exception", null);
            Utility.currentActivity.finish();
            Utility.PrintDebug("TcpClient",Utility.currentActivity.getLocalClassName(), null);
        }
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
