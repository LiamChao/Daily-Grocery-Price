package tw.tcnr109a05.grocery;

import android.content.ContentValues;
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

public class FriendDbHelper_Price extends SQLiteOpenHelper {
    public static final int VERSION = 1;   // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    private static final DatabaseErrorHandler errorHandler = null;
    private static final Context context = null;
    private static final String DB_FILE = "price.db";    // 資料庫名稱
    private static final String DB_TABLE_F10200 = "crop";   // 資料表名稱
    private static final String DB_TABLE_F10201 = "poultry";
    private static final String DB_TABLE_F10202 = "fish";
    private static final String crTBsql_F10200 = "CREATE TABLE " + DB_TABLE_F10200 + " ( "
            + "id INTEGER PRIMARY KEY," + "num TEXT NOT NULL," + "crop TEXT,"
            + "market TEXT," + "price DECIMAL," + "c_date TEXT);";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql_F10201 = "CREATE TABLE " + DB_TABLE_F10201 + " ( "
            + "id INTEGER PRIMARY KEY," + "chicken200 DECIMAL NOT NULL," + "chicken195 DECIMAL,"
            + "chicken_retail DECIMAL," + "egg DECIMAL," + "p_date TEXT);";
    private static final String crTBsql_F10202 = "CREATE TABLE " + DB_TABLE_F10202 + " ( "
            + "id INTEGER PRIMARY KEY," + "num TEXT NOT NULL," + "fish TEXT,"
            + "market Text," + "price DECIMAL," + "f_date TEXT);";
    private static SQLiteDatabase database;
    public String sCreateTableCommand;
    String TAG = "TCNR05=>";

    public FriendDbHelper_Price(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //傳入的參數說明
//		context: 用來開啟或建立資料庫的應用程式物件，如 Activity 物件
//		name: 資料庫檔案名稱，若傳入 null 表示將資料庫暫存在記憶體
//		factory: 用來建立指標物件的類別，若傳入 null 表示使用預設值
//		version: 即將要建立的資料庫版本 (版本編號從 1 開始)
//		        若資料庫檔案不存在，就會呼叫 onCreate() 方法
//		        若即將建立的資料庫版本比現存的資料庫版本新，就會呼叫 onUpgrade() 方法
//		        若即將建立的資料庫版本比現存的資料庫版本舊，就會呼叫 onDowngrade() 方法
//		errHandler: 當資料庫毀損時的處理程式，若傳入 null 表示使用預設的處理程式
        super(context, "price.db", null, 1);
        sCreateTableCommand = "";
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public FriendDbHelper_Price(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    //----------------------------------------------
//     需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
//    public static SQLiteDatabase getDatabase(Context context) {
//        if (database == null || !database.isOpen()) {
//            database = new FriendDbHelper(context, DB_FILE, null, VERSION)
//                    .getWritableDatabase();
//        }
//        return database;
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql_F10200);
        db.execSQL(crTBsql_F10201);
        db.execSQL(crTBsql_F10202);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade()");//version改變的時候走這段

        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_F10200);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_F10201);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_F10202);

        onCreate(db);
    }

    public int RecCount_F10200() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT*FROM " + DB_TABLE_F10200;
        Cursor recSet = db.rawQuery(sql, null);

//        recSet.close();
//        db.close();
        //------------方便debug用---------------
        int a = recSet.getCount();
        int b = 0;
        recSet.close();//要放這位置，在上面就先關掉cursor的話會閃退
        db.close();//原先的寫法回傳還沒執行就關掉，會GG
        return a;
        //-------------跟return那行一樣--------------
//        return recSet.getCount();
    }

    public int RecCount_F10201() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT*FROM " + DB_TABLE_F10201;
        Cursor recSet = db.rawQuery(sql, null);

//        recSet.close();
//        db.close();
        //------------方便debug用---------------
        int a = recSet.getCount();
        int b = 0;
        recSet.close();//要放這位置，在上面就先關掉cursor的話會閃退
        db.close();//原先的寫法回傳還沒執行就關掉，會GG
        return a;
        //-------------跟return那行一樣--------------
//        return recSet.getCount();
    }

    public int RecCount_F10202() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT*FROM " + DB_TABLE_F10202;
        Cursor recSet = db.rawQuery(sql, null);

//        recSet.close();
//        db.close();
        //------------方便debug用---------------
        int a = recSet.getCount();
        int b = 0;
        recSet.close();//要放這位置，在上面就先關掉cursor的話會閃退
        db.close();//原先的寫法回傳還沒執行就關掉，會GG
        return a;
        //-------------跟return那行一樣--------------
//        return recSet.getCount();
    }

    public ArrayList<String> getRecSet_F10200() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_F10200;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
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

    public ArrayList<String> getRecSet_F10201() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_F10201;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
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

    public ArrayList<String> getRecSet_F10202() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_F10202;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
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

    public long insertRec_F10200(String b_num, String b_crop, String b_market, String b_price, String b_date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("num", b_num);
        rec.put("crop", b_crop);
        rec.put("market", b_market);
        rec.put("price", b_price);
        rec.put("c_date", b_date);
        long rowID = db.insert(DB_TABLE_F10200, null, rec);
        db.close();
        return rowID;
    }

    public long insertRec_F10201(String b_chicken200, String b_chicken195, String b_chicken_retail, String b_egg, String b_date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("chicken200", b_chicken200);
        rec.put("chicken195", b_chicken195);
        rec.put("chicken_retail", b_chicken_retail);
        rec.put("egg", b_egg);
        rec.put("p_date", b_date);
        long rowID = db.insert(DB_TABLE_F10201, null, rec);
        db.close();
        return rowID;
    }

    public long insertRec_F10202(String b_num, String b_fish, String b_market, String b_price, String b_date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("num", b_num);
        rec.put("fish", b_fish);
        rec.put("market", b_market);
        rec.put("price", b_price);
        rec.put("f_date", b_date);
        long rowID = db.insert(DB_TABLE_F10202, null, rec);
        db.close();
        return rowID;
    }

    //================================================================================
    public int clearRec_F10200() {//清除資料
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_F10200;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            //			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_F10200, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            db.close();
            return rowsAffected;
        } else {
            db.close();
            return -1;
        }
    }

    public int clearRec_F10201() {//清除資料
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_F10201;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            //			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_F10201, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            db.close();
            return rowsAffected;
        } else {
            db.close();
            return -1;
        }
    }

    public int clearRec_F10202() {//清除資料
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_F10202;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            //			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_F10202, "1", null); //
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






















