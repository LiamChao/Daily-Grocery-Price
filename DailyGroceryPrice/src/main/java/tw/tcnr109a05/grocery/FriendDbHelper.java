package tw.tcnr109a05.grocery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class FriendDbHelper extends SQLiteOpenHelper {
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料庫物件，固定的欄位變數
    private static final String DB_FILE = "friends.db"; // 資料庫名稱
    private static final String DB_TABLE = "member";    // 資料表名稱
    private static final String crTBsql = "CREATE     TABLE   " + DB_TABLE + "   ( "
            + "id    INTEGER   PRIMARY KEY," + "MemberID TEXT NOT NULL," + "username TEXT,"
            + "password TEXT," + "name TEXT," + "sex TEXT," + "birthday DATE," + "age INT," + "address TEXT,"
            + "email TEXT," + "tel TEXT," + "social TEXT" + ");";
    private static final String DB_TABLE_f10501 = "recipe";    // 資料表名稱
    private static final String crTBsql_f10501 = "CREATE  TABLE   " + DB_TABLE_f10501 + "   ( "
            + "id    INTEGER   PRIMARY KEY," + "recipe TEXT," + "introduction TEXT," +
            "ingredients TEXT," + "step TEXT," + "r_id INTEGER);";
    private static SQLiteDatabase database;
    public String sCreateTableCommand;
    String TAG = "TCNR15=>";

    public FriendDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
            database = new FriendDbHelper(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql);
        db.execSQL(crTBsql_f10501);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade()");//version改變的時候走這段

        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        onCreate(db);
    }


    public long insertRec(String b_MemberID, String b_username, String b_password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues res = new ContentValues();
        res.put("MemberID", b_MemberID);
        res.put("username", b_username);
        res.put("password", b_password);

        res.put("name", " ");
        res.put("birthday", " ");
        res.put("age", " ");
        res.put("address", " ");
        res.put("email", " ");
        res.put("tel", " ");
        res.put("social", " ");
        res.put("sex", " ");
        long rowID = db.insert(DB_TABLE, null, res);
        db.close();

        return rowID;
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

    public String FindRec(String tusername, String tpassword) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = null;
        String sql = "SELECT * FROM " + DB_TABLE + " WHERE username LIKE ? AND password LIKE ? ORDER BY id ASC";
        String[] args = {tusername, tpassword};
        Cursor recSet = db.rawQuery(sql, args);

        int columnCount = recSet.getColumnCount();
        int a = 0;
        int x = 0;
        if (recSet.getCount() != 0) {
            recSet.moveToFirst();
            fldSet = recSet.getString(0) + " "
                    + recSet.getString(1) + " "
                    + recSet.getString(2) + " "
                    + recSet.getString(3) + "\n";
            while (recSet.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    fldSet += recSet.getString(i) + " ";
                }
                fldSet += "\n";
            }
        }
        recSet.close();
        db.close();
        return fldSet;
    }

    public String FindRec1(String tusername) {//使帳號不能重複

        SQLiteDatabase db = getReadableDatabase();
        String fldSet = null;
        String sql = "SELECT * FROM " + DB_TABLE + " WHERE username LIKE ?";
        String[] args = {tusername};
        Cursor recSet = db.rawQuery(sql, args);

        int columnCount = recSet.getColumnCount();
        int a = 0;
        int x = 0;
        if (recSet.getCount() != 0) {
            recSet.moveToFirst();
            fldSet = recSet.getString(0) + " "
                    + recSet.getString(1) + " "
                    + recSet.getString(2) + " "
                    + recSet.getString(3) + "\n";
            while (recSet.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    fldSet += recSet.getString(i) + " ";
                }
                fldSet += "\n";
            }
        }
        recSet.close();
        db.close();
        return fldSet;
    }

    public String Findid(String tusername) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = null;
        String sql = "SELECT * FROM " + DB_TABLE + " WHERE username LIKE ?";
        String[] args = {tusername};
        Cursor recSet = db.rawQuery(sql, args);

        int columnCount = recSet.getColumnCount();
        if (recSet.getCount() != 0) {
            recSet.moveToFirst();
            fldSet = recSet.getString(0);
            while (recSet.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    fldSet += recSet.getString(i) + " ";
                }
                fldSet += "\n";
            }
        }
        recSet.close();
        db.close();
        return fldSet;
    }

    public int updateRec(String b_id, String b_password) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);

        if (recSet.getCount() != 0) {
            ContentValues rec = new ContentValues();
            rec.put("password", b_password);
            String whereClause = "id = '" + b_id + "'";
            int rowsAffected = db.update(DB_TABLE, rec, whereClause, null);
            db.close();
            return rowsAffected;
        } else {
            db.close();
            return -1;
        }
    }

    public int updateRec_F10701(String b_id, String b_tname, String b_birthday, String b_age, String b_address, String b_email, String b_tel, String b_social, String b_sex) {//更新會員資料
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);

        if (recSet.getCount() != 0) {
            ContentValues rec = new ContentValues();
            rec.put("name", b_tname);
            rec.put("birthday", b_birthday);
            rec.put("age", b_age);
            rec.put("address", b_address);
            rec.put("email", b_email);
            rec.put("tel", b_tel);
            rec.put("social", b_social);
            rec.put("sex", b_sex);
            String whereClause = "id = '" + b_id + "'";
            int rowsAffected = db.update(DB_TABLE, rec, whereClause, null);
            db.close();
            return rowsAffected;
        } else {
            db.close();
            return -1;
        }
    }

    public long insert_f10501(String s_t001, String s_t002, String s0, String s1, int i) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues res = new ContentValues();
        res.put("recipe", s_t001);
        res.put("introduction", s_t002);

        res.put("ingredients", s0);
        res.put("step", s1);
        res.put("r_id", i);


        long rowID = db.insert(DB_TABLE_f10501, null, res);
        int ccc = 0;
        db.close();

        return rowID;
    }

    public ArrayList<String> getRecSet() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_f10501 + " WHERE id ORDER BY id DESC";
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "♨";
            recAry.add(fldSet);
        }
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> getRecSet_F10700(String u_id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE + " WHERE id LIKE ?";
        String[] args = {u_id};
        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "♨";
            recAry.add(fldSet);
        }
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> getRecSet_id() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_f10501 + " WHERE r_id LIKE 1  ";
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "♨";
            recAry.add(fldSet);
        }
        recSet.close();
        db.close();
        return recAry;
    }

    public int getID() {
        return 1;
    }
}






















