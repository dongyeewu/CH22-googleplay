package tw.careabout.yourfamily.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HomeDbHelper extends SQLiteOpenHelper {

    private static final String DB_TABLE = "member";    // 資料庫物件，固定的欄位變數
    private static final String createMemberTable = "CREATE TABLE IF NOT EXISTS " + DB_TABLE + " ( "
            + "mem001 INTEGER ," + "mem002 TEXT ," + "mem003 TEXT ,"
            + "mem004 TEXT NOT NULL,"  + "mem005 TEXT NOT NULL," + "mem006 TEXT NOT NULL," + "mem007 INTEGER ,"
            + "mem008 TEXT ,"  + "mem009 TEXT ," + "mem010 TEXT ," + "mem011 TEXT ,"
            + "mem012 TEXT ,"  + "mem013 TEXT ," + "mem014 TEXT ) ";

    public HomeDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createHomeTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(createMemberTable);
    }

    //--檢查帳戶是否已建立
    public int checkMemberTable(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT * FROM " + DB_TABLE + " WHERE mem004 LIKE ? ";
        String arg[] = { email } ;
        Cursor memberRec = db.rawQuery(sql,arg); //arg一定要是字串陣列
        if ( memberRec.getCount()!=0 ){
            memberRec.close();
            db.close();
            return 1;
        }
        else{
            memberRec.close();
            db.close();
            return 0;
        }
    }

    public int checkMemberTable() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = " SELECT * FROM " + DB_TABLE ;
        Cursor memberRec = db.rawQuery(sql,null);
        if ( memberRec.getCount()!=0 ){
            memberRec.close();
            db.close();
            return 1;
        }
        else{
            memberRec.close();
            db.close();
            return 0;
        }
    }

    //--建立member SQLite資料
    public long insertMemberRec(int mem001, String mem002, String mem003, String mem004, String mem005, String mem006, int mem007,
                                     String mem008, String mem009, String mem010, String mem011, String mem012, String mem013 ,String mem014) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues rec = new ContentValues();
        rec.put("mem001", mem001); //id
        rec.put("mem002", mem002); //GivenName
        rec.put("mem003", mem003); //FamilyName
        rec.put("mem004", mem004); //Email
        rec.put("mem005", mem005); //DisplayName
        rec.put("mem006", mem006); //照片URL
        rec.put("mem007", mem007); //群組ID
        rec.put("mem008", mem008); //最後登入時間
        rec.put("mem009", mem009); //預備欄位
        rec.put("mem010", mem010); //預備欄位
        rec.put("mem011", mem011); //預備欄位
        rec.put("mem012", mem012); //預備欄位
        rec.put("mem013", mem013); //預備欄位
        rec.put("mem014", mem014); //預備欄位
        long rowID = db.insert(DB_TABLE, null, rec);
        db.close();
        return rowID;
    }

    //--清空 member SQLite資料
    public void clearMemberRec(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            int rowsAffected = db.delete(DB_TABLE, "1", null); //
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            cursor.close();
            db.close();
        } else {
            cursor.close();
            db.close();
        }
    }

    //--讀取現在登入的使用者 SQLite 資料
    public ArrayList<String> getUser(String email) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> userRowData = new ArrayList<>();
        String sql = "SELECT*FROM "+DB_TABLE+" WHERE mem004 = ?";
        String[] args = { email }; //問號的查詢條件
        Cursor cursor = db.rawQuery(sql,args); //將args[ ]帶入sql的問號，會按照順序放
        int columnCount = cursor.getColumnCount();  //看有幾個欄位，column:欄位

        if( cursor.getCount()!=0 ){
            cursor.moveToFirst();
            for(int i=0; i<columnCount; i++){
                userRowData.add(cursor.getString(i));
            }
        }
        cursor.close();
        db.close();
        return userRowData;
    }

    public ArrayList<String> getUser() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> userRowData = new ArrayList<>();
        String sql = "SELECT*FROM "+DB_TABLE;
        Cursor cursor = db.rawQuery(sql,null); //將args[ ]帶入sql的問號，會按照順序放
        int columnCount = cursor.getColumnCount();  //看有幾個欄位，column:欄位

        if( cursor.getCount()!=0 ){
            cursor.moveToFirst();
            for(int i=0; i<columnCount; i++){
                userRowData.add(cursor.getString(i));
            }
        }
        cursor.close();
        db.close();
        return userRowData;
    }

}
