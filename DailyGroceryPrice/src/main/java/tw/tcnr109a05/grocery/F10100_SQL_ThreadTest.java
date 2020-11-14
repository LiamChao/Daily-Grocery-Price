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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class F10100_SQL_ThreadTest extends Thread {

    F10100 activity;
    private F10100_FriendDbHelper dbHper;
    private Handler mHandler;
    private ProgressBar mProBar;
    private JSONArray jsonArray;
    private JSONObject jsonData;
    private String title, pDate, link, description;    //OPENDATA STRING
    private String news_title, news_pDate, news_link, news_description;
    private int count = 0;

    public F10100_SQL_ThreadTest(F10100 activity, F10100_FriendDbHelper dbHper) {
        this.activity = activity;
        this.dbHper = dbHper;
    }

    @Override
    public void run() {
        super.run();

        try {
            String Task_opendata
                    = new TransTask().execute("https://data.coa.gov.tw/Service/OpenData/Agriculturalnews_agriRss.aspx").get();

            List<Map<String, Object>> mList;
            mList = new ArrayList<>();
            //-----解析json-----
            jsonArray = new JSONArray(Task_opendata);
            //-----------------
            int xx = jsonArray.length();  //Debug==> set if() to check, if xx.length=0, then it must rerun

            //------------json排序---------------------
            jsonArray = sortJsonArray(jsonArray);
            //----------------------------------------

            for (int i = 0; i < jsonArray.length(); i++) {
                /*{
                    "author": "2.16.886.101.20003.20024",
                    "title": "超過500萬國人登記農遊券  歡迎一起農遊消暑趣",
                    "link": "https://www.coa.gov.tw/theme_data.php?theme=news&sub_theme=agri&id=8151",
                    "description": " \r\r  行政院農業委員會配合行政院振興方案推出之500萬張農遊券，經國人熱情響應登記已超過512萬人，並於109年7月19日中午全數抽出，將於109年7月20日中午前發送票券資訊以簡訊給中籤之民眾，竭誠歡迎國人即日起拿著「三倍券+農遊券」，農林漁牧都好用，一起買農產、玩農村、遊漁港、吃農家菜，用行動支持台灣農業和農漁民。 \r\r國人熱情響應 登記抽籤結果  \r\r  農委會表示，500萬張「農遊券」登記抽籤活動自109年7月9日 15時開始，已於 109年7月18日 23:59 截止，感謝國人熱情響應登記，共有5,127,513人完成登記，有效抽籤人數為5,117,972人。扣除第一梯次已發送約165.1萬張券，餘近335萬張將於第二梯次抽出發送，第二梯次中籤率高達96.6%。中籤者之簡訊通知票券資訊部分，最晚將於109年7月20日中午前將發送完畢。有關登記結果、抽籤結果及申請驗證碼等各項查詢事項，民眾可於7月20日 中午12點起至活動網站（https://eticket.coa.gov.tw/Coupon/ContactService）查詢是否中籤。若您查詢有獲得農遊券，但於 7月20日 中午12點後仍未收到簡訊，除電信訊號問題外，可能是資料填寫有誤(如身分證號、手機號碼等)，請至該網站填寫客服表單透過email申請寄送驗證碼。 \r\r出遊小撇步 使用期限及注意事項 \r\r  農委會特別提醒民眾，農遊券需在簡訊通知後60日內使用，請務必注意使用期限，又因應第一梯次系統發送簡訊，部分有資訊不完整情形，已因應完成補發，並統一將第一梯次使用期限延長至109年9月17日止，與第二梯次相同。 \r\r  農委會建議民眾出遊前，先至農業易遊網【農遊券專區】(https://ezgo.coa.gov.tw/zh-TW/Front/ETicket/UseStore)查詢農遊券合作業者以利規劃行程，並注意要在居住地以外之跨鄉鎮市區才能消費抵用喔！目前全臺有超過2千家農林漁牧業者加入農遊券消費抵用合作業者，場域或類型包含希望廣場、花博市集、各地農夫市集、農漁會超市、農產品直銷站、休閒農場、休閒農業區、田媽媽班、農村社區、觀光漁港、娛樂漁船、森林遊樂區店家等據點，農委會歡迎符合資格業者踴躍參與，一起加入服務遊客抵用農遊券之行列。 \r\r  農委會提醒，民眾出遊到合作業者現場時，只需點開手機簡訊並連結至取票網站，輸入身分證號及簡訊上之驗證碼，即出現農遊券QR Code，並提供業者端掃描收券即可，消費可抵用包括門票、餐飲、合作業者場內套裝行程、農業體驗與農漁特產伴手禮等服務或商品，消費每滿250元即可抵用農遊券一張，一筆消費可以多張農遊券合併抵用，使用不分平假日但不找零，民眾也可以併同使用紙本或電子三倍券共同消費。 \r\r三倍券+農遊券 好康多更多 \r\r  農委會表示，使用三倍券、農遊券還有提供好康三重送優惠，第一重遊客到農業場域購買限定優惠商品，每滿1,500元，再加贈農遊券1張，限量送完為止；第二重使用農遊券消費加碼抽獎活動，獎品包含手機、兩人同行臺東一日遊、休閒農場住宿券等；第三重於部分農遊券合作業者場域消費，享滿千送百贈禮。所有精選優惠組合及好康資訊，將持續更新於農委會的農業易遊網「三倍券+農遊券」專區，鼓勵民眾趁著暑期規劃台灣農遊行程，與親朋好友共享好玩、好吃、好買的好時光。更多三倍券+農遊券優惠商品及農遊券領用資訊，歡迎民眾上農業易遊網的農遊券專區(https://ezgo.coa.gov.tw/zh-TW/Front/ETicket/Index)搜尋。 \r\r  農委會強調，希望國人一起使用農遊券加三倍券，增加農村旅遊商機及農產品銷售效益，該會預期帶動農業旅遊等產值超過50億元，加上三倍券的消費，可以幫農民、農村及農業帶來超過200億元以上產值。 \r\r附件：農遊券專區QR code及農遊券登記QR code(https://www.coa.gov.tw/files/news/8151/A01_1.pdf); ",
                    "pubDate": "Sat, 18 Jul 2020 16:00:00 GMT                                                                       ",
                    "cDate": "2020-07-19T23:32:01.433"
                  }*/
                jsonData = jsonArray.getJSONObject(i);
                title = jsonData.getString("title");
                pDate = jsonData.getString("cDate");
                link = jsonData.getString("link");
                description = jsonData.getString("description");
                //------Data insert SQLite----------------
                news_title = title;
                news_pDate = pDate;
                news_link = link;
                news_description = description;

                long rowID = dbHper.insertRec_F10101_news(news_title, news_pDate, news_link, news_description);
                //=========跑讀取條=================
                count++;
                mHandler.post(new Runnable() {
                    public void run() {
                        mProBar.setProgress(count * 10000 / jsonArray.length());
                    }
                });
                //================================
            }
//            activity.tHandler.sendEmptyMessage(1);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    void setHandler(Handler h) {
        mHandler = h;
    }

    void setProgressBar(ProgressBar bar01) {
        mProBar = bar01;
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
//                        try {
//                            lidDate = jsonOb1.getString("作物代號");
//                            ridDate = jsonOb2.getString("作物代號");
//                        } catch (JSONException jsone) {
//                            jsone.printStackTrace();
//                        }
                        return lidDate.compareTo(ridDate);
                    }
                }
        );
        return new JSONArray(json);
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
    }
}

