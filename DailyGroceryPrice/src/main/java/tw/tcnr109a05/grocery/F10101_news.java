package tw.tcnr109a05.grocery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class F10101_news extends AppCompatActivity implements View.OnClickListener {

    private static final String DB_FILE = "news.db";
    private static final int DBversion = 1;
    private TextView title;
    private ArrayList<String> recSet;
    private int index;
    private F10100_FriendDbHelper dbHper;
    private String news_title, news_content, news_link;

    private ProgressBar proBar;
    private ListView content;
    private ArrayList<Map<String, Object>> mList;
    private HashMap<String, Object> item;
    private SimpleAdapter adapter;
    private Button btn_url;
    private Uri uri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10103_news_detail);
        setUpviewComponent();
    }

    private void setUpviewComponent() {
        title = (TextView) findViewById(R.id.f10103_title);  //新聞標題
        //=======Set Content=======
        content = (ListView) findViewById(R.id.f10103_listView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 65 / 100; // 設定ScrollView使用尺寸的0.7
        content.getLayoutParams().height = newscrollheight;
        content.setLayoutParams(content.getLayoutParams()); // 重定ScrollView大小
        btn_url = (Button) findViewById(R.id.f10103_url); //超連結
        btn_url.setOnClickListener(this);
        proBar = (ProgressBar) findViewById(R.id.proBar2);
        proBar.setVisibility(View.INVISIBLE);
        //--------------------------------------------

        //-------連結資料庫-----------------
        dbHper = new F10100_FriendDbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet_news();

        //-----------------------從F10100接收資料----------------------------------
        Intent intent = this.getIntent();
        index = intent.getIntExtra("KEY_NEWS", 0);//getIntExtra後面要給個預設值避免取不到值
        //取得資料庫資料
        try {
            String[] fld = recSet.get(index).split("#");
                /*{
                    "author": "2.16.886.101.20003.20024",
                    "title": "超過500萬國人登記農遊券  歡迎一起農遊消暑趣",
                    "link": "https://www.coa.gov.tw/theme_data.php?theme=news&sub_theme=agri&id=8151",
                    "description": " \r\r  行政院農業委員會配合行政院振興方案推出之500萬張農遊券，經國人熱情響應登記已超過512萬人，並於109年7月19日中午全數抽出，將於109年7月20日中午前發送票券資訊以簡訊給中籤之民眾，竭誠歡迎國人即日起拿著「三倍券+農遊券」，農林漁牧都好用，一起買農產、玩農村、遊漁港、吃農家菜，用行動支持台灣農業和農漁民。 \r\r國人熱情響應 登記抽籤結果  \r\r  農委會表示，500萬張「農遊券」登記抽籤活動自109年7月9日 15時開始，已於 109年7月18日 23:59 截止，感謝國人熱情響應登記，共有5,127,513人完成登記，有效抽籤人數為5,117,972人。扣除第一梯次已發送約165.1萬張券，餘近335萬張將於第二梯次抽出發送，第二梯次中籤率高達96.6%。中籤者之簡訊通知票券資訊部分，最晚將於109年7月20日中午前將發送完畢。有關登記結果、抽籤結果及申請驗證碼等各項查詢事項，民眾可於7月20日 中午12點起至活動網站（https://eticket.coa.gov.tw/Coupon/ContactService）查詢是否中籤。若您查詢有獲得農遊券，但於 7月20日 中午12點後仍未收到簡訊，除電信訊號問題外，可能是資料填寫有誤(如身分證號、手機號碼等)，請至該網站填寫客服表單透過email申請寄送驗證碼。 \r\r出遊小撇步 使用期限及注意事項 \r\r  農委會特別提醒民眾，農遊券需在簡訊通知後60日內使用，請務必注意使用期限，又因應第一梯次系統發送簡訊，部分有資訊不完整情形，已因應完成補發，並統一將第一梯次使用期限延長至109年9月17日止，與第二梯次相同。 \r\r  農委會建議民眾出遊前，先至農業易遊網【農遊券專區】(https://ezgo.coa.gov.tw/zh-TW/Front/ETicket/UseStore)查詢農遊券合作業者以利規劃行程，並注意要在居住地以外之跨鄉鎮市區才能消費抵用喔！目前全臺有超過2千家農林漁牧業者加入農遊券消費抵用合作業者，場域或類型包含希望廣場、花博市集、各地農夫市集、農漁會超市、農產品直銷站、休閒農場、休閒農業區、田媽媽班、農村社區、觀光漁港、娛樂漁船、森林遊樂區店家等據點，農委會歡迎符合資格業者踴躍參與，一起加入服務遊客抵用農遊券之行列。 \r\r  農委會提醒，民眾出遊到合作業者現場時，只需點開手機簡訊並連結至取票網站，輸入身分證號及簡訊上之驗證碼，即出現農遊券QR Code，並提供業者端掃描收券即可，消費可抵用包括門票、餐飲、合作業者場內套裝行程、農業體驗與農漁特產伴手禮等服務或商品，消費每滿250元即可抵用農遊券一張，一筆消費可以多張農遊券合併抵用，使用不分平假日但不找零，民眾也可以併同使用紙本或電子三倍券共同消費。 \r\r三倍券+農遊券 好康多更多 \r\r  農委會表示，使用三倍券、農遊券還有提供好康三重送優惠，第一重遊客到農業場域購買限定優惠商品，每滿1,500元，再加贈農遊券1張，限量送完為止；第二重使用農遊券消費加碼抽獎活動，獎品包含手機、兩人同行臺東一日遊、休閒農場住宿券等；第三重於部分農遊券合作業者場域消費，享滿千送百贈禮。所有精選優惠組合及好康資訊，將持續更新於農委會的農業易遊網「三倍券+農遊券」專區，鼓勵民眾趁著暑期規劃台灣農遊行程，與親朋好友共享好玩、好吃、好買的好時光。更多三倍券+農遊券優惠商品及農遊券領用資訊，歡迎民眾上農業易遊網的農遊券專區(https://ezgo.coa.gov.tw/zh-TW/Front/ETicket/Index)搜尋。 \r\r  農委會強調，希望國人一起使用農遊券加三倍券，增加農村旅遊商機及農產品銷售效益，該會預期帶動農業旅遊等產值超過50億元，加上三倍券的消費，可以幫農民、農村及農業帶來超過200億元以上產值。 \r\r附件：農遊券專區QR code及農遊券登記QR code(https://www.coa.gov.tw/files/news/8151/A01_1.pdf); ",
                    "pubDate": "Sat, 18 Jul 2020 16:00:00 GMT ",
                    "cDate": "2020-07-19T23:32:01.433"
                  }*/
            news_title = fld[1];  //新聞標題
            news_link = fld[2]; //新聞連結
            news_content = fld[3]; //新聞內文
            //=====Set Adapter=====
            mList = new ArrayList<>();
            item = new HashMap<String, Object>();
            item.put("description", news_content);
            mList.add(item);
            adapter = new SimpleAdapter(
                    getApplicationContext(),
                    mList,
                    R.layout.f10104_news_content,
                    new String[]{"description"},
                    new int[]{R.id.f10104_news_content}
            );


        } catch (RuntimeException e) {
        }
//        //------------將接收資料傳到layout--------------------
        title.setText(news_title);
        content.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.end) {
            this.finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f10103_url:
                uri = Uri.parse(news_link);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), getString(R.string.onBackPressed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_end, menu);
        return true;
    }
}
