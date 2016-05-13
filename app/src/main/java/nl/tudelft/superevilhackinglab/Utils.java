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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by divhax on 11/03/2016.
 */
public class Utils {
    public static void postToServer(final String serverUrl, final String paramKey,
                                    final String paramValue, final OperationEventHandler evtHandler) {
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
                    evtHandler.onSuccess();
                }
                catch (Exception ex) {
                    evtHandler.onFailure();
                }
            }
        });
        t.start();
    }
    public static void postToServer(final String serverUrl, final HashMap<String, String> params,
                                    final OperationEventHandler evtHandler) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(serverUrl);
                List<NameValuePair> nvPair = new ArrayList<NameValuePair>();

                for(String key : params.keySet()) {
                    nvPair.add(new BasicNameValuePair(key, params.get(key)));
                }

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nvPair));
                    HttpResponse response = httpClient.execute(httpPost);
                    // write response to log
                    String responseString = new BasicResponseHandler().handleResponse(response);
                    Log.d("Http Post Response:", responseString);
                    evtHandler.onSuccess();
                }
                catch (Exception ex) {
                    evtHandler.onFailure();
                }
            }
        });
        t.start();
    }


    public static String readFileContent(File theFile) throws Exception {
        FileInputStream fis = new FileInputStream(theFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        return sb.toString();
    }
}
