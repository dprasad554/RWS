package tatwa.example.com.recyclerview.webservices;

import android.app.Application;
import android.content.Context;

import tatwa.example.com.recyclerview.receiver.ConnectivityReciever;


public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getmInstance(){
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    public void setConnectivityListener(ConnectivityReciever.ConnectivityReceiverListener listener) {
        ConnectivityReciever.connectivityReceiverListener = listener;
    }
}
