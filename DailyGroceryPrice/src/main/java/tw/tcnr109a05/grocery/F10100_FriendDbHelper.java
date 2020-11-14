package tw.tcnr109a05.grocery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class F10100_FriendDbHelper extends SQLiteOpenHelper {
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    private static final String DB_FILE = "news.db";
    private static final String DB_TABLE = "news";    // 資料庫物件，固定的欄位變數
    private static final String DB_TABLE_anno = "anno";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql = "CREATE   TABLE   " + DB_TABLE + "   ( "
            + "num    INTEGER   PRIMARY KEY," + "title TEXT NOT NULL," + "link TEXT," + "description TEXT,"
            + "cdate DATETIME);";
    private static final String crTBsq_anno = "CREATE   TABLE   " + DB_TABLE_anno + "   ( "
            + "num    INTEGER   PRIMARY KEY," + "anno_title TEXT NOT NULL," + "anno_date TEXT," + "anno_content TEXT"
            + ");";
    private static SQLiteDatabase database;
    public String sCreateTableCommand;    // 資料庫名稱
    String TAG = "TCNR07=>";

    public F10100_FriendDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //傳入的參數說明
//		context: 用來開啟或建立資料庫的應用程式物件，如 Activity 物件
//		name: 資料庫檔案名稱，若傳入 null 表示將資料庫暫存在記憶體
//		factory: 用來建立指標物件的類別，若傳入 null 表示使用預設值
//		version: 即將要建立的資料庫版本 (版本編號從 1 開始)
//		        若資料庫檔案不存在，就會呼叫 onCreate() 方法
//		        若即將建立的資料庫版本比現存的資料庫版本新，就會呼叫 onUpgrade() 方法
//		        若即將建立的資料庫版本比現存的資料庫版本舊，就會呼叫 onDowngrade() 方法
//		errHandler: 當資料庫毀損時的處理程式，若傳入 null 表示使用預設的處理程式
        sCreateTableCommand = "";
    }

    //----------------------------------------------
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new F10100_FriendDbHelper(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql);
        db.execSQL(crTBsq_anno);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade()");//version改變的時候走這段

        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_anno);


        onCreate(db);
    }


    public int RecCount() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
//        recSet.close();
//        db.close();
        int a = recSet.getCount();
        int b = 0;
        recSet.close();
        return a;
//        return recSet.getCount();
    }

    public int RecCount_anno() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_anno;
        Cursor recSet = db.rawQuery(sql, null);
//        recSet.close();
//        db.close();
        int a = recSet.getCount();
        int b = 0;
        recSet.close();
        return a;
    }

    public ArrayList<String> getRecSet_news() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();

        //----------------------------
        Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
//        Log.d(TAG,"recAry="+recAry);
        return recAry;
    }

    public ArrayList<String> getRecSet_anno() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_anno;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();

        //----------------------------
        Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
//        Log.d(TAG,"recAry="+recAry);
        return recAry;
    }

    public void createTB() {
        //批次新增
        int maxRecord = 50;
        SQLiteDatabase db = getWritableDatabase();
        for (int i = 0; i < maxRecord; i++) {
            ContentValues newRow = new ContentValues();
            newRow.put("name", "路人" + u_chinaYear(i));
            newRow.put("grp", "第" + u_chinaYear((int) (Math.random() * 4 + 1)) + "組");
            newRow.put("address", "總統府一路" + u_chinaYear(i) + "號");
            db.insert(DB_TABLE, null, newRow);
        }
        db.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    private String u_chinaYear(int i) {
        String c_number = "";
        String china_year[] = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
        c_number = china_year[i % 10];
        return c_number;
    }

    public long insertRec_F10101_news(String b_title, String b_pDate, String b_link, String b_description) {
//        news_title, news_pDate, news_link, news_description
        SQLiteDatabase db = getWritableDatabase();
        ContentValues res = new ContentValues();
//        res.put("MemberID", b_MemberID);
//        res.put("Email", b_Email);
        res.put("title", b_title);
        res.put("cdate", b_pDate);
        res.put("link", b_link);
        res.put("description", b_description);
        long rowID = db.insert(DB_TABLE, null, res);
        db.close();

        return rowID;
    }

    public long insertRec_F10101_anno(String a_num, String a_title, String a_date, String a_content) {
//        news_title, news_pDate, news_link, news_description
        SQLiteDatabase db = getWritableDatabase();
        ContentValues res = new ContentValues();
//        res.put("MemberID", b_MemberID);
//        res.put("Email", b_Email);
        res.put("num", a_num);
        res.put("anno_title", a_title);
        res.put("anno_date", a_date);
        res.put("anno_content", a_content);
        long rowID = db.insert(DB_TABLE_anno, null, res);
        db.close();

        return rowID;
    }
}
