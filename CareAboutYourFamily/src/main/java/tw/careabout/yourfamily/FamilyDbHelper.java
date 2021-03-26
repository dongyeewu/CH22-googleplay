package tw.careabout.yourfamily;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class FamilyDbHelper extends SQLiteOpenHelper {
    String TAG = "tcnr18=>";
    public String sCreateTableCommand;    // 資料庫名稱
    private static final String DB_FILE = "CAYF.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    private static final String DB_TABLE = "family";    // 資料庫物件，固定的欄位變數
    private static final String DB_TABLE_fm= "family_member";    
    private static final String DB_TABLE_m= "member";
    private static final String DB_TABLE_e= "elder";
    private static final String crTBsql = "CREATE TABLE IF NOT EXISTS " + DB_TABLE +
            " (fam001  INTEGER PRIMARY KEY, fam002 TEXT NOT NULL,fam003 TEXT NOT NULL,fam004 TEXT NOT NULL,fam005 TEXT NOT NULL," +
            "fam006 TEXT NOT NULL,fam007 TEXT NOT NULL,fam008 TEXT NOT NULL,fam009 TEXT NOT NULL,fam010 TEXT NOT NULL); ";

    private static final String crTBsql_fm= "CREATE TABLE IF NOT EXISTS " + DB_TABLE_fm + " ( "
            + "mem001 INTEGER ," + "mem002 TEXT ," + "mem003 TEXT ,"
            + "mem004 TEXT NOT NULL,"  + "mem005 TEXT NOT NULL," + "mem006 TEXT NOT NULL," + "mem007 INTEGER ,"
            + "mem008 TEXT ,"  + "mem009 TEXT ," + "mem010 TEXT ," + "mem011 TEXT ,"
            + "mem012 TEXT ,"  + "mem013 TEXT ," + "mem014 TEXT ) ";
    private static SQLiteDatabase database;
    //----------------------------------------------
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new FamilyDbHelper(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }
    public FamilyDbHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context, "CAYF.db", null, 1);
        Log.d(TAG, "FamilyDbHelper()");
        sCreateTableCommand="";
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
        Log.d(TAG, "onUpgrade()");

        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        onCreate(db);
    }

    public  long fam_s_c(String fam002, String fam003, String fam004) {
            SQLiteDatabase db=getWritableDatabase();
            ContentValues newRow = new ContentValues();//SQLite 一筆資料
            newRow.put("fam002", fam002);
            newRow.put("fam003", fam003);
            newRow.put("fam004", fam004);
            long rowID=db.insert(DB_TABLE, null, newRow);
            db.close();
        return  rowID;
    }

    private void fam_s_list() {
        SQLiteDatabase db=getReadableDatabase();

    }

    //----My->Lite----
    public long fam_s_m(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE, null, rec);
        db.close();
        return rowID;
    }
    public long mem_s_m(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_m, null, rec);
        db.close();
        return rowID;
    }
    public long fme_s_m(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_fm, null, rec);
        db.close();
        return rowID;
    }
    public  int clearRec() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
//        //-----------
//        Cursor c1=db.execSQL("");
//        Cursor c2=db.rawQuery();
//        Cursor c3=db.insert();
//        Cursor c4=db.update(, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , );
//        Cursor c5=db.delete();
        int rowsAffected=-1;
//        //------------------------------------------------
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            rowsAffected = db.delete(DB_TABLE, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
        }
        recSet.close();
        db.close();
        return rowsAffected;
    }
    public  int clearRec_m() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_m;
        Cursor recSet = db.rawQuery(sql, null);
        int rowsAffected=-1;
//        //------------------------------------------------
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            rowsAffected = db.delete(DB_TABLE_m, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
        }
        recSet.close();
        db.close();
        return rowsAffected;
    }
    public  int clearRec_fm() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_fm;
        Cursor recSet = db.rawQuery(sql, null);
        int rowsAffected=-1;
//        //------------------------------------------------
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            rowsAffected = db.delete(DB_TABLE_fm, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
        }
        recSet.close();
        db.close();
        return rowsAffected;
    }
    //抓member資料
    public ArrayList<String> getRecSet_m() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_m;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();

        //----------------------------
        Log.d(TAG, "recSet_m=" + recSet);
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

    //抓member資料
    public ArrayList<String> getRecSet_fm() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_fm;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();

        //----------------------------
        Log.d(TAG, "recSet_fm=" + recSet);
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
    //抓ellder資料
    public ArrayList<String> getRecSet_e() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_e;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();

        //----------------------------
        Log.d(TAG, "recSet_e=" + recSet);
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


    public void createTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(crTBsql);
        db.close();
    }

    public void createTable_fm() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(crTBsql_fm);
        db.close();
    }
}