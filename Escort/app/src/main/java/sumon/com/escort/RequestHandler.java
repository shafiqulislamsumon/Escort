package sumon.com.escort;

import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestHandler {
    private String TAG = RequestHandler.class.getSimpleName();

    public String postRequestHandler(String requestUrl, JSONObject smsObject) throws UnsupportedEncodingException {

        URL url;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Encoding", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestMethod(RequestUrl.POST_METHOD);
            connection.setConnectTimeout(AppConstants.CONNECTION_TIMEOUT);
            connection.setReadTimeout(AppConstants.READ_TIMEOUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            String smsJsonStr = smsObject.toString();
            Log.i("Json String", smsJsonStr);

            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(smsJsonStr);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            int responseCode = connection.getResponseCode();
            Log.e(TAG, String.valueOf(responseCode));

            if (responseCode == 200){
/*                InputStream inputStream = new BufferedInputStream(connection.getInputStream());

                StringBuilder jsonResult = inputStreamToString(inputStream);
                JSONObject jsonResponse = new JSONObject(jsonResult.toString());
                Log.e("Data From JSON", jsonResponse.toString());*/
            }else{
                InputStream inputStream = new BufferedInputStream(connection.getErrorStream());
                Log.e("ERROR STREAM", inputStream.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public StringBuilder inputStreamToString(InputStream is) {
        String rLine;
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

    // Get Request Handler
    public String getRequestHandler(String requestUrl){
        // To Store response
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(requestUrl);
            // Open Connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Local
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                stringBuilder.append(result + "\n");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}