package sumon.com.escort;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChecker {

    private static Context context;
    private static NetworkChecker networkChecker = null;

    private NetworkChecker(){
        // Singleton pattern is applied
    }

    public static NetworkChecker getNetworkCheckerInstance(Context context){

        if(networkChecker == null){
            NetworkChecker.context = context;
            networkChecker = new NetworkChecker();
        }
        return networkChecker;
    }

    public boolean isNetworkAvailable(){

        boolean ret = false;
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            for (int networkType : networkTypes){
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType){
                    ret = true;
                }
            }
        }catch (Exception e){
            ret = false;
            e.printStackTrace();
        }

        return ret;
    }
}
