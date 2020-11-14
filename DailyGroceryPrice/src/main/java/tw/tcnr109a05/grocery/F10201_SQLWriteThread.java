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

public class F10201_SQLWriteThread extends Thread {

    F10201 activity;
    private FriendDbHelper_Price dbHper;
    private Handler mHandler;
    private ProgressBar mProBar;
    private JSONArray jsonArray;
    private JSONObject jsonData;
    private String chicken200, chicken195, chicken_retail, egg, p_date;
    private String tChicken200, tChicken195, tChicken_retail, tEgg, tDate;
    private int count = 0;

    public F10201_SQLWriteThread(F10201 activity, FriendDbHelper_Price dbHper) {
        this.activity = activity;
        this.dbHper = dbHper;
    }

    @Override
    public void run() {
        super.run();

        try {
            String Task_opendata
                    = new TransTask().execute("https://data.coa.gov.tw/Service/OpenData/FromM/PoultryTransBoiledChickenData.aspx").get();

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

                chicken200 = jsonData.getString("白肉雞(2.0Kg以上)");
                chicken195 = jsonData.getString("白肉雞(1.75-1.95Kg)");
                chicken_retail = jsonData.getString("白肉雞(門市價高屏)");
                egg = jsonData.getString("雞蛋(產地)");
                p_date = jsonData.getString("日期");

                //-------傳入SQLite-----------------
                tChicken200 = chicken200;
                tChicken195 = chicken195;
                tChicken_retail = chicken_retail;
                tEgg = egg;
                tDate = p_date;
                long rowID = dbHper.insertRec_F10201(tChicken200, tChicken195, tChicken_retail, tEgg, tDate);
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
                            lidDate = jsonOb1.getString("日期");
                            ridDate = jsonOb2.getString("日期");
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
