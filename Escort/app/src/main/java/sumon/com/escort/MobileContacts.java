package sumon.com.escort;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MobileContacts {
    private Context context;
    private JSONArray contactsArray;
    private JSONObject contactsJsonObj;
    private long lastUpdatedTime;

    MobileContacts(Context context, long lastUpdatedTime){
        this.context = context;
        contactsArray = new JSONArray();
        contactsJsonObj = new JSONObject();
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public JSONObject getContacts(){

        try {
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            while (cursor.moveToNext())
            {
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                long contactUpdateTime = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP));

                if (contactUpdateTime > lastUpdatedTime){

                    JSONObject contactData = new JSONObject();
                    contactData.put("name", name);
                    contactData.put("number", number);

                    contactsArray.put(contactData);
                }
            }
            cursor.close();

            contactsJsonObj.put("CONTACT", contactsArray);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return contactsJsonObj;
    }

}
