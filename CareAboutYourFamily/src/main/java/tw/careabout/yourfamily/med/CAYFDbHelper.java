package tw.careabout.yourfamily.med;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class CAYFDbHelper extends SQLiteOpenHelper {
    public String sCreateTableCommand;    // 資料庫名稱
    private static final String DB_FILE = "CAYF.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    private static final String DB_TABLE = "med";    // 資料庫物件，固定的欄位變數

    private static final String crTBsql = " CREATE TABLE IF NOT EXISTS " + DB_TABLE + "(med001 INTEGER PRIMARY KEY," +
            "med002 TEXT,med003 TEXT,med004 TEXT,med005 TEXT,med006 TEXT,med007 TEXT,med008 TEXT," +
            "med009 TEXT,med010 TEXT,med011 TEXT,med012 TEXT)";


    public CAYFDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql);
    }

    //使用sql 指令ctTB動作
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);

    }

    public long insertRec(String content, String settime,String reqID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("med002", content);
        rec.put("med003", settime);
        rec.put("med001", reqID);
        long rowID = db.insert(DB_TABLE, null, rec);
        db.close();
        return rowID;
    }

    public long updateRec(String id, String content, String settime,String reqID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("med002", content);
        rec.put("med003", settime);
        rec.put("med001", reqID);
        String[] whereArgs = {id};
        long rowID = db.update(DB_TABLE, rec, "med001 =?", whereArgs);
        db.close();
        return rowID;

    }

//    public int Recount() {
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "SELECT * FROM " + DB_TABLE;
//        Cursor recSet = db.rawQuery(sql, null);
//        return recSet.getCount();
//        //這一段語法是說這一段有幾筆
//    }

    public void createTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(crTBsql);
        db.close();
    }


    public ArrayList<String> setRec() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT med001,med002,med003 FROM med";
        ArrayList<String> recAry = new ArrayList<>();
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            int columnCount = recSet.getColumnCount();
            while (recSet.moveToNext()) {
                String fldSet = "";
                for (int i = 0; i < columnCount; i++) {
                    fldSet += (recSet.getString(i)) + "#";
                }
                recAry.add(fldSet);
            }
        }
        recSet.close();
        db.close();
        return recAry;
    }

    public void delRec(String med001) {
        SQLiteDatabase db = getWritableDatabase();
        String[] where = {med001};
        db.delete(DB_TABLE, "med001=?", where);
        db.close();


    }
}