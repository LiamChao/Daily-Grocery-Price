package tw.tcnr109a05.grocery.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import tw.tcnr109a05.grocery.F00000;

public class FriendsContentProvider extends ContentProvider {
    private static final String AUTHORITY = "tw.tcnr109a05.grocery";
    private static final String DB_FILE = "friends.db", DB_TABLE = "recipe";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DB_TABLE);
    private static final String DB_TABLE_f00000 = "members";
    public static final Uri CONTENT_URI_f00000 = Uri.parse("content://" + AUTHORITY + "/" + DB_TABLE_f00000);
    private static final int URI_ROOT = 0;
    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String crTBsql = "CREATE  TABLE   " + DB_TABLE + "   ( "
            + "id    INTEGER   PRIMARY KEY," + "recipe TEXT," + "introduction TEXT," +
            "ingredients TEXT," + "step TEXT," + "r_id INTEGER);";
    private static final String crTBsql_f00000 = "CREATE     TABLE   " + DB_TABLE_f00000 + "   ( "
            + "id    INTEGER   PRIMARY KEY," + "MemberID TEXT NOT NULL," + "username TEXT,"
            + "password TEXT," + "name TEXT," + "sex TEXT," + "birthday DATE," + "age TEXT," + "address TEXT,"
            + "email TEXT," + "tel TEXT," + "social TEXT," + "level INT);";

    static {
        sUriMatcher.addURI(AUTHORITY, DB_TABLE, CONTACTS);
        sUriMatcher.addURI(AUTHORITY, DB_TABLE + "/#", CONTACT_ID);
    }

    static {
        sUriMatcher.addURI(AUTHORITY, DB_TABLE_f00000, CONTACTS);
        sUriMatcher.addURI(AUTHORITY, DB_TABLE_f00000 + "/#", CONTACT_ID);
    }

    String TAG = "tcnr14=>";

    ;
    private SQLiteDatabase mFriendDb;

    @Override
    public boolean onCreate() {
        // ---宣告 使用Class DbOpenHelper.java 作為處理SQLite介面
        // Content Provider 就是 data Server, 負責儲存及提供資料, 他允許任何不同的APP使用
        // 共同的資料(不同的APP用同一個SQLite).

        DbOpenHelper friendDbOpenHelper = new DbOpenHelper(getContext(), DB_FILE, null, 1);

        mFriendDb = friendDbOpenHelper.getWritableDatabase();
        // 檢查資料表是否已經存在，如果不存在，就建立一個。
        Cursor cursor = mFriendDb
                .rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + DB_TABLE + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                // 沒有資料表，要建立一個資料表。
                mFriendDb.execSQL(crTBsql);
                mFriendDb.execSQL(crTBsql_f00000);
            }
            cursor.close();
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (sUriMatcher.match(uri) != CONTACTS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
//  Cursor c = mFriendDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)


        switch (F00000.mem) {
            case 1:

                Cursor c = mFriendDb.query(true, DB_TABLE_f00000, projection, selection, selectionArgs, null, null, sortOrder, null); //"ASC DESC"
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;

            case 2:

                Cursor d = mFriendDb.query(true, DB_TABLE, projection, selection, selectionArgs, null, null, sortOrder, null); //"ASC DESC"
                d.setNotificationUri(getContext().getContentResolver(), uri);
                return d;
        }

        return null;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) != CONTACTS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        switch (F00000.mem) {
            case 1:

                long rowId = mFriendDb.insert(DB_TABLE_f00000, null, values);
                if (rowId > 0) {
                    // 在已有的 Uri的後面追加ID數據
                    Uri insertedRowUri = ContentUris.withAppendedId(CONTENT_URI_f00000, rowId);
                    ;
                    // 通知數據已經改變
                    getContext().getContentResolver().notifyChange(insertedRowUri, null);
                    return insertedRowUri;
                }
                break;
            case 2:

                rowId = mFriendDb.insert(DB_TABLE, null, values);
                if (rowId > 0) {
                    // 在已有的 Uri的後面追加ID數據
                    Uri insertedRowUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
                    ;
                    // 通知數據已經改變
                    getContext().getContentResolver().notifyChange(insertedRowUri, null);
                    return insertedRowUri;
                }
                break;
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (sUriMatcher.match(uri) != CONTACTS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int rowsAffected = mFriendDb.delete(DB_TABLE, selection, null);
        return rowsAffected;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (sUriMatcher.match(uri) != CONTACTS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int rowsAffected = mFriendDb.update(DB_TABLE, values, selection, null);
        return rowsAffected;
    }
    // ------------------------
}