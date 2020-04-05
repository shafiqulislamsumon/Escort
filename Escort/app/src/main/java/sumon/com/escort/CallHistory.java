package sumon.com.escort;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallHistory {

    private Context context;
    private JSONArray callHistoryArray;
    private JSONObject callHistoryJsonObj;
    private SimpleDateFormat simpleDateFormat;
    private long lastUpdatedTime;

    CallHistory(Context context, long lastUpdatedTime){
        this.context = context;
        callHistoryArray = new JSONArray();
        callHistoryJsonObj = new JSONObject();
        this.lastUpdatedTime = lastUpdatedTime;
        simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_a", Locale.getDefault());
    }

    public JSONObject getCallHistory() {

        try {

            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){

                Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
                int numberColIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                int typeColIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
                int dateColIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
                int durationColIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

                while (cursor.moveToNext()) {

                    long dateTimeMillis = cursor.getLong(dateColIndex);
                    if (dateTimeMillis > lastUpdatedTime){

                        int typeCode = cursor.getInt(typeColIndex);
                        Date date = new Date(dateTimeMillis);
                        String callDate = simpleDateFormat.format(date);
                        String phoneNumber = cursor.getString(numberColIndex);
                        String callDuration = cursor.getString(durationColIndex);
                        String callType = "";

                        switch (typeCode) {
                            case CallLog.Calls.OUTGOING_TYPE:
                                callType = "OUTGOING";
                                break;
                            case CallLog.Calls.INCOMING_TYPE:
                                callType = "INCOMING";
                                break;
                            case CallLog.Calls.MISSED_TYPE:
                                callType = "MISSED";
                                break;
                        }

                        JSONObject callHistoryData = new JSONObject();
                        callHistoryData.put("type", callType);
                        callHistoryData.put("number", phoneNumber);
                        callHistoryData.put("date", callDate);
                        callHistoryData.put("duration", callDuration);

                        callHistoryArray.put(callHistoryData);
                    }
                }
                cursor.close();

                callHistoryJsonObj.put("CALLHISTORY", callHistoryArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return callHistoryJsonObj;
    }

}
