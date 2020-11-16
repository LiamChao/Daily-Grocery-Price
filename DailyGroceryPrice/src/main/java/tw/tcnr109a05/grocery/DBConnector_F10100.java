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

public class DBConnector_F10100 {
    private static String Target = "TCNR07==>";


    public static String executeQuery(String query_string) {
        String result = "";

        try {
            HttpClient httpClient = new DefaultHttpClient();  //設定一個Client端
//----------自家-------
            HttpPost httpPost = new HttpPost("");
//=======================================

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query_string", query_string)); //要跟PHP裡的KEY值一樣
            // query_string -> 給php 使用的參數
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost); //真正執行於此行
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent(); //接收值
            //-------------------------

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();

        } catch (Exception e) {
            Log.d(Target, "Exception e" + e.toString());
        }
        return result;
    }
}
