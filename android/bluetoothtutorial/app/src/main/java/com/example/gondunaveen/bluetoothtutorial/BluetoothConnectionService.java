package com.example.gondunaveen.bluetoothtutorial;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "ttt";

    private static final UUID MY_UUID_INSECURE =
                UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;


    public BluetoothConnectionService(Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mContext = context;
        start();
    }

    /**This thread runs while listening to incoming connections. It behaves
     * lika a server-side client .It runs unti a connection is accepted or
     * until cancelled
     */
    private class AcceptThread extends Thread {

        //Local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp= null;

            //creating a listening server socket
            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
                Log.d(TAG, "Accept Thread : Setting up Server using :" + MY_UUID_INSECURE);
            }catch(IOException e) {
                Log.e(TAG, "AcceptThread : IOException: "+e.getMessage());
            }
            mmServerSocket = tmp;
        }
        public void run() {
            Log.d(TAG, "run : AcceptThread Running");

            BluetoothSocket socket = null;
            try {
                //It is a blocking call.It will only return on successful connection or exception
                Log.d(TAG, "run : RFCOM Server Socket start...");

                socket = mmServerSocket.accept();
                Log.e(TAG, "run : RFCOM server socket accepted connection");
            }catch (IOException e) {
                Log.e(TAG, "AcceptThread : IOException: "+e.getMessage());
            }

            if(socket != null) {
                connected(socket, mmDevice);
            }

            Log.i(TAG, "END mAcceptThread ");
        }

        public void cancel() {
            Log.d(TAG, "cancel : cancelling AcceptThread");
            try{
                mmServerSocket.close();
            }catch(IOException e) {
                Log.e(TAG, "cancel : Close of AcceptThread ServerSocket failed.." + e.getMessage());
            }
        }
    }

    /** This thread  runs while attempting to make an outgoing connection
     * with a device. It runs straight through ; the connection either succeeds or fails.
     */
    private class ConnectThread extends Thread {

        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
             Log.d(TAG, "Connect thread started....");
             mmDevice = device;
             deviceUUID = uuid;
        }
        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread ");

            //make a bluetooth socket for a connection with the given bluetooth device
            try{
                Log.d(TAG, "ConnectThread : Trying to create InsecurerRfcommSocket using UUID : "
                        + MY_UUID_INSECURE);
                 tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            }catch (IOException e){
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket "+e.getMessage());
            }

            mmSocket = tmp;

            //always cancel discovery when over
            mBluetoothAdapter.cancelDiscovery();

            //This is a blocking call .It will only return on successful connection or exception
            try {
                mmSocket.connect();

                Log.d(TAG, "ConnectThread connected");
            }catch(IOException e) {
                //close socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run : Closed socket");
                }catch(IOException e1) {
                    Log.e(TAG, "Unable to close connection in socket" + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectedThread: Could not connect to UUID: "+MY_UUID_INSECURE);
            }
            connected(mmSocket, mmDevice);
        }
        public void cancel() {
            try {
                Log.d(TAG, "cancel : Closing client Socket");
                mmSocket.close();
            }catch(IOException e) {
                Log.e(TAG, "cancel : close() of mmSocket in ConnecThread failed.."+e.getMessage());
            }
        }
    }



    /***start the chat/game service. Specifically start AcceptThread to begin a
     * session in listening mode. Called by Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "Start");

        //cancel threads attempting to make connections
        if (mConnectThread!=null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /***Accept thread starts and sits waiting for a connection .
     * Then  ConnectThread starts and attempts to make a connection with the other devices AcceptThread
     */

    public void startClient(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startClient: Started..");

        //initProgressDialog
        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth",
                "Please wait....", true);
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "Connected Thread starting...");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //dismiss the progess dialog box when connection is established.

            try {
                mProgressDialog.dismiss();
            }catch (NullPointerException e) {
                e.printStackTrace();
            }

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            }catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];

            int bytes;

            while(true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "Input Stream : " + incomingMessage);
                }catch(IOException e) {
                    Log.e(TAG, "write : error reading to input stream ." + e.getMessage());
                    break;
                }
            }
        }

        //call this function from main activity to send data to remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: writing to OutputStream : " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write : error writing to output stream ." + e.getMessage());
            }
        }


        /*call this function from main activity to shut down the connection */
        public void cancel() {
            try{
                mmSocket.close();
            } catch(IOException e) {  }
        }
    }
    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "Connected : starting");

        //start the  thread to manage the connections and transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    //write to connected thread unsynchronized manner
    public void write(byte[] out) {
        //create a temporary object
        ConnectedThread r;

        //Sync the copy of ConnectedThread
        Log.d(TAG, "write: write called");
        r = mConnectedThread;

        //Peform the write unsynchronized
        r.write(out);
    }


}
