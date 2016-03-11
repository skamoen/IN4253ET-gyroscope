package nl.tudelft.superevilhackinglab;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divhax on 11/03/2016.
 */
public class Utils {
    public static void postToServer(final String serverUrl, final String paramKey, final String paramValue) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(serverUrl);
                List<NameValuePair> nvPair = new ArrayList<NameValuePair>();
                nvPair.add(new BasicNameValuePair(paramKey, paramValue));

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nvPair));
                    HttpResponse response = httpClient.execute(httpPost);
                    // write response to log
                    String responseString = new BasicResponseHandler().handleResponse(response);
                    Log.d("Http Post Response:", responseString);

                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        t.start();
    }
}
