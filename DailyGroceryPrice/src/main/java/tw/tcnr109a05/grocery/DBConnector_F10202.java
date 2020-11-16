package tw.tcnr109a05.grocery;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DBConnector_F10202 {
    //宣告類別變數以方便存取，並判斷是否連線成功
    public static int httpstate = 0;
    static String result = null;
    static String TAG = "TCNR05=>";
    //---------------------------
    static InputStream is = null;
    static String line = null;
    static int code;
    static String mysql_code = null;


    //-------localhost-------
    static String connect_ip = ""; //localhost

    //--------------------------------------
    public static String executeQuery(String query_string) {

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(connect_ip + "android_connect_db.php");


            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query_string", query_string));
            // query_string -> 給php 使用的參數
            Log.d(TAG, "Value=" + params.toString());

            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();

            Log.d(TAG, "inputStream=" + inputStream.toString());

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
            Log.d(TAG, "result" + result);
        } catch (Exception e) {
            Log.d("TAG", "Exception e" + e.toString());
        }
        return result;
    }
}

