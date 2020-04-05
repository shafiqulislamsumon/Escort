package sumon.com.escort;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
/*

        EscortDBHandler escortDBHandler = new EscortDBHandler(context);
        long newUpdatedTime = System.currentTimeMillis();
        escortDBHandler.insertIntoUpdatedTimeTable(newUpdatedTime);
        long lastUpdatedTime = escortDBHandler.getLastUpdatedTime();
        Toast.makeText(context, "alarm receiver DB time : " + lastUpdatedTime, Toast.LENGTH_SHORT).show();
*/

        //Toast.makeText(context, "alarm receiver ", Toast.LENGTH_SHORT).show();
        //PostRequestHandler postRequestHandler = new PostRequestHandler(context);
        //postRequestHandler.execute();


        SoundCapture soundCapture = SoundCapture.getSoundCaptureInstance(context);
        soundCapture.startSoundCaptureProcess();
    }

}
