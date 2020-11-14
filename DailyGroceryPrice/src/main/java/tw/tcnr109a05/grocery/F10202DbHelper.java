package tw.tcnr109a05.grocery;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class F10202DbHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    private static final DatabaseErrorHandler errorHandler = null;
    private static final Context context = null;
    private static final String DB_FILE = "F10202.db";    // 資料庫名稱
    private static final String DB_TABLE = "fish";    // 資料表名稱
    private static final String crTBsql = "CREATE TABLE " + DB_TABLE + " ( "
            + "ID INTEGER PRIMARY KEY," + "Fish TEXT ," + "Market TEXT," + "Avgprice DECIMAL," + "Tradedate TEXT);";    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;
    public String sCreateTableCommand;
    String TAG = "TCNR05=>";

    public F10202DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //傳入的參數說明
//		context: 用來開啟或建立資料庫的應用程式物件，如 Activity 物件
//		name: 資料庫檔案名稱，若傳入 null 表示將資料庫暫存在記憶體
//		factory: 用來建立指標物件的類別，若傳入 null 表示使用預設值
//		version: 即將要建立的資料庫版本 (版本編號從 1 開始)
//		        若資料庫檔案不存在，就會呼叫 onCreate() 方法
//		        若即將建立的資料庫版本比現存的資料庫版本新，就會呼叫 onUpgrade() 方法
//		        若即將建立的資料庫版本比現存的資料庫版本舊，就會呼叫 onDowngrade() 方法
//		errHandler: 當資料庫毀損時的處理程式，若傳入 null 表示使用預設的處理程式
        super(context, "F10202.db", null, 1);
        sCreateTableCommand = "";
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public F10202DbHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    //----------------------------------------------
//     需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new FriendDbHelper(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade()");//version改變的時候走這段

        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        onCreate(db);
    }

    public int RecCount() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT*FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);

//        recSet.close();
//        db.close();
        int a = recSet.getCount();
        int b = 0;

        return a;
//        return recSet.getCount();
    }

    public ArrayList<String> getRecSet() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<>();
        //---------------------------------
        Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        recSet.close();
        db.close();

        Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }

//    public long insertRec(String b_num, String b_fish, String b_market, String b_price, String b_date) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues rec = new ContentValues();
//        rec.put("num", b_num);
//        rec.put("fish", b_fish);
//        rec.put("market", b_market);
//        rec.put("price", b_price);
//        rec.put("f_date", b_date);
//        long rowID = db.insert(DB_TABLE, null, rec);
//        db.close();
//        return rowID;
//    }

    public int clearRec() {//清除資料
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            //			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            db.close();
            return rowsAffected;
        } else {
            db.close();
            return -1;
        }
    }
}






















