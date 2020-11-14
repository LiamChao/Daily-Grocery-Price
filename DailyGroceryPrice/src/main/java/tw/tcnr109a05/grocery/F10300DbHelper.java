package tw.tcnr109a05.grocery;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class F10300DbHelper extends SQLiteOpenHelper {

    public static final String DB_TABLE = "nutrition";
    public static final String COLUMN_NAME = "樣品名稱";
    public static final String COLUMN_ITEM = "分析項";
    public static final String COLUMN_Content = "每100克含量";
    public static final String COLUMN_Description = "內容物描述";
    private static final String TAG = F10300DbHelper.class.getSimpleName();
    private static final String DB_FILE = "nutrition.db";
    private static final int DBversion = 1;
    Context context;
    SQLiteDatabase db;
    private Resources mResources;

    public F10300DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_FILE, null, DBversion);
//        sCreateTableCommand = "";
        mResources = context.getResources();
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BUGS_TABLE = "CREATE TABLE " + DB_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT  NOT NULL, " +
                COLUMN_ITEM + " TEXT NOT NULL, " +
                COLUMN_Content + " FLOAT NOT NULL, " +
                COLUMN_Description + " TEXT NOT NULL " + " );";

        db.execSQL(SQL_CREATE_BUGS_TABLE);
        Log.d(TAG, "Database Created Successfully");

        try {
            readDataToDb(db);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void readDataToDb(SQLiteDatabase db) throws IOException, JSONException {

        final String MNU_NAME = "樣品名稱";
        final String MNU_ITEM = "分析項";
        final String MNU_CONTENT = "每100克含量";
        final String MNU_DESCRIPTION = "內容物描述";

        try {
            String jsonDataString = readJsonDataFromFile();
            JSONArray menuItemsJsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < menuItemsJsonArray.length(); ++i) {

                String name;
                String item;
                String content;
                String description;

                JSONObject menuItemObject = menuItemsJsonArray.getJSONObject(i);

                name = menuItemObject.getString(MNU_NAME);
                item = menuItemObject.getString(MNU_ITEM);
                content = menuItemObject.getString(MNU_CONTENT);
                description = menuItemObject.getString(MNU_DESCRIPTION);

                ContentValues menuValues = new ContentValues();

                menuValues.put(COLUMN_NAME, name);
                menuValues.put(COLUMN_ITEM, item);
                menuValues.put(COLUMN_Content, content);
                menuValues.put(COLUMN_Description, description);

                db.insert(DB_TABLE, null, menuValues);
                Log.d(TAG, "Inserted Successfully " + menuValues);
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private String readJsonDataFromFile() throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String jsonDataString = null;
            inputStream = mResources.openRawResource(R.raw.nutrition_0802);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                builder.append(jsonDataString);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return new String(builder);
    }

    public ArrayList<String> getRecSet() {
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

        Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }
}




















