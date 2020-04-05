package sumon.com.escort;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

class GetRequestHandler extends AsyncTask<Void, Void, Void> {

    private ListAdapter adapter;
    private String TAG = GetRequestHandler.class.getSimpleName();
    ArrayList<HashMap<String, String>> smsList;

    GetRequestHandler(){
        smsList = new ArrayList<>();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        JsonParser sh = new JsonParser();

        // Making a request to url and getting response
        String jsonStr = sh.convertJson(RequestUrl.URL_GET_SMS);

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray smsArray = jsonObj.getJSONArray("result");

                // looping through All Contacts
                for (int i = 0; i < smsArray.length(); i++) {
                    JSONObject c = smsArray.getJSONObject(i);

                    String date = c.getString("date");
                    String mobile = c.getString("mobile");
                    String sms = c.getString("sms");

                    // tmp hash map for single sms
                    HashMap<String, String> smsData = new HashMap<>();

                    // adding each child node to HashMap key => value
                    smsData.put("date", date);
                    smsData.put("mobile", mobile);
                    smsData.put("sms", sms);

                    // adding contact to contact list
                    smsList.add(smsData);
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        for(HashMap map : smsList){
            String date = map.get("date").toString();
            String mobile = map.get("mobile").toString();
            String sms = map.get("sms").toString();

            Log.d("sumon", date);
            Log.d("sumon", mobile);
            Log.d("sumon", sms);
        }

    }

}