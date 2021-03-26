package tw.careabout.yourfamily.info;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String[] DB_TABLE = new String[]{"member", "elder", "mededit", "record"};
    private static final String[] INFO_TABLE =
            {"welfare01", "welfare02", "welfare03", "welfare04", "welfare05",
                    "institution01", "institution02", "institution03", "institution04", "institution05", "institution06",
                    "facilities01", "facilities02", "facilities03", "facilities04", "facilities05", "facilities06", "facilities07"};

    String crTBsql = "";


    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        for (String tableName : INFO_TABLE) {
            crTBsql = " CREATE TABLE IF NOT EXISTS " + tableName + "(ifo001 INTEGER PRIMARY KEY," +
                    "ifo002 TEXT,ifo003 TEXT,ifo004 TEXT,ifo005 TEXT,ifo006 TEXT,ifo007 TEXT,ifo008 TEXT," +
                    "ifo009 TEXT,ifo010 TEXT,ifo011 TEXT,ifo012 TEXT)";

            db.execSQL(crTBsql);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String s : DB_TABLE) {
            db.execSQL("DROP TABLE IF EXISTS " + s);
        }
        onCreate(db);

    }

    public int clearRec(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM " + tableName;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            int rowsAffected = db.delete(tableName, "1", null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public void chkAllTableExists() {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        db.close();
    }

    public long insertRec_mysql(ContentValues rec, String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowID = db.insert(s, null, rec);
        db.close();
        return rowID;
    }


    //for info ==================================================================
    public ArrayList<String> getInfoDistrict(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT ifo008 FROM " + tableName;

        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i);
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }


    public ArrayList<String> getInfoRec(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + tableName;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();

        //----------------------------
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

        return recAry;
    }
}
