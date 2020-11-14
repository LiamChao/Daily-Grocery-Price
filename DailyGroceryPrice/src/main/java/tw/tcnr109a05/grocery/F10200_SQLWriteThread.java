package tw.tcnr109a05.grocery;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class F10200_SQLWriteThread extends Thread {

    F10200 activity;
    private FriendDbHelper_Price dbHper;
    private Handler mHandler;
    private ProgressBar mProBar;
    private JSONArray jsonArray;
    private JSONObject jsonData;
    private String c_date, num, crop, market, price;
    private String tNum, tCrop, tMarket, tPrice, tDate;
    private int count = 0;

    public F10200_SQLWriteThread(F10200 activity, FriendDbHelper_Price dbHper) {
        this.activity = activity;
        this.dbHper = dbHper;
    }

    @Override
    public void run() {
        super.run();

        try {
            String Task_opendata
                    = new TransTask().execute("https://data.coa.gov.tw/Service/OpenData/FromM/FarmTransData.aspx").get();

            //解析json
            jsonArray = new JSONArray(Task_opendata);
//                int xx = jsonArray.length();
            //---------------------------------------
//                jsonArray = sortJsonArray(jsonArray);
            //---------------------------------------
            //json排序
            //+表頭

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonData = jsonArray.getJSONObject(i);

                num = jsonData.getString("作物代號");
                crop = jsonData.getString("作物名稱");
                market = jsonData.getString("市場名稱");
                price = jsonData.getString("平均價");
                c_date = jsonData.getString("交易日期");

                //-------傳入SQLite-----------------
                tNum = num;
                tCrop = crop;
                tMarket = market;
                tPrice = price;
                tDate = c_date;
                long rowID = dbHper.insertRec_F10200(tNum, tCrop, tMarket, tPrice, tDate);
                //=========跑讀取條=================
                count++;
                mHandler.post(new Runnable() {
                    public void run() {
                        mProBar.setProgress(count * 100 / jsonArray.length());
                    }
                });
                //================================
            }
            activity.tHandler.sendEmptyMessage(1);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private JSONArray sortJsonArray(JSONArray jsonArray) {
        //county 自定義的排序method
        final ArrayList<JSONObject> json = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json.add(jsonArray.getJSONObject(i));
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }
//--------------------------------------------------------------------------------------------------
        Collections.sort(
                json, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonOb1, JSONObject jsonOb2) {
                        // 用多重key 排序
                        String lidDate = "", ridDate = "";
                        try {
                            lidDate = jsonOb1.getString("作物代號");
                            ridDate = jsonOb2.getString("作物代號");
                        } catch (JSONException jsone) {
                            jsone.printStackTrace();
                        }
                        return lidDate.compareTo(ridDate);
                    }
                }
        );
        return new JSONArray(json);
    }

    void setProgressBar(ProgressBar bar01) {
        mProBar = bar01;
    }

    void setHandler(Handler h) {
        mHandler = h;
    }

    private class TransTask extends AsyncTask<String, Void, String> {
        String ans;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while (line != null) {
                    Log.d("HTTP", line);
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ans = sb.toString();
            //------------
            return ans;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("s", "s:" + s);
            parseJson(s);
        }

        private void parseJson(String s) {

        }
    }
}
