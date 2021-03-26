package tw.careabout.yourfamily.record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/*** Created by vpoint88 on 2017/7/6.*/
////----------------------------------------------------------
//建構式參數說明：
//context
//可以操作資料庫的內容本文，一般可直接傳入Activity物件。
//name
//要操作資料庫名稱，如果資料庫不存在，會自動被建立出來並呼叫onCreate()方法。
//factory
//用來做深入查詢用，入門時用不到。
//version
//版本號碼。
////-----------------------

public class Record_DbHelper extends SQLiteOpenHelper {
    //    String TAG = "tcnr17=>";
    public String sCreateTableCommand;    // 資料庫名稱
    private static final String DB_FILE = "CAYF.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    private static final String DB_RESH_join_rs = "resh_join_rs",
            DB_TABLE_ELD_re = "elder_re",
            DB_TABLE_REC_re = "recordedit_re";    // 資料庫物件，固定的欄位變數
    private static final String crRESHjoinsql_rs = "CREATE TABLE IF NOT EXISTS "
            + DB_RESH_join_rs
            + "(mem001 INTEGER, mem004 TEXT, mem007 INTEGER, eld007 INTEGER,  "
            + " eld001 INTEGER  ,eld002 TEXT,  eld004 TEXT , eld005 TEXT, eld006 TEXT, eld003 TEXT  )";
    private static final String crTBELDsql_re = "CREATE TABLE IF NOT EXISTS "
            + DB_TABLE_ELD_re + "(" +
            "eld001 INTEGER," +
            "eld002 TEXT,eld004 TEXT," +
            "eld005 TEXT,eld006 TEXT," +
            "eld007 INTEGER);";
    private static final String crTBRECsql_re = "CREATE TABLE IF NOT EXISTS "
            + DB_TABLE_REC_re + "(" +
            "rec001 INTEGER PRIMARY KEY," +
            "rec002 INTEGER,rec003 REAL," +
            "rec004 REAL,rec005 REAL," +
            "rec006 INTEGER,rec007 INTEGER," +
            "rec008 INTEGER,rec009 INTEGER," +
            "rec010 INTEGER,rec011 INTEGER," +
            "rec012 INTEGER,rec013 INTEGER," +
            "rec014 TEXT,rec015 TEXT," +
            "rec016 TEXT,rec017 TEXT," +
            "rec018 INTEGER);";

    public Record_DbHelper(Context context) {
        // super(context, name, factory, version);
        super(context, DB_FILE, null, VERSION);
        sCreateTableCommand = "";
    }

    public Record_DbHelper(Context context, String DB_name, int version) {
        // super(context, name, factory, version);
        super(context, DB_name, null, version);
        sCreateTableCommand = "";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crRESHjoinsql_rs);
        db.execSQL(crTBELDsql_re);
        db.execSQL(crTBRECsql_re);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.d(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + DB_RESH_join_rs);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_ELD_re);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_REC_re);
        onCreate(db);
    }

    public void createTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(crRESHjoinsql_rs);
        db.execSQL(crTBELDsql_re);
        db.execSQL(crTBRECsql_re);
        db.close();
    }

    public ArrayList<String> getrecJoinSet_rs() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_RESH_join_rs;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<>();

        //----------------------------
//        Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();

        while (recSet.moveToNext()) {
            StringBuilder fldSet = new StringBuilder();
            for (int i = 0; i < columnCount; i++)
                fldSet.append(recSet.getString(i)).append("#");
            recAry.add(fldSet.toString());
        }
        //------------------------
        recSet.close();
        db.close();

//        Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }

    public int reshJoinCount_rs() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_RESH_join_rs;
        Cursor recSet = db.rawQuery(sql, null);
        int a = recSet.getCount();
        recSet.close();
        db.close();
        return a;
    }

    public long insertRec_j_rs(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_RESH_join_rs, null, rec);
        db.close();
        return rowID;
    }

    public int clearRESHjoin_rs() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_RESH_join_rs;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            //db.delete(DB_RESH_join_rs, "1", null); //
            int rowsAffected = db.delete(DB_RESH_join_rs, "1", null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public ArrayList<String> findeldSet_re() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_ELD_re + "";
        ArrayList<String> eldAry = new ArrayList<>();
        Cursor eldSet = db.rawQuery(sql, null);
        if (eldSet.getCount() != 0) {
            int columnCount = eldSet.getColumnCount();
            while (eldSet.moveToNext()) {
                StringBuilder fldSet = new StringBuilder();
                for (int i = 0; i < columnCount; i++)
                    fldSet.append(eldSet.getString(i)).append("#");
                eldAry.add(fldSet.toString());
            }
        }
        eldSet.close();
        db.close();
        return eldAry;
    }

    public ArrayList<String> findrecSet_re() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_REC_re + " ORDER BY rec001 DESC ";
        ArrayList<String> recAry = new ArrayList<>();
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            int columnCount = recSet.getColumnCount();
            while (recSet.moveToNext()) {
                StringBuilder fldSet = new StringBuilder();
                for (int i = 0; i < columnCount; i++)
                    fldSet.append(recSet.getString(i)).append("#");
                recAry.add(fldSet.toString());
            }
        }
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> findrecSet_re( String eldID ) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_REC_re + " WHERE rec002 = ? ORDER BY rec001 DESC";
        String[] args = { eldID }; //問號的查詢條件
        ArrayList<String> recAry = new ArrayList<>();
        Cursor recSet = db.rawQuery(sql, args);
        if (recSet.getCount() != 0) {
            int columnCount = recSet.getColumnCount();
            while (recSet.moveToNext()) {
                StringBuilder fldSet = new StringBuilder();
                for (int i = 0; i < columnCount; i++)
                    fldSet.append(recSet.getString(i)).append("#");
                recAry.add(fldSet.toString());
            }
        }
        recSet.close();
        db.close();
        return recAry;
    }
    //    ContentValues values
    public void eldInsert_m_re(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(DB_TABLE_ELD_re, null, rec);
        db.close();
    }

    public void recInsert_m_re(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(DB_TABLE_REC_re, null, rec);
        db.close();
    }

    public void recInsert_m_re(ArrayList<String> recordRowData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("rec002", recordRowData.get(6));
        rec.put("rec003", recordRowData.get(7));
        rec.put("rec004", recordRowData.get(8));
        rec.put("rec005", recordRowData.get(9));
        rec.put("rec006", recordRowData.get(10));
        rec.put("rec007", recordRowData.get(11));
        rec.put("rec008", recordRowData.get(12));
        rec.put("rec009", recordRowData.get(13));
        rec.put("rec010", recordRowData.get(14));
        rec.put("rec011", recordRowData.get(15));
        rec.put("rec012", recordRowData.get(16));
        rec.put("rec013", recordRowData.get(17));
        rec.put("rec014", recordRowData.get(18));
        rec.put("rec015", recordRowData.get(19));
        rec.put("rec016", recordRowData.get(20));
        rec.put("rec017", recordRowData.get(21));
        rec.put("rec018", recordRowData.get(22));
        long rowID = db.insert(DB_TABLE_REC_re, null, rec);
        db.close();
    }

    public void recUpdate_m_re(ArrayList<String> recordRowData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        String whereClause = "rec001=?";
        String whereArg[] = { recordRowData.get(5) };

        rec.put("rec002", recordRowData.get(6));
        rec.put("rec003", recordRowData.get(7));
        rec.put("rec004", recordRowData.get(8));
        rec.put("rec005", recordRowData.get(9));
        rec.put("rec006", recordRowData.get(10));
        rec.put("rec007", recordRowData.get(11));
        rec.put("rec008", recordRowData.get(12));
        rec.put("rec009", recordRowData.get(13));
        rec.put("rec010", recordRowData.get(14));
        rec.put("rec011", recordRowData.get(15));
        rec.put("rec012", recordRowData.get(16));
        rec.put("rec013", recordRowData.get(17));
        rec.put("rec014", recordRowData.get(18));
        rec.put("rec015", recordRowData.get(19));
        rec.put("rec016", recordRowData.get(20));
        rec.put("rec017", recordRowData.get(21));
        rec.put("rec018", recordRowData.get(22));

        db.update(DB_TABLE_REC_re, rec, whereClause, whereArg );
        db.close();
    }

    //--刪除 Record SQLite資料
    public void deleteRecordRec(int id){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_REC_re + " WHERE rec001 = ? ";
        String arg[] = { String.valueOf(id) };
        Cursor cursor = db.rawQuery(sql, arg);

        if (cursor.getCount() != 0) {
            String whereClause = " rec001 = ? ";
            int rowsAffected = db.delete(DB_TABLE_REC_re, whereClause, arg);
            cursor.close();
            db.close();
        } else {
            cursor.close();
            db.close();
        }
    }
    
    public void eldClear_re() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_ELD_re;
        Cursor eldSet = db.rawQuery(sql, null);
        if (eldSet.getCount() != 0) {
            db.delete(DB_TABLE_ELD_re, "1", null); //
        }
        eldSet.close();
        db.close();
    }

    public void recClear_re() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_REC_re;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            db.delete(DB_TABLE_REC_re, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
        }
        recSet.close();
        db.close();
    }

}
